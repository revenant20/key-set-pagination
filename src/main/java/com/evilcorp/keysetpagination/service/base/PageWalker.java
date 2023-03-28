package com.evilcorp.keysetpagination.service.base;

import com.evilcorp.keysetpagination.dto.PageLoadDuration;
import com.evilcorp.keysetpagination.dto.UploadCommand;
import com.evilcorp.keysetpagination.entity.Ent;
import com.evilcorp.keysetpagination.repository.DataRepository;
import com.evilcorp.keysetpagination.writers.SimpleCsvWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Slf4j
public abstract class PageWalker<T extends Ent> extends BaseWalker {

    private final DataRepository<T, String> repository;

    public PageWalker(DataRepository<T, String> repository) {
        this.repository = repository;
    }

    @Override
    protected void walk(UploadCommand command, SimpleCsvWriter<PageLoadDuration> writer) {
        var start = now();
        var all = repository.findAll(PageRequest.of(0, command.getPageSize()));
        var end = now();
        var tuples = new ArrayList<PageLoadDuration>();
        updateCsvDataset(tuples, 0, start, end);
        int totalPages = all.getTotalPages();
        for (int i = 1; i < totalPages; i++) {
            start = now();
            repository.findAll(PageRequest.of(i, command.getPageSize()));
            end = now();
            updateCsvDataset(tuples, i, start, end);
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
