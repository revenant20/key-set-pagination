package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.KeySetByPGShortFilterWalker;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class DealsKeySetByPGShortFilterWalker extends KeySetByPGShortFilterWalker<Deal> {
    @Getter
    private final String path = "./deals_count_keyset_by_PG_short_filter.csv";

    public DealsKeySetByPGShortFilterWalker(DealRepository repository) {
        super(repository);
    }
}
