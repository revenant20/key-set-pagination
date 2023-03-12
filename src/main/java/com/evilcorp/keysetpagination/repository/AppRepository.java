package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.App;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppRepository extends DataRepository<App, String> {

    @Query(value = """
            select
            *
            from apps
            where id >= :id
            order by id limit :size"""
            , nativeQuery = true)
    List<App> findAllKeySet(String id, int size);

    @Query(value = """
            select
            *
            from apps
            order by id limit :size"""
            , nativeQuery = true)
    List<App> findFirst(int size);


    Page<App> findAll(Pageable pageable);
}
