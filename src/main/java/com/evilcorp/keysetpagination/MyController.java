package com.evilcorp.keysetpagination;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {


    @GetMapping
    String hello() {
        return "Hello world";
    }
}
