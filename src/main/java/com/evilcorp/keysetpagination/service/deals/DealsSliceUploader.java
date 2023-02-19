package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.dto.DataTuple;
import com.evilcorp.keysetpagination.repository.DataRepository;
import com.evilcorp.keysetpagination.repository.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.BaseUploader;
import com.evilcorp.keysetpagination.service.MyCsvWriter;
import com.evilcorp.keysetpagination.service.UploadCommand;
import com.evilcorp.keysetpagination.service.base.SliceUploader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static java.time.LocalDateTime.now;

@Component
@Slf4j
public class DealsSliceUploader extends SliceUploader<Deal> {

    @Getter
    private final String path = "./deals_slice_count.csv";

    public DealsSliceUploader(DealRepository repository) {
        super(repository);
    }
}
