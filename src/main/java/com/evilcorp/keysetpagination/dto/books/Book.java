package com.evilcorp.keysetpagination.dto.books;

import lombok.Builder;

@Builder
public record Book(String id, String description, int rating) {
}
