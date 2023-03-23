package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static java.time.LocalTime.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class DBLoader {

    @Value("${app.row.number}")
    private Integer appRowNumber;
    @Value("${deal.row.number}")
    private Integer dealRowNumber;
    private final AppRepository appRepository;
    private final DealRepository dealRepository;

    public void init() {
        loadApps();
        loadDeals();
    }

    private void loadDeals() {
        long count = dealRepository.count();
        var loading = 0;
        var start = now();
        if (count < dealRowNumber) {
            long arg = dealRowNumber - count;
            log.info("Начало загрузки {} записей", arg);
            var list = new ArrayList<Deal>();
            for (int i = 0; i < arg; i++) {
                var deal = new Deal();
                deal.setText(UUID.randomUUID() + (Math.random() > 0.3 ? UUID.randomUUID().toString() : ""));
                deal.setType(UUID.randomUUID() + (Math.random() > 0.6 ? UUID.randomUUID().toString() : ""));
                if (Math.random() > 0.5) {
                    deal.setDate(LocalDate.now().plusMonths(ThreadLocalRandom.current().nextInt(40)));
                } else {
                    deal.setDate(LocalDate.now().minusMonths(ThreadLocalRandom.current().nextInt(40)));
                }
                list.add(deal);
                if (list.size() > 1000 || i == arg - 1) {
                    dealRepository.saveAll(list);
                    loading += list.size();
                    if (i % 50 == 0) {
                        log.info("загрузил {}", (double) (loading * 100L) / (double) arg);
                    }
                    list.clear();
                }
            }
        }
        var end = now();
        log.info("Загрузилось за {} секунд", start.until(end, ChronoUnit.SECONDS));
        System.out.println("dealRepository.count() = " + dealRepository.count());
    }

    public void loadApps() {
        long count = appRepository.count();
        var loading = 0;
        if (count < appRowNumber) {
            long arg = appRowNumber - count;
            log.info("Начало загрузки {} записей", arg);
            var list = new ArrayList<App>();
            for (int i = 0; i < appRowNumber; i++) {
                var app = new App();
                app.setText(UUID.randomUUID() + (Math.random() > 0.3 ? UUID.randomUUID().toString() : ""));
                app.setType(UUID.randomUUID() + (Math.random() > 0.6 ? UUID.randomUUID().toString() : ""));
                if (Math.random() > 0.5) {
                    app.setDate(LocalDate.now().plusMonths(ThreadLocalRandom.current().nextInt(40)));
                } else {
                    app.setDate(LocalDate.now().minusMonths(ThreadLocalRandom.current().nextInt(40)));
                }
                list.add(app);
                if (list.size() > 1000 || i == arg - 1) {
                    appRepository.saveAll(list);
                    loading += list.size();
                    if (i % 50 == 0) {
                        log.info("загрузил {}", (double) (loading * 100L) / (double) arg);
                    }
                    list.clear();
                }
            }
        }
        System.out.println("appRepository.count() = " + appRepository.count());
    }

}
