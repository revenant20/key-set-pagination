package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.dto.PageLoadDuration;
import com.evilcorp.keysetpagination.dto.UploadCommand;
import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.testcontainers.TestcontainersInitializer;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
@EnabledIf(expression = "${keysetpagination.testcontainers.enabled}", loadContext = true)
@Rollback(value = false)
@Transactional(propagation = Propagation.NEVER)
@Slf4j
class AppsPageReversWalkerTest {

    @Autowired
    AppRepository appRepository;

    @Autowired
    AppsPageReversWalker appsPageReversWalker;

    @BeforeEach
    void setUp() {
        appRepository.deleteAll();
        appRepository.saveAll(
                Stream.generate(this::generateApps)
                        .limit(9).toList()
        );
    }

    @Test
    @SneakyThrows
    void test() {
        appsPageReversWalker.walk(UploadCommand.builder().pageSize(3).build());
        List<PageLoadDuration> beans = beanBuilderExample(new File("apps_count_page_reverse.csv").toPath(), PageLoadDuration.class);
        assertEquals(3, beans.size());
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

    @SneakyThrows
    public <CsvBean> List<CsvBean> beanBuilderExample(Path path, Class clazz) {

        try (Reader reader = Files.newBufferedReader(path)) {
            CsvToBean<CsvBean> cb = new CsvToBeanBuilder<CsvBean>(reader)
                    .withType(clazz)
                    .build();

            return cb.parse();
        }
    }
}