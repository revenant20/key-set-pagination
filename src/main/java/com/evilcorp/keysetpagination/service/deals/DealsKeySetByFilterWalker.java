package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.KeySetByFilterWalker;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class DealsKeySetByFilterWalker extends KeySetByFilterWalker<Deal> {
    @Getter
    private final String path = "./deals_count_keyset_by_filter.csv";

    public DealsKeySetByFilterWalker(DealRepository repository) {
        super(repository);
    }
}
