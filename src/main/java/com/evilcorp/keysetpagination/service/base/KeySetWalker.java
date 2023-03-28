package com.evilcorp.keysetpagination.service.base;

import com.evilcorp.keysetpagination.dto.PageLoadDuration;
import com.evilcorp.keysetpagination.dto.UploadCommand;
import com.evilcorp.keysetpagination.entity.Ent;
import com.evilcorp.keysetpagination.repository.DataRepository;
import com.evilcorp.keysetpagination.writers.SimpleCsvWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Slf4j
public abstract class KeySetWalker<T extends Ent> extends BaseWalker {

    private final DataRepository<T, String> repository;

    public KeySetWalker(DataRepository<T, String> repository) {
        this.repository = repository;
    }

    @Override
    protected void walk(UploadCommand command, SimpleCsvWriter<PageLoadDuration> writer) {
        long count = repository.count();
        log.info("В таблице {} записей", count);
        int size = command.getPageSize() + 1;
        var start = now();
        var firstPage = repository.findFirst(Pageable.ofSize(size));
        var end = now();
        var tuples = new ArrayList<PageLoadDuration>();
        updateCsvDataset(tuples, 0, start, end);
        var lastId = firstPage.get(firstPage.size() - 1).getId();
        for (var i = 1; ; i++) {
            start = now();
            var rows = repository.findAllKeySet(lastId, Pageable.ofSize(size));
            end = now();
            lastId = rows.get(rows.size() - 1).getId();
            updateCsvDataset(tuples, i, start, end);
            if (tuples.size() >= 100) {
                writer.writeToCsv(tuples);
                tuples.clear();
            }
            if (rows.size() < size) {
                break;
            }
        }
        writer.writeToCsv(tuples);
        log.info("CSV CREATES DONE");
    }
}
