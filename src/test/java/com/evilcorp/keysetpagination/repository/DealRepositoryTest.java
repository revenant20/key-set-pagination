package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.entity.Deal;
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
class DealRepositoryTest {

    @Autowired
    private DealRepository dealRepository;

    @BeforeEach
    void setUp() {
        dealRepository.deleteAll();
        dealRepository.saveAll(
                Stream.generate(this::generateApps)
                        .limit(9).toList()
        );
    }

    @Test
    void testFilter() {
        var first = dealRepository.findFirstByFilter(4);
        assertEquals(4, first.size());
        var app = first.get(3);

        var secondPage = dealRepository.findAllByFilter(4, app.getCreatedAt(), app.getId());
        assertEquals(4, secondPage.size());
        var second = secondPage.get(3);

        var thirdPage = dealRepository.findAllByFilter(4, second.getCreatedAt(), second.getId());
        assertEquals(3, thirdPage.size());
        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Deal::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
    }
    @Test
    void testPgFilter() {
        var first = dealRepository.findFirstByFilter(4);
        assertEquals(4, first.size());
        var app = first.get(3);

        var secondPage = dealRepository.findAllByPGShortFilter(4, app.getCreatedAt(), app.getId());
        assertEquals(4, secondPage.size());
        var second = secondPage.get(3);

        var thirdPage = dealRepository.findAllByPGShortFilter(4, second.getCreatedAt(), second.getId());
        assertEquals(3, thirdPage.size());
        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Deal::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
    }

    @Test
    void testPgJpqlFilter() {
        var first = dealRepository.findFirstByFilter(4);
        assertEquals(4, first.size());
        var app = first.get(3);

        var secondPage = dealRepository.findAllByPgShortFilterJpql(app.getCreatedAt(), app.getId(), Pageable.ofSize(4));
        assertEquals(4, secondPage.size());
        var second = secondPage.get(3);

        var thirdPage = dealRepository.findAllByPgShortFilterJpql(second.getCreatedAt(), second.getId(), Pageable.ofSize(4));
        assertEquals(3, thirdPage.size());
        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Deal::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
    }

    @Test
    void testPageFilter() {
        var first = dealRepository.findAll(PageRequest.of(0, 3)).getContent();
        assertEquals(3, first.size());

        var secondPage = dealRepository.findAll(PageRequest.of(1, 3)).getContent();
        assertEquals(3, secondPage.size());

        var thirdPage = dealRepository.findAll(PageRequest.of(2, 3)).getContent();
        assertEquals(3, thirdPage.size());
        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Deal::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
        var nonExistPage = dealRepository.findAll(PageRequest.of(3, 3)).getContent();
        assertEquals(0, nonExistPage.size());
    }

    @Test
    void testSliceFilter() {
        var first = dealRepository.findAllBy(PageRequest.of(0, 3)).getContent();
        assertEquals(3, first.size());

        var secondPage = dealRepository.findAllBy(PageRequest.of(1, 3)).getContent();
        assertEquals(3, secondPage.size());

        var thirdPage = dealRepository.findAllBy(PageRequest.of(2, 3)).getContent();
        assertEquals(3, thirdPage.size());
        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(Deal::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
        var nonExistPage = dealRepository.findAllBy(PageRequest.of(3, 3)).getContent();
        assertEquals(0, nonExistPage.size());
    }

    @AfterEach
    void tearDown() {
        dealRepository.deleteAll();
    }

    @NotNull
    private Deal generateApps() {
        var app = new Deal();
        if (Math.random() > 0.5) {
            app.setCreatedAt(LocalDate.now().plusMonths(ThreadLocalRandom.current().nextInt(40)));
        } else {
            app.setCreatedAt(LocalDate.now().minusMonths(ThreadLocalRandom.current().nextInt(40)));
        }
        return app;
    }
}