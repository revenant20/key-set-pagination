package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.repository.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.KeySetUploader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DealsKeySetUploader extends KeySetUploader<Deal> {

    @Getter
    private final String path = "./deals_count.csv";

    public DealsKeySetUploader(DealRepository repository) {
        super(repository);
    }
}
