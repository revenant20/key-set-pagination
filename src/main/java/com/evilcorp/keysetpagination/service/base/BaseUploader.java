package com.evilcorp.keysetpagination.service.base;

import com.evilcorp.keysetpagination.dto.DataTuple;
import com.evilcorp.keysetpagination.service.DbUploader;
import com.evilcorp.keysetpagination.service.MyCsvWriter;
import com.evilcorp.keysetpagination.service.UploadCommand;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;

@Slf4j
public abstract class BaseUploader implements DbUploader {

    protected abstract String getPath();

    @SneakyThrows
    @Override
    public void upload(UploadCommand command) {
        log.info("{} начинает выгрузку", this.getClass().getSimpleName());
        try (var writer = new MyCsvWriter<DataTuple>(new FileWriter(getPath()))) {
            upload(command, writer);
        }
    }

    protected abstract void upload(UploadCommand command, MyCsvWriter<DataTuple> writer);
}
