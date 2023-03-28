package com.evilcorp.keysetpagination.service.base;

import com.evilcorp.keysetpagination.dto.PageLoadDuration;
import com.evilcorp.keysetpagination.dto.UploadCommand;
import com.evilcorp.keysetpagination.entity.Ent;
import com.evilcorp.keysetpagination.repository.DataRepository;
import com.evilcorp.keysetpagination.writers.SimpleCsvWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Slf4j
public abstract class SliceReversWalker<T extends Ent> extends BaseWalker {

    private final DataRepository<T, String> repository;

    public SliceReversWalker(DataRepository<T, String> repository) {
        this.repository = repository;
    }

    @Override
    protected void walk(UploadCommand command, SimpleCsvWriter<PageLoadDuration> writer) {
        LocalDateTime start;
        LocalDateTime end;
        var tuples = new ArrayList<PageLoadDuration>();
        int count = (int) repository.count();
        int moduloOfTotalPages = count % command.getPageSize();
        final int totalPages;
        if (moduloOfTotalPages == 0) {
            totalPages = count / command.getPageSize();
        } else {
            totalPages = ((count - moduloOfTotalPages) / command.getPageSize()) + 1;
        }
        int moduloOfHalfPages = totalPages % 2;
        final int halfPages;
        if (moduloOfHalfPages == 0) {
            halfPages = totalPages/2;
        } else {
            halfPages = ((totalPages - moduloOfTotalPages) / 2) + 1;
        }
        int page = 0;
        for (var i = 0; i < halfPages; i++) {
            start = now();
            repository.findAllBy(PageRequest.of(i, command.getPageSize(), Sort.Direction.ASC, "id"));
            end = now();
            updateCsvDataset(tuples, page, start, end);
            page++;
            if (i % 100 == 0) {
                log.info("выгрузил страницу номер {} из {}", i, totalPages);
            }
            if (tuples.size() >= 100) {
                writer.writeToCsv(tuples);
                tuples.clear();
            }
        }
        for (var i = totalPages - halfPages - 1; i >= 0; i--) {
            start = now();
            repository.findAllBy(PageRequest.of(i, command.getPageSize(), Sort.Direction.DESC, "id"));
            end = now();
            updateCsvDataset(tuples, page, start, end);
            page++;
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
