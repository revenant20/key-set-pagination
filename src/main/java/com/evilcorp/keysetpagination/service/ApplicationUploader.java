package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.service.apps.AppsKeySetUploader;
import com.evilcorp.keysetpagination.service.apps.AppsPageUploader;
import com.evilcorp.keysetpagination.service.apps.AppsSliceUploader;
import com.evilcorp.keysetpagination.service.base.BaseUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static java.time.LocalTime.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationUploader {

    private final List<BaseUploader> baseUploaders;
    private final DBLoader loader;
    private final Set<Class<? extends BaseUploader>> classes = Set.of(
            AppsKeySetUploader.class,
            AppsPageUploader.class,
            AppsSliceUploader.class
    );

    @PostConstruct
    public void initPageable() {
        loader.init();
        UploadCommand command = UploadCommand.builder().pageSize(10).build();
        baseUploaders
                .stream()
                .filter(cl -> !classes.contains(cl.getClass()))
                .forEach(baseUploader -> {
                    var start = now();
                    baseUploader.upload(command);
                    var end = now();
                    log.info("Загрузка с помощью {} завершена за {} секунд", baseUploader.getClass(), start.until(end, ChronoUnit.SECONDS));
                });
    }
}
