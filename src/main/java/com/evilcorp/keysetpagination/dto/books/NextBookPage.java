package com.evilcorp.keysetpagination.dto.books;

import lombok.Builder;

@Builder
public record NextBookPage(BookFilter filter, Sorting sorting) {
}
