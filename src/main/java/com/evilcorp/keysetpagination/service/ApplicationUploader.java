package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.dto.UploadCommand;
import com.evilcorp.keysetpagination.service.apps.AppsKeySetWalker;
import com.evilcorp.keysetpagination.service.apps.AppsPageWalker;
import com.evilcorp.keysetpagination.service.apps.AppsSliceWalker;
import com.evilcorp.keysetpagination.service.base.BaseWalker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static java.time.LocalTime.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationUploader {

    private final List<BaseWalker> baseWalkers;
    private final DBLoader loader;
    private final Set<Class<? extends BaseWalker>> classes = Set.of(
            AppsKeySetWalker.class,
            AppsPageWalker.class,
            AppsSliceWalker.class
    );

//    @PostConstruct
    public void initPageable() {
        loader.init();
        UploadCommand command = UploadCommand.builder().pageSize(10).build();
        baseWalkers
                .stream()
                .filter(cl -> !classes.contains(cl.getClass()))
                .forEach(baseWalker -> {
                    var start = now();
                    baseWalker.walk(command);
                    var end = now();
                    log.info("Загрузка с помощью {} завершена за {} секунд", baseWalker.getClass(), start.until(end, ChronoUnit.SECONDS));
                });
    }
}
