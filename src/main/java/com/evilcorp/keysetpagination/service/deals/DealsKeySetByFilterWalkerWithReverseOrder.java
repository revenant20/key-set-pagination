package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.KeySetByFilterWalkerWithReverseOrder;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class DealsKeySetByFilterWalkerWithReverseOrder extends KeySetByFilterWalkerWithReverseOrder<Deal> {
    @Getter
    private final String path = "./deals_count_keyset_by_filter_with_revers_order.csv";

    @Getter
    private final String name = "keyset нормальный DESC (no index)";

    public DealsKeySetByFilterWalkerWithReverseOrder(DealRepository repository) {
        super(repository);
    }
}
