package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.SliceReversWalker;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class DealsSliceReversWalker extends SliceReversWalker<Deal> {

    @Getter
    private final String path = "./deals_count_slice_reverse.csv";

    @Getter
    private final String name = "slice ASC/DESC";

    public DealsSliceReversWalker(DealRepository repository) {
        super(repository);
    }
}
