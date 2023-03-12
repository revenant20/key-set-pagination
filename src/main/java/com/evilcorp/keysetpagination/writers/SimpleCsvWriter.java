package com.evilcorp.keysetpagination.writers;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.SneakyThrows;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SimpleCsvWriter<T> implements Closeable {

    private final StatefulBeanToCsv<T> statefulBeanToCsv;

    private final FileWriter writer;

    public SimpleCsvWriter(FileWriter writer) {
        this.writer = writer;
        this.statefulBeanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(';')
                .build();
    }

    @SneakyThrows
    public void writeToCsv(List<T> updates) {
        statefulBeanToCsv.write(updates);
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
