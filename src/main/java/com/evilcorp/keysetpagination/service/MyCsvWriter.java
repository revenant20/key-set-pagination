package com.evilcorp.keysetpagination.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.SneakyThrows;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MyCsvWriter<T> implements Closeable {

    private final StatefulBeanToCsv<T> sbc;

    private final FileWriter writer;

    public MyCsvWriter(FileWriter writer) {
        this.writer = writer;
        this.sbc = new StatefulBeanToCsvBuilder<T>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(';')
                .build();
    }

    @SneakyThrows
    public void writeToCsv(List<T> updates) {
        sbc.write(updates);
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
