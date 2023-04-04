package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.SliceReversWalker;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class AppsSliceReversWalker extends SliceReversWalker<App> {

    @Getter
    private final String path = "./apps_count_slice_reverse.csv";
    @Getter
    private final String name = "slice ASC/DESC";

    public AppsSliceReversWalker(AppRepository repository) {
        super(repository);
    }
}
