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
public class SliceUploader implements DbUploader {

    private final AppRepository repository;

    @SneakyThrows
    @Override
    public void upload(UploadCommand command) {
        try (var writer = new MyCsvWriter<DataTuple>(new FileWriter("./taskSlice.csv"))) {
            var firstPage = repository.findAll(PageRequest.of(0, command.getPageSize()));
            var updates = new ArrayList<DataTuple>();
            int totalPages = firstPage.getTotalPages();
            int i = 1;
            while (firstPage.hasNext()) {
                var start = now();
                var slice = repository.findAll(PageRequest.of(i, command.getPageSize()));
                var end = now();
                var tuple = new DataTuple();
                tuple.setPage(i);
                tuple.setTime(start.until(end, ChronoUnit.MILLIS));
                updates.add(tuple);
                if (i % 100 == 0) {
                    log.info("SLICE выгрузил страницу номер {} из {}", i, totalPages);
                }
                i++;
                if (slice.getContent().size() < 10) {
                    break;
                }
            }
            writer.writeToCsv(updates);
            log.info("CSV CREATES DONE");
        }
    }
}
