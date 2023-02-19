package com.evilcorp.keysetpagination.service.base;

import com.evilcorp.keysetpagination.dto.DataTuple;
import com.evilcorp.keysetpagination.repository.DataRepository;
import com.evilcorp.keysetpagination.repository.Ent;
import com.evilcorp.keysetpagination.service.MyCsvWriter;
import com.evilcorp.keysetpagination.service.UploadCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Slf4j
public abstract class SliceUploader<T extends Ent> extends BaseUploader {

    private final DataRepository<T, String> repository;

    public SliceUploader(DataRepository<T, String> repository) {
        this.repository = repository;
    }

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
