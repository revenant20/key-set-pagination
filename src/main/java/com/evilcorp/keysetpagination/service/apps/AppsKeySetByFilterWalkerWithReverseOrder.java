package com.evilcorp.keysetpagination.service.apps;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.KeySetByFilterWalkerWithReverseOrder;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class AppsKeySetByFilterWalkerWithReverseOrder extends KeySetByFilterWalkerWithReverseOrder<App> {
    @Getter
    private final String path = "./apps_count_by_filter_with_revers_order.csv";

    @Getter
    private final String name = "keyset нормальный DESC (index)";

    public AppsKeySetByFilterWalkerWithReverseOrder(AppRepository repository) {
        super(repository);
    }
}
