package com.evilcorp.keysetpagination.controller;

import com.evilcorp.keysetpagination.dto.BadRequestException;
import com.evilcorp.keysetpagination.dto.books.BookFilter;
import com.evilcorp.keysetpagination.dto.books.GetBookRequest;
import com.evilcorp.keysetpagination.dto.books.ListOfBook;
import com.evilcorp.keysetpagination.dto.books.NextBookPage;
import com.evilcorp.keysetpagination.service.BookService;
import com.evilcorp.keysetpagination.service.EncryptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public record BookController(BookService bookService, EncryptionService encryptionService, ObjectMapper mapper) {

    @SneakyThrows
    @PostMapping
    public ListOfBook books(@RequestBody GetBookRequest request) {
        final BookFilter filter;
        if (StringUtils.isNotBlank(request.token())) {
            var nextPage = encryptionService.decode(request.token());
            filter = nextPage.filter();
        } else {
            filter = request.filter();
        }
        if (filter == null) {
            throw new BadRequestException();
        }
        var books = bookService.getBooksByFilter(filter);
        String newToken;
        if (books.size() == filter.limit()+1) {
            var lastBook = books.get(books.size() - 1);
            newToken = encryptionService.encode(NextBookPage.builder()
                    .filter(
                            BookFilter.builder()
                                    .id(lastBook.id())
                                    .limit(filter.limit())
                                    .rating(lastBook.rating())
                                    .build())
                    .build()
            );
        } else {
            newToken = null;
        }
        return ListOfBook.builder()
                .books(books.stream().limit(filter.limit()).toList())
                .token(newToken)
                .build();
    }

}
