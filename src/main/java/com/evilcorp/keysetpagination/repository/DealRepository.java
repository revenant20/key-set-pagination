package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.Deal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DealRepository extends DataRepository<Deal, String> {

    @Query(value = """
            select
                d
            from
                Deal d
            where
                d.id >= :id
            order by
                d.id
            """
            )
    List<Deal> findAllKeySet(String id, Pageable pageable);

    @Query(value = """
            select
                d
            from
                Deal d
            order by
                d.id
                """
            )
    List<Deal> findFirst(Pageable pageable);

    @Query(value = """
            select
                d
            from
                Deal d
            order by
                d.createdAt, d.id
                """
            )
    List<Deal> findFirstByFilter(Pageable pageable);

    @Query(value = """
            select
                d
            from
                Deal d
            where
                d.createdAt >= :date
            and
                (d.createdAt > :date or d.id >= :str)
            order by
                d.createdAt, d.id
                """)
    List<Deal> findAllByFilter(Pageable pageable, LocalDate date, String str);

    @Query(value = """
            select
                *
            from
                deals
            where
                (created_at, id) >= (:date, :str)
            order by
                created_at, id
            limit
                :size
                """
            , nativeQuery = true)
    List<Deal> findAllByPGShortFilter(int size, LocalDate date, String str);

    Page<Deal> findAll(Pageable pageable);

    Slice<Deal> findAllBy(Pageable page);

    @Override
    @Query(value = """
            select
                a
            from
                Deal a
            where
                row_values(a.createdAt, :date, a.id, :str) = true
            order by
                a.createdAt,
                a.id
            """
    )
    List<Deal> findAllByPgShortFilterJpql(LocalDate date, String str, Pageable page);
}
