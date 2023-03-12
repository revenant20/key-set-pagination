package com.evilcorp.keysetpagination.service.base;

import com.evilcorp.keysetpagination.dto.PageLoadDuration;
import com.evilcorp.keysetpagination.service.Walker;
import com.evilcorp.keysetpagination.writers.SimpleCsvWriter;
import com.evilcorp.keysetpagination.dto.UploadCommand;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;

@Slf4j
public abstract class BaseWalker implements Walker {

    protected abstract String getPath();

    @SneakyThrows
    @Override
    public void upload(UploadCommand command) {
        log.info("{} начинает выгрузку", this.getClass().getSimpleName());
        try (var writer = new SimpleCsvWriter<PageLoadDuration>(new FileWriter(getPath()))) {
            upload(command, writer);
        }
    }

    protected abstract void upload(UploadCommand command, SimpleCsvWriter<PageLoadDuration> writer);
}
