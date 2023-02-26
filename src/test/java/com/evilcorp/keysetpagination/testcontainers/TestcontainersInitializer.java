package com.evilcorp.keysetpagination.testcontainers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

import static com.evilcorp.keysetpagination.testcontainers.StaticPostgresContainer.StorageType.PERMANENT_STORAGE;
import static com.evilcorp.keysetpagination.testcontainers.StaticPostgresContainer.StorageType.TEMPORARY_STORAGE;

@Slf4j
public class TestcontainersInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        final var testcontainersEnabled = context.getEnvironment()
                .getProperty("keysetpagination.testcontainers.enabled");
        if (!"true".equals(testcontainersEnabled)) {
            var env = context.getEnvironment();
            env.getPropertySources().addFirst(new MapPropertySource(
                    "testcontainers",
                    // All of this is needed to completely disable
                    // anything, what could trigger errors, when running
                    // unit tests, working with databases
                    //
                    // Those tests should just be ignored if
                    // keysetpagination.testcontainers.enabled is false
                    // Use @EnabledIf(expression = "${keysetpagination.testcontainers.enabled}
                    //                loadContext = true)
                    // to disable tests working with testcontainers
                    Map.of(
                            // Running postgresql specific liquibase on
                            // h2 just triggers errors before Spring
                            // understands we should not run tests at all
                            "spring.liquibase.enabled", "false",
                            // if hibernate tries to validate ddl it discovers
                            // that there's no tables at all and triggres
                            // errors before Spring understands we should not
                            // run tests at all
                            "spring.jpa.hibernate.ddl-auto", "none",
                            // define keysetpagination.testcontainers.enabled if it is not
                            // defined so that
                            // @EnabledIf(expression = "${keysetpagination.testcontainers.enabled}
                            //                loadContext = true)
                            // would work
                            "keysetpagination.testcontainers.enabled", "false"
                    )
            ));
            return;
        }
        final var permanentStorageEnabled = context.getEnvironment()
                .getProperty("keysetpagination.testcontainers.permanentStorage.enabled");
        final var storageType = ("true".equals(permanentStorageEnabled))
                ? PERMANENT_STORAGE : TEMPORARY_STORAGE;
        final var container = StaticPostgresContainer.getContainer(storageType);
        final String jdbcUrl = container.getJdbcUrl();
        var env = context.getEnvironment();
        env.getPropertySources().addFirst(new MapPropertySource(
                "testcontainers",
                Map.of(
                        "spring.datasource.url", jdbcUrl,
                        "spring.datasource.username", container.getUsername(),
                        "spring.datasource.password", container.getPassword()
                )
        ));
    }

}
