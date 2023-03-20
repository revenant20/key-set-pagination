package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.KeySetByShortFilterWalker;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class AppsKeySetByShortFilterWalker extends KeySetByShortFilterWalker<App> {
    @Getter
    private final String path = "./apps_count_by_short_filter.csv";

    public AppsKeySetByShortFilterWalker(AppRepository repository) {
        super(repository);
    }
}
