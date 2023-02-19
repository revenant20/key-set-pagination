package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.dto.DataTuple;
import lombok.SneakyThrows;

import java.io.FileWriter;

public abstract class BaseUploader implements DbUploader {

    protected abstract String getPath();

    @SneakyThrows
    @Override
    public void upload(UploadCommand command) {
        try (var writer = new MyCsvWriter<DataTuple>(new FileWriter(getPath()))) {
            upload(command, writer);
        }
    }

    protected abstract void upload(UploadCommand command, MyCsvWriter<DataTuple> writer);
}
