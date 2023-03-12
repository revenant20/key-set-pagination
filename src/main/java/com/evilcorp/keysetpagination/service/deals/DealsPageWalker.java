package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.repository.DealRepository;
import com.evilcorp.keysetpagination.service.base.PageWalker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.time.LocalDateTime.now;

@Component
@Slf4j
public class DealsPageWalker extends PageWalker<Deal> {

    @Getter
    private final String path = "./deals_page_count.csv";

    public DealsPageWalker(DealRepository repository) {
        super(repository);
    }
}
