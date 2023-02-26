package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.repository.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.repository.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
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

    private final static int ROW_NUMBER = 10_000;
    private final static int DEAL_ROW_NUMBER = 1_000_000;
    private final AppRepository appRepository;
    private final DealRepository dealRepository;

    public void init() {
//        loadApps();
        loadDeals();
    }

    private void loadDeals() {
        long count = dealRepository.count();
        var loading = 0;
        var start = now();
        if (count < DEAL_ROW_NUMBER) {
            long arg = DEAL_ROW_NUMBER - count;
            log.info("Начало загрузки {} записей", arg);
            var list = new ArrayList<Deal>();
            for (int i = 0; i < arg; i++) {
                var deal = new Deal();
                deal.setId(UUID.randomUUID().toString());
                deal.setText(UUID.randomUUID() + (Math.random() > 0.3 ? UUID.randomUUID().toString() : ""));
                deal.setType(UUID.randomUUID() + (Math.random() > 0.6 ? UUID.randomUUID().toString() : ""));
                list.add(deal);
                if (list.size() > 2000 || i == arg - 1) {
                    dealRepository.saveAll(list);
                    loading += list.size();
                    if (i % 50 == 0) {
                        log.info("загрузил {}", (double)(loading * 100L) / (double) arg);
                    }
                    list.clear();
                }
            }
        }
        var end = now();
        log.info("Загрузилось за {} секунд", start.until(end, ChronoUnit.SECONDS));
    }

    private void loadApps() {
        long count = appRepository.count();
        var loading = 0;
        var start = now();
        if (count < ROW_NUMBER) {
            long arg = ROW_NUMBER - count;
            log.info("Начало загрузки {} записей", arg);
            var list = new ArrayList<App>();
            for (int i = 0; i < ROW_NUMBER; i++) {
                var app = new App();
                app.setId(UUID.randomUUID().toString());
                app.setText(UUID.randomUUID() + (Math.random() > 0.3 ? UUID.randomUUID().toString() : ""));
                app.setType(UUID.randomUUID() + (Math.random() > 0.6 ? UUID.randomUUID().toString() : ""));
                list.add(app);
                if (list.size() > 200) {
                    appRepository.saveAll(list);
                    loading += list.size();
                    if (i % 50 == 0) {
                        log.info("загрузил {}", (double)(loading * 100L) / (double) arg);
                    }
                    list.clear();
                }
            }
        }
        var end = now();
        log.info("Загрузилось за {} секунд", start.until(end, ChronoUnit.SECONDS));
    }

}
