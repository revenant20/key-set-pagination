package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.dto.books.BookDto;
import com.evilcorp.keysetpagination.dto.books.BookFilter;
import com.evilcorp.keysetpagination.entity.Book;
import com.evilcorp.keysetpagination.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

    public List<BookDto> getBooksByFilter(BookFilter filter) {
        List<Book> books;
        if (filter.id() == null || filter.id().isBlank()) {
            books = repository.findFirst(Pageable.ofSize(filter.limit() + 1));
        } else {
            books = repository.findAllByFilter(filter.rating(), filter.id(), Pageable.ofSize(filter.limit() + 1));
        }
        return books.stream()
                .map(book -> BookDto.builder()
                        .id(book.getId())
                        .description(book.getDescription())
                        .rating(book.getRating())
                        .build()
                ).toList();
    }

}
