package com.evilcorp.keysetpagination.service.base;

import com.evilcorp.keysetpagination.dto.PageLoadDuration;
import com.evilcorp.keysetpagination.dto.UploadCommand;
import com.evilcorp.keysetpagination.entity.Ent;
import com.evilcorp.keysetpagination.repository.DataRepository;
import com.evilcorp.keysetpagination.writers.SimpleCsvWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Slf4j
public abstract class SliceWalker<T extends Ent> extends BaseWalker {

    private final DataRepository<T, String> repository;

    public SliceWalker(DataRepository<T, String> repository) {
        this.repository = repository;
    }

    @Override
    protected void walk(UploadCommand command, SimpleCsvWriter<PageLoadDuration> writer) {
        var start = now();
        var firstPage = repository.findAllBy(PageRequest.of(0, command.getPageSize()));
        var end = now();
        var updates = new ArrayList<PageLoadDuration>();
        updateCsvDataset(updates, 0, start, end);
        int i = 1;
        while (firstPage.hasNext()) {
            start = now();
            var slice = repository.findAllBy(PageRequest.of(i, command.getPageSize(), Sort.Direction.ASC, "id"));
            end = now();
            updateCsvDataset(updates, i, start, end);
            if (i % 100 == 0) {
                log.info("SLICE выгрузил страницу номер {}", i);
            }
            i++;
            if (updates.size() >= 100) {
                writer.writeToCsv(updates);
                updates.clear();
            }
            if (slice.getContent().size() < 10) {
                break;
            }
        }
        writer.writeToCsv(updates);
        log.info("CSV CREATES DONE");
    }
}
