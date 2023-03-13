package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.dto.books.BookDto;
import com.evilcorp.keysetpagination.dto.books.BookFilter;
import com.evilcorp.keysetpagination.dto.books.Sorting;
import com.evilcorp.keysetpagination.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;

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

    public BookRepository repository() {
        return repository;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BookService) obj;
        return Objects.equals(this.repository, that.repository);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repository);
    }

    @Override
    public String toString() {
        return "BookService[" +
               "repository=" + repository + ']';
    }

}
