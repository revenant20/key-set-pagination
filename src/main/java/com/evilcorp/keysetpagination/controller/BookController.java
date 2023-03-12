package com.evilcorp.keysetpagination.controller;

import com.evilcorp.keysetpagination.dto.books.BookFilter;
import com.evilcorp.keysetpagination.dto.books.GetBookRequest;
import com.evilcorp.keysetpagination.dto.books.ListOfBook;
import com.evilcorp.keysetpagination.dto.books.Sorting;
import com.evilcorp.keysetpagination.service.BookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public record BookController(BookService bookService) {

    @PostMapping
    public ListOfBook books(@RequestBody GetBookRequest request) {
        Sorting sorting;
        final BookFilter filter;
        if (StringUtils.isNotBlank(request.token())) {
            sorting = request.sorting(); // позже буду брать из токена
            filter = request.filter();
        } else {
            sorting = request.sorting();
            filter = request.filter();
        }
        if (sorting.properties() == null || sorting.properties().size() == 0) {
            sorting = new Sorting(sorting.fieldName(), sorting.direction(), List.of("id"));
        }
        return ListOfBook.builder()
                .books(bookService.getBooksByFilter(filter, sorting))
                .sorting(sorting)
                .filter(filter)
                .build();
    }

}
