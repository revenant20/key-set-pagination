package com.evilcorp.keysetpagination.controller;

import com.evilcorp.keysetpagination.dto.BadRequestException;
import com.evilcorp.keysetpagination.dto.books.BookDto;
import com.evilcorp.keysetpagination.dto.books.BookFilter;
import com.evilcorp.keysetpagination.dto.books.GetBookRequest;
import com.evilcorp.keysetpagination.dto.books.ListOfBook;
import com.evilcorp.keysetpagination.dto.books.NextBookPage;
import com.evilcorp.keysetpagination.dto.books.Sorting;
import com.evilcorp.keysetpagination.service.BookService;
import com.evilcorp.keysetpagination.service.EncryptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public record BookController(BookService bookService, EncryptionService encryptionService, ObjectMapper mapper) {

    @SneakyThrows
    @PostMapping
    public ListOfBook books(@RequestBody GetBookRequest request) {
        Sorting sorting;
        final BookFilter filter;
        if (StringUtils.isNotBlank(request.token())) {
            var nextPage = encryptionService.decode(request.token());
            sorting = nextPage.sorting();
            filter = nextPage.filter();
        } else {
            sorting = request.sorting();
            filter = request.filter();
        }
        if (sorting == null || filter == null) {
            throw new BadRequestException();
        }
        var books = bookService.getBooksByFilter(filter, sorting);
        String newToken;
        if (books.size() == filter.limit()) {
            newToken = encryptionService.encode(NextBookPage.builder()
                    .sorting(sorting)
                    .filter(new BookFilter(filter.limit(), filter.offset() + 1))
                    .build()
            );
        } else {
            newToken = null;
        }
        return ListOfBook.builder()
                .books(books)
                .token(newToken)
                .build();
    }

}
