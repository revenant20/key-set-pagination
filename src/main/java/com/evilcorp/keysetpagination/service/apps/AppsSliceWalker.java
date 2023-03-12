package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.SliceWalker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
public class AppsSliceWalker extends SliceWalker<App> {

    @Getter
    private final String path = "./taskSlice.csv";

    public AppsSliceWalker(AppRepository repository) {
        super(repository);
    }
}
