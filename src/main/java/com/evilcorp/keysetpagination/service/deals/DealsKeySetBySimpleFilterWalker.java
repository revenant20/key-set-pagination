package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.KeySetBySimpleFilterWalker;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class DealsKeySetBySimpleFilterWalker extends KeySetBySimpleFilterWalker<Deal> {
    @Getter
    private final String path = "./deals_count_keyset_by_simple_filter.csv";

    public DealsKeySetBySimpleFilterWalker(DealRepository repository) {
        super(repository);
    }
}
