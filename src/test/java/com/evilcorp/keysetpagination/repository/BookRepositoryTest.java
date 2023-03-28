package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.Book;
import com.evilcorp.keysetpagination.testcontainers.TestcontainersInitializer;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
@EnabledIf(expression = "${keysetpagination.testcontainers.enabled}", loadContext = true)
@Rollback(value = false)
@Transactional(propagation = Propagation.NEVER)
@Slf4j
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository   ;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        bookRepository.saveAll(
                Stream.generate(this::generateBooks)
                        .limit(9).toList()
        );
    }

    @Test
    void testFindBooksByFilter() {
        var firstPage = bookRepository.findFirst(Pageable.ofSize(4));
        assertEquals(4, firstPage.size());
        var book = firstPage.get(3);

        var secondPage = bookRepository.findAllByFilter(book.getRating(), book.getId(), Pageable.ofSize(4));
        assertEquals(4, secondPage.size());
        var secondBook = secondPage.get(3);

        var thirdPage = bookRepository.findAllByFilter(secondBook.getRating(), secondBook.getId(), Pageable.ofSize(4));
        assertEquals(3, thirdPage.size());
        var books = Stream.of(firstPage,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Book::getId)
                .collect(Collectors.toSet());
        assertEquals(9, books.size());
    }

    @Test
    void testFilter() {
        var first = bookRepository.findFirstByFilter(Pageable.ofSize(4));
        assertEquals(4, first.size());
        var app = first.get(3);

        var secondPage = bookRepository.findAllByFilter(Pageable.ofSize(4), app.getCreatedAt(), app.getId());
        assertEquals(4, secondPage.size());
        var second = secondPage.get(3);

        var thirdPage = bookRepository.findAllByFilter(Pageable.ofSize(4), second.getCreatedAt(), second.getId());
        assertEquals(3, thirdPage.size());
        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Book::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
    }
    @Test
    void testPgFilter() {
        var first = bookRepository.findFirstByFilter(Pageable.ofSize(4));
        assertEquals(4, first.size());
        var app = first.get(3);

        var secondPage = bookRepository.findAllByPGShortFilter(4, app.getCreatedAt(), app.getId());
        assertEquals(4, secondPage.size());
        var second = secondPage.get(3);

        var thirdPage = bookRepository.findAllByPGShortFilter(4, second.getCreatedAt(), second.getId());
        assertEquals(3, thirdPage.size());
        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Book::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
    }

    @Test
    void testPgJpqlFilter() {
        var first = bookRepository.findFirstByFilter(Pageable.ofSize(4));
        assertEquals(4, first.size());
        var app = first.get(3);

        var secondPage = bookRepository.findAllByPGShortFilterJpql(app.getCreatedAt(), app.getId(), Pageable.ofSize(4));
        assertEquals(4, secondPage.size());
        var second = secondPage.get(3);

        var thirdPage = bookRepository.findAllByPGShortFilterJpql(second.getCreatedAt(), second.getId(), Pageable.ofSize(4));
        assertEquals(3, thirdPage.size());
        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Book::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
    }

    @Test
    void testPageFilter() {
        var first = bookRepository.findAll(PageRequest.of(0, 3)).getContent();
        assertEquals(3, first.size());

        var secondPage = bookRepository.findAll(PageRequest.of(1, 3)).getContent();
        assertEquals(3, secondPage.size());

        var thirdPage = bookRepository.findAll(PageRequest.of(2, 3)).getContent();
        assertEquals(3, thirdPage.size());
        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Book::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
        var nonExistPage = bookRepository.findAll(PageRequest.of(3, 3)).getContent();
        assertEquals(0, nonExistPage.size());
    }

    @Test
    void testSliceFilter() {
        var first = bookRepository.findAllBy(PageRequest.of(0, 3)).getContent();
        assertEquals(3, first.size());

        var secondPage = bookRepository.findAllBy(PageRequest.of(1, 3)).getContent();
        assertEquals(3, secondPage.size());

        var thirdPage = bookRepository.findAllBy(PageRequest.of(2, 3)).getContent();
        assertEquals(3, thirdPage.size());
        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Book::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
        var nonExistPage = bookRepository.findAllBy(PageRequest.of(3, 3)).getContent();
        assertEquals(0, nonExistPage.size());
    }

    @Test
    void testReversPageLoading() {
        int pageSize = 3;
        long count = bookRepository.count();
        var half = (count / pageSize) / 2;
        System.out.println("half = " + half);
        var first = bookRepository.findAll(PageRequest.of(0, pageSize, Sort.Direction.ASC, "id")).getContent();

        var secondPage = bookRepository.findAll(PageRequest.of(1, pageSize, Sort.Direction.DESC, "id")).getContent();
        assertEquals(3, secondPage.size());

        var thirdPage = bookRepository.findAll(PageRequest.of(0, pageSize, Sort.Direction.DESC, "id")).getContent();
        assertEquals(3, thirdPage.size());

        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Book::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
    }

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @NotNull
    private Book generateBooks() {
        var book = new Book();
        book.setRating(ThreadLocalRandom.current().nextInt(100));
        if (Math.random() > 0.5) {
            book.setCreatedAt(LocalDate.now().plusMonths(ThreadLocalRandom.current().nextInt(40)));
        } else {
            book.setCreatedAt(LocalDate.now().minusMonths(ThreadLocalRandom.current().nextInt(40)));
        }
        return book;
    }

}