package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.App;
import com.evilcorp.keysetpagination.entity.Deal;
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
                app.createdAt >= :date
            and
                (app.createdAt > :date or app.id >= :str)
            order by
                app.createdAt, app.id
                """)
    List<App> findAllByFilter(Pageable pageable, LocalDate date, String str);

    @Query(value = """
            select
                *
            from
                apps
            where
                (created_at, id) >= (:date, :str)
            order by
                created_at, id
            limit
                :size
                """
            , nativeQuery = true)
    List<App> findAllByPGShortFilter(int size, LocalDate date, String str);

    @Override
    @Query(value = """
            select
                a
            from
                App a
            where
                row_values(a.createdAt, :date, a.id, :str) = true
            order by
                a.createdAt,
                a.id
            """
            )
    List<App> findAllByPgShortFilterJpql(LocalDate date, String str, Pageable page);
}
