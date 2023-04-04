package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.SliceWalker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppsSliceWalker extends SliceWalker<App> {

    @Getter
    private final String path = "./apps_count_slice.csv";

    @Getter
    private final String name = "slice ASC";

    public AppsSliceWalker(AppRepository repository) {
        super(repository);
    }
}
