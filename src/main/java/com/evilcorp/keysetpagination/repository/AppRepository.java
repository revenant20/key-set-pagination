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

    @Query(value = """
            select
            *
            from apps
            order by created_at, id limit :size"""
            , nativeQuery = true)
    List<App> findFirstByFilter(int size);

    @Query(value = """
            select
            *
            from apps
            where created_at >= :date
            and (created_at > :date or id >= :str)
            order by created_at, id
            limit :size"""
            , nativeQuery = true)
    List<App> findAllByFilter(int size, LocalDate date, String str);

    @Query(value = """
            select
            *
            from apps
            where (created_at, id) >= (:date, :str)
            order by created_at, id
            limit :size"""
            , nativeQuery = true)
    List<App> findAllByPGShortFilter(int size, LocalDate date, String str);
}
