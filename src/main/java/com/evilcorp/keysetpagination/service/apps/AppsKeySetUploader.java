package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.repository.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.repository.DataRepository;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.KeySetUploader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppsKeySetUploader extends KeySetUploader<App> {

    @Getter
    private final String path = "./taskKey.csv";

    public AppsKeySetUploader(AppRepository repository) {
        super(repository);
    }
}
