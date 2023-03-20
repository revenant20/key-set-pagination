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
            order by date, id limit :size"""
            , nativeQuery = true)
    List<App> findFirstByFilter(int size);

    @Query(value = """
            select
            *
            from apps
            where 1<>1
            or date > :date
            or date = :date and id > :str
            or date = :date and id = :str
            order by date, id
            limit :size"""
            , nativeQuery = true)
    List<App> findAllByFilter(int size, LocalDate date, String str);

    @Query(value = """
            select
            *
            from apps
            where date >= :date and id >= :str
            order by date, id
            limit :size"""
            , nativeQuery = true)
    List<App> findAllByShortFilter(int size, LocalDate date, String str);

    @Query(value = """
            select
            *
            from apps
            where (date, id) >= (:date, :str)
            order by date, id
            limit :size"""
            , nativeQuery = true)
    List<App> findAllByPGShortFilter(int size, LocalDate date, String str);
}
