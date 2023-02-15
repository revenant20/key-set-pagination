package com.evilcorp.keysetpagination.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppRepository extends CrudRepository<App, String> {

    @Query(value = """
            select
            *
            from apps
            where id >= :id
            order by id limit 11"""
            , nativeQuery = true)
    List<App> findAllKeySet(String id);

    @Query(value = """
            select
            *
            from apps
            order by id limit 11"""
            , nativeQuery = true)
    List<App> findFirst();


    Page<App> findAll(Pageable pageable);
}
