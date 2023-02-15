package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.repository.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

import static java.time.LocalTime.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBLoader {

    private final static int ROW_NUMBER = 100_0;
    private final AppRepository appRepository;

    @PostConstruct
    void init() {
        long count = appRepository.count();
        var start = now();
        if (count < ROW_NUMBER) {
            log.info("Начало загрузки {} записей", ROW_NUMBER - count);
            var list = new ArrayList<App>();
            for (int i = 0; i < ROW_NUMBER; i++) {
                var app = new App();
                app.setId(UUID.randomUUID().toString());
                app.setText(UUID.randomUUID() + (Math.random() > 0.3 ? UUID.randomUUID().toString() : ""));
                app.setType(UUID.randomUUID() + (Math.random() > 0.6 ? UUID.randomUUID().toString() : ""));
                list.add(app);
                if (list.size() > 100) {
                    appRepository.saveAll(list);
                    list.clear();
                }
            }
        }
        var end = now();
        log.info("Загрузилось за {} секунд", start.until(end, ChronoUnit.SECONDS));
    }

}
