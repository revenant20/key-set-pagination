package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.PageWalker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DealsPageWalker extends PageWalker<Deal> {

    @Getter
    private final String path = "./deals_count_page.csv";

    @Getter
    private final String name = "page ASC";

    public DealsPageWalker(DealRepository repository) {
        super(repository);
    }
}
