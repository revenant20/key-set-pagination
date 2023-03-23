package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.KeySetWalker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DealsKeySetWalker extends KeySetWalker<Deal> {

    @Getter
    private final String path = "./deals_count_keyset.csv";

    public DealsKeySetWalker(DealRepository repository) {
        super(repository);
    }
}
