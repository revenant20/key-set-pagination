package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, String> {
    Page<Book> findAll(Pageable pageable);

    @Query(value = """
            select
                b
            from
                Book b
            order by
                b.rating, b.id
                """)
    List<Book> findFirst(Pageable pageable);

    @Query(value = """
            select
                b
            from
                Book b
            where
                b.rating >= :rating
            and
                (b.rating > :rating or b.id >= :str)
            order by
                b.rating, b.id
                """)
    List<Book> findAllByFilter(Integer rating, String str, Pageable pageable);
}
