package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.dto.DataTuple;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.DbUploader;
import com.evilcorp.keysetpagination.service.MyCsvWriter;
import com.evilcorp.keysetpagination.service.UploadCommand;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class PageUploader implements DbUploader {

    private final AppRepository repository;

    @SneakyThrows
    @Override
    public void upload(UploadCommand command) {
        try (var writer = new MyCsvWriter<DataTuple>(new FileWriter("./task.csv"))) {
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
            }
            writer.writeToCsv(tuples);
            log.info("CSV CREATES DONE");
        }

    }
}
