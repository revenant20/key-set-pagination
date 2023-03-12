package com.evilcorp.keysetpagination.dto.books;

import lombok.Builder;

@Builder
public record BookDto(String id, String description, int rating) {
}
