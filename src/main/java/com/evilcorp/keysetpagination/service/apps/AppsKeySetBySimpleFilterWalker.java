package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.KeySetBySimpleFilterWalker;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class AppsKeySetBySimpleFilterWalker extends KeySetBySimpleFilterWalker<App> {
    @Getter
    private final String path = "./apps_count_by_simple_filter.csv";

    @Getter
    private final String name = "keyset очевидный (index)";

    public AppsKeySetBySimpleFilterWalker(AppRepository repository) {
        super(repository);
    }
}
