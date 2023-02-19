package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.service.apps.KeySetUploader;
import com.evilcorp.keysetpagination.service.apps.PageUploader;
import com.evilcorp.keysetpagination.service.apps.SliceUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalTime.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationUploader {

    private final KeySetUploader keySetUploader;
    private final PageUploader pageUploader;
    private final SliceUploader sliceUploader;
    private final List<BaseUploader> baseUploaders;

    @PostConstruct
    public void initPageable() {
        UploadCommand command = UploadCommand.builder().pageSize(10).build();
        //keySetUploader.upload(build);
        //pageUploader.upload(build);
        //sliceUploader.upload(build);
        baseUploaders.forEach(baseUploader -> {
            var start = now();
            baseUploader.upload(command);
            var end = now();
            log.info("Загрузка с помощью {} завершена за {} секунд", baseUploader.getClass(), start.until(end, ChronoUnit.SECONDS));
        });
    }
}
