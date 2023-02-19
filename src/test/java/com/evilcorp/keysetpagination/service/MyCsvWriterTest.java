package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.dto.DataTuple;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyCsvWriterTest {

    @SneakyThrows
    @Test
    void test() {
        try (var writer = new MyCsvWriter<DataTuple>(new FileWriter("test.csv"))){
            writer.writeToCsv(List.of(
                    DataTuple.builder().build(),
                    DataTuple.builder().build()
            ));
            writer.writeToCsv(List.of(
                    DataTuple.builder().build(),
                    DataTuple.builder().time(4).build(),
                    DataTuple.builder().time(4).build()
            ));
        }
        long count = Files.lines(Path.of("./test.csv"))
                .count();
        assertEquals(6, count);
    }
}