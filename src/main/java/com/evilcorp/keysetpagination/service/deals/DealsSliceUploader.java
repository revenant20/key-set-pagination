package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.dto.DataTuple;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.BaseUploader;
import com.evilcorp.keysetpagination.service.MyCsvWriter;
import com.evilcorp.keysetpagination.service.UploadCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Component
@Slf4j
@RequiredArgsConstructor
public class DealsSliceUploader extends BaseUploader {

    private final DealRepository repository;

    @Getter
    private final String path = "./deals_slice_count.csv";

    @Override
    protected void upload(UploadCommand command, MyCsvWriter<DataTuple> writer) {
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
