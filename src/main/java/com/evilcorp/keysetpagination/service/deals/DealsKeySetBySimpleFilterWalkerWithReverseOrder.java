package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.KeySetBySimpleFilterWalkerWithReverseOrder;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class DealsKeySetBySimpleFilterWalkerWithReverseOrder extends KeySetBySimpleFilterWalkerWithReverseOrder<Deal> {
    @Getter
    private final String path = "./deals_count_keyset_by_simple_filter_with_reverse_order.csv";

    public DealsKeySetBySimpleFilterWalkerWithReverseOrder(DealRepository repository) {
        super(repository);
    }
}
