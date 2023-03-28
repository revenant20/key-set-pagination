package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.KeySetBySimpleFilterWalkerWithReverseOrder;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class AppsKeySetBySimpleFilterWalkerWithReverseOrder extends KeySetBySimpleFilterWalkerWithReverseOrder<App> {
    @Getter
    private final String path = "./apps_count_by_simple_filter_with_revers_order.csv";

    public AppsKeySetBySimpleFilterWalkerWithReverseOrder(AppRepository repository) {
        super(repository);
    }
}
