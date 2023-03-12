package com.evilcorp.keysetpagination.controller;

import com.evilcorp.keysetpagination.dto.books.Book;
import com.evilcorp.keysetpagination.dto.books.GetBookRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

    @PostMapping
    public List<Book> books(@RequestBody GetBookRequest request) {
        return List.of(Book.builder().id("1").description("asd").rating(1).build());
    }

}
