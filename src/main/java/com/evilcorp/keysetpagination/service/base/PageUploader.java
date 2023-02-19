package com.evilcorp.keysetpagination.service.base;

import com.evilcorp.keysetpagination.dto.DataTuple;
import com.evilcorp.keysetpagination.repository.DataRepository;
import com.evilcorp.keysetpagination.repository.Ent;
import com.evilcorp.keysetpagination.service.MyCsvWriter;
import com.evilcorp.keysetpagination.service.UploadCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Slf4j
public abstract class PageUploader<T extends Ent> extends BaseUploader {

    private final DataRepository<T, String> repository;

    public PageUploader(DataRepository<T, String> repository) {
        this.repository = repository;
    }

    @Override
    protected void upload(UploadCommand command, MyCsvWriter<DataTuple> writer) {
        var all = repository.findAll(PageRequest.of(0, command.getPageSize()));
        var tuples = new ArrayList<DataTuple>();
        int totalPages = all.getTotalPages();
        for (int i = 1; i < totalPages; i++) {
            var start = now();
            repository.findAll(PageRequest.of(i, command.getPageSize()));
            var end = now();
            var tuple = new DataTuple();
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
