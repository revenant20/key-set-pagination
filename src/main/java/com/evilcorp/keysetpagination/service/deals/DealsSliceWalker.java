package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.SliceWalker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DealsSliceWalker extends SliceWalker<Deal> {

    @Getter
    private final String path = "./deals_count_slice.csv";

    @Getter
    private final String name = "slice ASC";

    public DealsSliceWalker(DealRepository repository) {
        super(repository);
    }
}
