package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.dto.DataTuple;
import com.evilcorp.keysetpagination.repository.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.repository.DataRepository;
import com.evilcorp.keysetpagination.service.DbUploader;
import com.evilcorp.keysetpagination.service.MyCsvWriter;
import com.evilcorp.keysetpagination.service.UploadCommand;
import com.evilcorp.keysetpagination.service.base.PageUploader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Slf4j
@Component
public class AppsPageUploader extends PageUploader<App> {

    @Getter
    private final String path = "./task.csv";

    public AppsPageUploader(AppRepository repository) {
        super(repository);
    }
}
