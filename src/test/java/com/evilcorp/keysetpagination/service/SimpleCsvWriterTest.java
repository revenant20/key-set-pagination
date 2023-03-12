package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.dto.PageLoadDuration;
import com.evilcorp.keysetpagination.writers.SimpleCsvWriter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleCsvWriterTest {

    @SneakyThrows
    @Test
    void test() {
        try (var writer = new SimpleCsvWriter<PageLoadDuration>(new FileWriter("test.csv"))){
            writer.writeToCsv(List.of(
                    PageLoadDuration.builder().build(),
                    PageLoadDuration.builder().build()
            ));
            writer.writeToCsv(List.of(
                    PageLoadDuration.builder().build(),
                    PageLoadDuration.builder().time(4).build(),
                    PageLoadDuration.builder().time(4).build()
            ));
        }
        long count = Files.lines(Path.of("./test.csv"))
                .count();
        assertEquals(6, count);
    }
}