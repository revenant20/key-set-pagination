package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, String> {
    Page<Book> findAll(Pageable pageable);
}
