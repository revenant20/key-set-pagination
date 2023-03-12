package com.evilcorp.keysetpagination.controller;

import com.evilcorp.keysetpagination.dto.books.GetBookRequest;
import com.evilcorp.keysetpagination.dto.books.ListOfBook;
import com.evilcorp.keysetpagination.dto.books.Sorting;
import com.evilcorp.keysetpagination.service.BookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public record BookController(BookService bookService) {

    @PostMapping
    public ListOfBook books(@RequestBody GetBookRequest request) {
        var sorting = request.sorting();
        if (sorting.properties() == null || sorting.properties().size() == 0) {
            sorting = new Sorting(sorting.fieldName(), request.sorting().direction(), List.of("id"));
        }
        return ListOfBook.builder()
                .books(bookService.getBooksByFilter(request.filter(), sorting))
                .sorting(sorting)
                .filter(request.filter())
                .build();
    }

}
