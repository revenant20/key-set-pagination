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
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @NotNull
    private Book generateBooks() {
        var book = new Book();
        book.setRating(ThreadLocalRandom.current().nextInt(100));
        return book;
    }

}