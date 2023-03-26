package com.evilcorp.keysetpagination.service.base;

import com.evilcorp.keysetpagination.dto.PageLoadDuration;
import com.evilcorp.keysetpagination.dto.UploadCommand;
import com.evilcorp.keysetpagination.entity.Ent;
import com.evilcorp.keysetpagination.repository.DataRepository;
import com.evilcorp.keysetpagination.writers.SimpleCsvWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Slf4j
public abstract class PageReversWalker<T extends Ent> extends BaseWalker {

    private final DataRepository<T, String> repository;

    public PageReversWalker(DataRepository<T, String> repository) {
        this.repository = repository;
    }

    @Override
    protected void walk(UploadCommand command, SimpleCsvWriter<PageLoadDuration> writer) {
        var start = now();
        var all = repository.findAll(PageRequest.of(0, command.getPageSize()));
        var end = now();
        var tuples = new ArrayList<PageLoadDuration>();
        var tuple = new PageLoadDuration();
        tuple.setPage(0);
        tuple.setTime(start.until(end, ChronoUnit.MILLIS));
        tuples.add(tuple);
        int totalPages = all.getTotalPages();
        int halfPages = totalPages / 2;
        for (int i = 1; i < halfPages; i++) {
            start = now();
            repository.findAll(PageRequest.of(i, command.getPageSize()));
            end = now();
            tuple = new PageLoadDuration();
            tuple.setPage(i);
            tuple.setTime(start.until(end, ChronoUnit.MILLIS));
            tuples.add(tuple);
            if (i % 100 == 0) {
                log.info("выгрузил страницу номер {} из {}", i, totalPages);
            }
            if (tuples.size() >= 100) {
                writer.writeToCsv(tuples);
                tuples.clear();
            }
        }
        for (int i = halfPages; i >= 0; i--) {
            start = now();
            repository.findAll(PageRequest.of(i, command.getPageSize()));
            end = now();
            tuple = new PageLoadDuration();
            tuple.setPage(i);
            tuple.setTime(start.until(end, ChronoUnit.MILLIS));
            tuples.add(tuple);
            if (i % 100 == 0) {
                log.info("выгрузил страницу номер {} из {}", i, totalPages);
            }
            if (tuples.size() >= 100) {
                writer.writeToCsv(tuples);
                tuples.clear();
            }
        }
        writer.writeToCsv(tuples);
        log.info("CSV CREATES DONE");
    }
}
