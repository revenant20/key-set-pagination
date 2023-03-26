package com.evilcorp.keysetpagination.service.deals;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import com.evilcorp.keysetpagination.service.base.PageReversWalker;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class DealsPageReversWalker extends PageReversWalker<App> {

    @Getter
    private final String path = "./deals_count_page_reverse.csv";

    public DealsPageReversWalker(AppRepository repository) {
        super(repository);
    }
}
