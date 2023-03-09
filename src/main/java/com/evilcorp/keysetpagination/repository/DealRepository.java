package com.evilcorp.keysetpagination.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;

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


    Page<Deal> findAll(Pageable pageable);

    Slice<Deal> findAllDealsBy(Pageable page);

}
