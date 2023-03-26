package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.PageReversWalker;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class AppsPageReversWalker extends PageReversWalker<App> {

    @Getter
    private final String path = "./apps_count_page_reverse.csv";

    public AppsPageReversWalker(AppRepository repository) {
        super(repository);
    }
}
