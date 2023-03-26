package com.evilcorp.keysetpagination.dto.books;

import lombok.Builder;

@Builder
public record BookFilter(int limit, String id, Integer rating) {
}
