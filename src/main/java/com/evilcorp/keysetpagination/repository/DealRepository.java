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
            order by
                d.createdAt DESC , d.id DESC
                """
            )
    List<Deal> findFirstByFilterWithReversFilter(Pageable pageable);

    @Query(value = """
            select
                d
            from
                Deal d
            where
                d.createdAt >= :createdAt
            and
                (d.createdAt > :createdAt or d.id >= :id)
            order by
                d.createdAt, d.id
                """)
    List<Deal> findAllByFilter(Pageable pageable, LocalDate createdAt, String id);

    @Query(value = """
            select
                d
            from
                Deal d
            where
                d.createdAt <= :createdAt
            and
                (d.createdAt < :createdAt or d.id <= :id)
            order by
                d.createdAt DESC, d.id DESC
                """)
    List<Deal> findAllByFilterWithReverseOrder(Pageable pageable, LocalDate createdAt, String id);

    @Query(value = """
            select
                d
            from
                Deal d
            where
                d.createdAt > :createdAt
            or
                (d.createdAt = :createdAt and d.id >= :id)
            order by
                d.createdAt, d.id
                """)
    List<Deal> findAllBySimpleFilter(Pageable pageable, LocalDate createdAt, String id);

    @Query(value = """
            select
                d
            from
                Deal d
            where
                d.createdAt < :createdAt
            or
                (d.createdAt = :createdAt and d.id <= :id)
            order by
                d.createdAt DESC , d.id DESC
                """)
    List<Deal> findAllBySimpleFilterWithReverseOrder(Pageable pageable, LocalDate createdAt, String id);

    @Query(value = """
            select
                *
            from
                deals
            where
                (created_at, id) >= (:createdAt, :id)
            order by
                created_at, id
            limit
                :size
                """
            , nativeQuery = true)
    List<Deal> findAllByPGShortFilter(int size, LocalDate createdAt, String id);

    Page<Deal> findAll(Pageable pageable);

    Slice<Deal> findAllBy(Pageable page);

    @Override
    @Query(value = """
            select
                a
            from
                Deal a
            where
                row_values(a.createdAt, :createdAt, a.id, :id) = true
            order by
                a.createdAt,
                a.id
            """
    )
    List<Deal> findAllByPGShortFilterJpql(LocalDate createdAt, String id, Pageable page);
}
