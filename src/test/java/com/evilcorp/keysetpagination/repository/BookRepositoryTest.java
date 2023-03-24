package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.App;
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
    private AppRepository appRepository;

    @BeforeEach
    void setUp() {
        appRepository.deleteAll();
        appRepository.saveAll(
                Stream.generate(this::generateApps)
                        .limit(9).toList()
        );
    }

    @Test
    void testFilter() {
        var first = appRepository.findFirstByFilter(4);
        assertEquals(4, first.size());
        var app = first.get(3);

        var secondPage = appRepository.findAllByFilter(4, app.getCreatedAt(), app.getId());
        assertEquals(4, secondPage.size());
        var second = secondPage.get(3);

        var thirdPage = appRepository.findAllByFilter(4, second.getCreatedAt(), second.getId());
        assertEquals(3, thirdPage.size());
        var collect = Stream.of(first,
                        secondPage,
                        thirdPage)
                .flatMap(Collection::stream)
                .map(App::getId)
                .collect(Collectors.toSet());
        assertEquals(9, collect.size());
    }

    @AfterEach
    void tearDown() {
        appRepository.deleteAll();
    }

    @NotNull
    private App generateApps() {
        var app = new App();
        if (Math.random() > 0.5) {
            app.setCreatedAt(LocalDate.now().plusMonths(ThreadLocalRandom.current().nextInt(40)));
        } else {
            app.setCreatedAt(LocalDate.now().minusMonths(ThreadLocalRandom.current().nextInt(40)));
        }
        return app;
    }
}