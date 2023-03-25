package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.Deal;
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

    List<T> findAllByFilter(Pageable pageable, LocalDate date, String str);

    List<T> findAllByPGShortFilter(int size, LocalDate date, String str);

    List<T> findAllByPgShortFilterJpql(LocalDate date, String str, Pageable page);
}
