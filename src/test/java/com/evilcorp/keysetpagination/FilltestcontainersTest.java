package com.evilcorp.keysetpagination;

import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.DBLoader;
import com.evilcorp.keysetpagination.dto.UploadCommand;
import com.evilcorp.keysetpagination.service.Walker;
import com.evilcorp.keysetpagination.service.apps.AppsKeySetByFilterWalker;
import com.evilcorp.keysetpagination.service.deals.DealsKeySetWalker;
import com.evilcorp.keysetpagination.service.deals.DealsPageWalker;
import com.evilcorp.keysetpagination.service.deals.DealsSliceWalker;
import com.evilcorp.keysetpagination.testcontainers.TestcontainersInitializer;
import lombok.extern.slf4j.Slf4j;
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

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Predicate;

import static java.time.LocalTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
@EnabledIf(expression = "${keysetpagination.testcontainers.enabled}", loadContext = true)
@Rollback(value = false)
@Transactional(propagation = Propagation.NEVER)
@Slf4j
public class FilltestcontainersTest {
    @Autowired
    DBLoader loader;

    @Autowired
    DealRepository repository;

    @Autowired
    DealsKeySetWalker keysetUploader;

    @Autowired
    DealsPageWalker pageUploader;

    @Autowired
    DealsSliceWalker sliceUploader;

    @Autowired
    List<Walker> walkers;

    @Autowired
    AppsKeySetByFilterWalker appsKeySetByFilterWalker;

    @Test
    public void fillWithData() {
        assertThat(loader).isNotNull();
        loader.init();
        assertThat(repository.count()).isGreaterThan(10);
        final UploadCommand cmd = UploadCommand.builder().pageSize(500).build();
        //keysetUploader.upload(cmd);
        //pageUploader.upload(cmd);
        //sliceUploader.upload(cmd);
        walkers.stream()
                .filter(Predicate.not(cl -> cl.getClass().equals(DealsPageWalker.class)))
                .filter(Predicate.not(cl -> cl.getClass().equals(DealsSliceWalker.class)))
                .forEach(walker -> {
                    var start = now();
                    walker.walk(cmd);
                    var end = now();
                    log.info("{} загрузил за {} секунд", walker.getClass().getSimpleName(), start.until(end, ChronoUnit.SECONDS));
                });
        //appsKeySetByFilterWalker.upload(cmd);
    }

}
