package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.KeySetByFilterWalker;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class AppsKeySetByFilterWalker extends KeySetByFilterWalker<App> {
    @Getter
    private final String path = "./apps_count_by_filter.csv";

    public AppsKeySetByFilterWalker(AppRepository repository) {
        super(repository);
    }
}
