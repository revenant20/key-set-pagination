package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.dto.DataTuple;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.BaseUploader;
import com.evilcorp.keysetpagination.service.MyCsvWriter;
import com.evilcorp.keysetpagination.service.UploadCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Component
@Slf4j
@RequiredArgsConstructor
public class DealsKeySetUploader extends BaseUploader {

    private final DealRepository repository;

    @Getter
    private final String path = "./deals_count.csv";

    @Override
    protected void upload(UploadCommand command, MyCsvWriter<DataTuple> writer) {
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
