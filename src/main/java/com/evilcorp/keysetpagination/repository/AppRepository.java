package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.App;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AppRepository extends DataRepository<App, String> {

    @Query(value = """
            select
                app
            from
                App app
            where
                app.id >= :id
            order by
                app.id
                """)
    List<App> findAllKeySet(String id, Pageable pageable);

    @Query(value = """
            select
                app
            from
                App app
            order by
                app.id
                """)
    List<App> findFirst(Pageable pageable);

    Page<App> findAll(Pageable pageable);

    @Query(value = """
            select
                app
            from
                App app
            order by
                app.createdAt, app.id
                """)
    List<App> findFirstByFilter(Pageable pageable);

    @Query(value = """
            select
                app
            from
                App app
            where
                app.createdAt >= :createdAt
            and
                (app.createdAt > :createdAt or app.id >= :id)
            order by
                app.createdAt, app.id
                """)
    List<App> findAllByFilter(Pageable pageable, LocalDate createdAt, String id);

    @Query(value = """
            select
                app
            from
                App app
            where
                app.createdAt > :createdAt
            or
                (app.createdAt = :createdAt and app.id >= :id)
            order by
                app.createdAt, app.id
                """)
    List<App> findAllBySimpleFilter(Pageable pageable, LocalDate createdAt, String id);

    @Query(value = """
            select
                *
            from
                apps
            where
                (created_at, id) >= (:createdAt, :id)
            order by
                created_at, id
            limit
                :size
                """
            , nativeQuery = true)
    List<App> findAllByPGShortFilter(int size, LocalDate createdAt, String id);

    @Override
    @Query(value = """
            select
                a
            from
                App a
            where
                row_values(a.createdAt, :createdAt, a.id, :id) = true
            order by
                a.createdAt,
                a.id
            """
            )
    List<App> findAllByPGShortFilterJpql(LocalDate createdAt, String id, Pageable page);
}
