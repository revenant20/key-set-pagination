package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.dto.DataTuple;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationUploader {

    private final AppRepository repository;

    @SneakyThrows
    public void initPageable() {
        var all = repository.findAll(PageRequest.of(0, 10));
        var tuples = new ArrayList<DataTuple>();
        int totalPages = all.getTotalPages();
        for (int i = 1; i < totalPages; i++) {
            var start = now();
            repository.findAll(PageRequest.of(i, 10));
            var end = now();
            var tuple = new DataTuple();
            tuple.setPage(i);
            tuple.setTime(start.until(end, ChronoUnit.MILLIS));
            tuples.add(tuple);
        }
        writeToCsv("./task.csv", tuples);
        log.info("CSV CREATES DONE");
    }

    @SneakyThrows
    //@PostConstruct
    public void initSlice() {
        var firstPage = repository.findAll(PageRequest.of(0, 10));
        var updates = new ArrayList<DataTuple>();
        int i = 1;
        while (firstPage.hasNext()) {
            var start = now();
            var slice = repository.findAll(PageRequest.of(i, 10));
            var end = now();
            var tuple = new DataTuple();
            tuple.setPage(i);
            tuple.setTime(start.until(end, ChronoUnit.MILLIS));
            updates.add(tuple);
            i++;
            if (slice.getContent().size() < 10) {
                break;
            }
        }

        writeToCsv("./taskSlice.csv", updates);
        log.info("CSV CREATES DONE");
    }

    @PostConstruct
    @SneakyThrows
    void initKeySet() {
        var firstPage = repository.findFirst();
        var tuples = new ArrayList<DataTuple>();
        var lastId = firstPage.get(firstPage.size() - 1).getId();
        for (var i = 0; ; i++) {
            var start = now();
            var rows = repository.findAllKeySet(lastId);
            var end = now();
            lastId = rows.get(rows.size() - 1).getId();
            var tuple = new DataTuple();
            tuple.setPage(i);
            tuple.setTime(start.until(end, ChronoUnit.MILLIS));
            tuples.add(tuple);
            if (rows.size() < 11) {
                break;
            }
        }
        writeToCsv("./taskKey.csv", tuples);
        log.info("CSV CREATES DONE");
    }

    private void writeToCsv(String fileName, ArrayList<DataTuple> updates) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try (Writer writer = new FileWriter(fileName)) {

            StatefulBeanToCsv<DataTuple> sbc = new StatefulBeanToCsvBuilder<DataTuple>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(';')

                    .build();

            sbc.write(updates);
        }
    }
}
