package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.KeySetByShortFilterWalker;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class DealsKeySetByShortFilterWalker extends KeySetByShortFilterWalker<Deal> {

    @Getter
    private final String path = "./deals_count_keyset_by_short_filter.csv";

    public DealsKeySetByShortFilterWalker(DealRepository repository) {
        super(repository);
    }
}
