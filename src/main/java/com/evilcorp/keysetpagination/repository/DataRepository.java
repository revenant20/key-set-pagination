package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.Ent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDate;
import java.util.List;

@NoRepositoryBean
public interface DataRepository<T extends Ent, M> extends CrudRepository<T, M> {

    List<T> findAllKeySet(String id, Pageable pageable);

    List<T> findFirst(Pageable pageable);

    Page<T> findAll(Pageable pageable);

    Slice<T> findAllBy(Pageable page);

    List<T> findFirstByFilter(Pageable pageable);

    List<T> findFirstByFilterWithReversFilter(Pageable pageable);

    List<T> findAllByFilter(Pageable pageable, LocalDate createdAt, String id);
    List<T> findAllByFilterWithReverseOrder(Pageable pageable, LocalDate createdAt, String id);

    List<T> findAllBySimpleFilter(Pageable pageable, LocalDate createdAt, String id);

    List<T> findAllBySimpleFilterWithReverseOrder(Pageable pageable, LocalDate createdAt, String id);

    List<T> findAllByPGShortFilter(int size, LocalDate createdAt, String id);

    List<T> findAllByPGShortFilterJpql(LocalDate createdAt, String id, Pageable page);
}
