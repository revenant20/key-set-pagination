package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.dto.DataTuple;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.DbUploader;
import com.evilcorp.keysetpagination.service.MyCsvWriter;
import com.evilcorp.keysetpagination.service.UploadCommand;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeySetUploader implements DbUploader {

    private final AppRepository repository;

    @SneakyThrows
    @Override
    public void upload(UploadCommand command) {
        try (var writer = new MyCsvWriter<DataTuple>(new FileWriter("./taskKey.csv"))){
            long count = repository.count();
            log.info("В таблице {} записей", count);
            int size = command.getPageSize() + 1;
            var firstPage = repository.findFirst(size);
            var tuples = new ArrayList<DataTuple>();
            var lastId = firstPage.get(firstPage.size() - 1).getId();
            for (var i = 0; ; i++) {
                var start = now();
                var rows = repository.findAllKeySet(lastId, size);
                var end = now();
                lastId = rows.get(rows.size() - 1).getId();
                var tuple = new DataTuple();
                tuple.setPage(i);
                tuple.setTime(start.until(end, ChronoUnit.MILLIS));
                tuples.add(tuple);
                if (rows.size() < size) {
                    break;
                }
            }
            writer.writeToCsv(tuples);
            log.info("CSV CREATES DONE");
        }
    }
}
