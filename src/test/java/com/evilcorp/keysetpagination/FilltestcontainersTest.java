package com.evilcorp.keysetpagination;

import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.DBLoader;
import com.evilcorp.keysetpagination.service.UploadCommand;
import com.evilcorp.keysetpagination.service.deals.DealsKeySetUploader;
import com.evilcorp.keysetpagination.service.deals.DealsPageUploader;
import com.evilcorp.keysetpagination.service.deals.DealsSliceUploader;
import com.evilcorp.keysetpagination.testcontainers.TestcontainersInitializer;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
@EnabledIf(expression = "${keysetpagination.testcontainers.enabled}", loadContext = true)
@Rollback(value = false)
@Transactional(propagation = Propagation.NEVER)
public class FilltestcontainersTest {
    @Autowired
    DBLoader loader;

    @Autowired
    DealRepository repository;

    @Autowired
    DealsKeySetUploader keysetUploader;

    @Autowired
    DealsPageUploader pageUploader;

    @Autowired
    DealsSliceUploader sliceUploader;

    @Test
    public void fillWithData() {
        assertThat(loader).isNotNull();
        loader.init();
        assertThat(repository.count()).isGreaterThan(10);
        final UploadCommand cmd = UploadCommand.builder().pageSize(1000).build();
        keysetUploader.upload(cmd);
        pageUploader.upload(cmd);
        sliceUploader.upload(cmd);
    }

}
