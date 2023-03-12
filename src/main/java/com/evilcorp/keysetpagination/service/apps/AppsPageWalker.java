package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.PageWalker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
public class AppsPageWalker extends PageWalker<App> {

    @Getter
    private final String path = "./task.csv";

    public AppsPageWalker(AppRepository repository) {
        super(repository);
    }
}
