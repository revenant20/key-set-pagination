package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.SliceWalkerWithoutOrderBy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppsSliceWalkerWithoutOrderBy extends SliceWalkerWithoutOrderBy<App> {

    @Getter
    private final String path = "./apps_count_slice_WithoutOrderBy.csv";

    @Getter
    private final String name = "slice WithoutOrderBy";

    public AppsSliceWalkerWithoutOrderBy(AppRepository repository) {
        super(repository);
    }
}
