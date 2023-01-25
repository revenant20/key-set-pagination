package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.repository.AppRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MyService {

    private final AppRepository repository;

    public MyService(AppRepository repository) {
        this.repository = repository;
    }

}
