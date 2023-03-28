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
public abstract class KeySetByFilterWalkerWithReverseOrder<T extends Ent> extends BaseWalker {

    private final DataRepository<T, String> repository;

    public KeySetByFilterWalkerWithReverseOrder(DataRepository<T, String> repository) {
        this.repository = repository;
    }

    @Override
    protected void walk(UploadCommand command, SimpleCsvWriter<PageLoadDuration> writer) {
        long count = repository.count();
        log.info("В таблице {} записей", count);
        int size = command.getPageSize() + 1;
        var start = now();
        var firstPage = repository.findFirstByFilterWithReversFilter(Pageable.ofSize(size));
        var end = now();
        var tuples = new ArrayList<PageLoadDuration>();
        updateCsvDataset(tuples, 0, start, end);
        var ent = firstPage.get(firstPage.size() - 1);
        var lastId = ent.getId();
        var lastDate = ent.getCreatedAt();
        for (var i = 1; ; i++) {
            start = now();
            var rows = repository.findAllByFilterWithReverseOrder(Pageable.ofSize(size), lastDate, lastId);
            end = now();
            lastId = rows.get(rows.size() - 1).getId();
            lastDate = rows.get(rows.size() - 1).getCreatedAt();
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
