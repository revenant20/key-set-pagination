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
            *
            from deals
            where id >= :id
            order by id limit :size"""
            , nativeQuery = true)
    List<Deal> findAllKeySet(String id, int size);

    @Query(value = """
            select
            *
            from deals
            order by id limit :size"""
            , nativeQuery = true)
    List<Deal> findFirst(int size);

    @Query(value = """
            select
            *
            from deals
            order by date, id limit :size"""
            , nativeQuery = true)
    List<Deal> findFirstByFilter(int size);

    @Query(value = """
            select
            *
            from deals
            where 1<>1
            or date > :date
            or date = :date and id > :str
            or date = :date and id = :str
            order by date, id
            limit :size"""
            , nativeQuery = true)
    List<Deal> findAllByFilter(int size, LocalDate date, String str);

    @Query(value = """
            select
            *
            from deals
            where date >= :date and id >= :str
            order by date, id
            limit :size"""
            , nativeQuery = true)
    List<Deal> findAllByShortFilter(int size, LocalDate date, String str);

    @Query(value = """
            select
            *
            from deals
            where (date, id) >= (:date, :str)
            order by date, id
            limit :size"""
            , nativeQuery = true)
    List<Deal> findAllByPGShortFilter(int size, LocalDate date, String str);


    Page<Deal> findAll(Pageable pageable);

    Slice<Deal> findAllBy(Pageable page);

}
