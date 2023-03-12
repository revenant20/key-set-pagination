package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.KeySetWalker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppsKeySetWalker extends KeySetWalker<App> {

    @Getter
    private final String path = "./taskKey.csv";

    public AppsKeySetWalker(AppRepository repository) {
        super(repository);
    }
}
