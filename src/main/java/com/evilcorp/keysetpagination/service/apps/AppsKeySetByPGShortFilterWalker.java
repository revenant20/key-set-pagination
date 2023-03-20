package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.KeySetByPGShortFilterWalker;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class AppsKeySetByPGShortFilterWalker extends KeySetByPGShortFilterWalker<App> {
    @Getter
    private final String path = "./apps_count_by_PG_short_filter.csv";

    public AppsKeySetByPGShortFilterWalker(AppRepository repository) {
        super(repository);
    }
}
