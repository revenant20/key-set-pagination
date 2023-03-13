package com.evilcorp.keysetpagination.dto.books;

import org.springframework.data.domain.Sort;

import java.util.List;

public record Sorting(Sort.Direction direction, List<String> properties) {
}
