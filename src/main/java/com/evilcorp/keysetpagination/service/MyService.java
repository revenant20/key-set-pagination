package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.repository.App;
import com.evilcorp.keysetpagination.repository.AppRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MyService {

    private final AppRepository repository;

    public MyService(AppRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        Page<App> all = repository.findAll(PageRequest.of(0, 2));
        all.get().forEach(System.out::println);
    }
}
