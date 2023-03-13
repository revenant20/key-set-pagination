package com.evilcorp.keysetpagination.dto.books;

import lombok.Builder;

import java.util.List;

@Builder
public record ListOfBook(List<BookDto> books, String token) {
}
