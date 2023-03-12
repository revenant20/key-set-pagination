package com.evilcorp.keysetpagination.dto.books;

public record GetBookRequest(BookFilter filter, Sorting sorting, String token) {
}
