package com.evilcorp.keysetpagination.dto.books;

import lombok.Builder;

@Builder
public record GetBookRequest(BookFilter filter, String token) {
}
