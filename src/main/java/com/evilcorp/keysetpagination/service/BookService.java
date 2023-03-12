package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.dto.books.BookDto;
import com.evilcorp.keysetpagination.dto.books.BookFilter;
import com.evilcorp.keysetpagination.dto.books.Sorting;
import com.evilcorp.keysetpagination.repository.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record BookService(BookRepository repository) {

    public List<BookDto> getBooksByFilter(BookFilter filter, Sorting sorting) {
        var books = repository.findAll(
                PageRequest.of(filter.offset(), filter.limit(),
                        Sort.by(sorting.direction(), sorting.properties().toArray(String[]::new))
                )
        );
        return books.get()
                .map(book -> BookDto.builder()
                        .id(book.getId())
                        .description(book.getDescription())
                        .rating(book.getRating())
                        .build()
                ).toList();
    }
}
