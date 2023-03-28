package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
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
            (b.rating > :rating or b.id >= :id)
        order by
            b.rating, b.id
    """)
    List<Book> findAllByFilter(
            Integer rating, String id, Pageable pageable);

    @Query(value = """
        select
            b
        from
            Book b
        where
            b.id >= :id
        order by
            b.id
    """)
    List<Book> findAllKeySet(String id, Pageable pageable);

    @Query(value = """
            select
                b
            from
                Book b
            order by
                b.createdAt, b.id
                """
    )
    List<Book> findFirstByFilter(Pageable pageable);

    @Query(value = """
        select
            b
        from
            Book b
        where
            b.createdAt >= :createdAt
        and
            (b.createdAt > :createdAt or b.id >= :id)
        order by
            b.createdAt, b.id
    """)
    List<Book> findAllByFilter(
            Pageable pageable, LocalDate createdAt, String id);

    @Query(nativeQuery = true,
        value = """
        select
            *
        from
            books
        where
            (created_at, id) >= (:createdAt, :id)
        order by
            created_at, id
        limit
            :size
    """)
    List<Book> findAllByPGShortFilter(
            int size, LocalDate createdAt, String id);

    Slice<Book> findAllBy(Pageable page);

    @Query(value = """
        select
            b
        from
            Book b
        where
            row_values(b.createdAt, :createdAt, b.id, :id) = true
        order by
            b.createdAt,
            b.id
    """)
    List<Book> findAllByPGShortFilterJpql(
            LocalDate createdAt, String id, Pageable page);
}
