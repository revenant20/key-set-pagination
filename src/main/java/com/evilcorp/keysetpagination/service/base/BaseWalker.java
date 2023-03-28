package com.evilcorp.keysetpagination.service.base;

import com.evilcorp.keysetpagination.dto.PageLoadDuration;
import com.evilcorp.keysetpagination.dto.UploadCommand;
import com.evilcorp.keysetpagination.service.Walker;
import com.evilcorp.keysetpagination.writers.SimpleCsvWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Slf4j
public abstract class BaseWalker implements Walker {

    protected abstract String getPath();

    @SneakyThrows
    @Override
    public void walk(UploadCommand command) {
        log.info("{} начинает выгрузку", this.getClass().getSimpleName());
        try (var writer = new SimpleCsvWriter<PageLoadDuration>(new FileWriter(getPath()))) {
            walk(command, writer);
        }
    }

    protected abstract void walk(UploadCommand command, SimpleCsvWriter<PageLoadDuration> writer);

    protected final void updateCsvDataset(ArrayList<PageLoadDuration> tuples, int i, LocalDateTime start, LocalDateTime end) {
        var tuple = new PageLoadDuration();
        tuple.setPage(i);
        tuple.setTime(start.until(end, ChronoUnit.MILLIS));
        tuples.add(tuple);
    }
}
