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

    List<T> findAllKeySet(String id, int size);

    List<T> findFirst(int size);

    Page<T> findAll(Pageable pageable);

    Slice<T> findAllBy(Pageable page);

    List<T> findFirstByFilter(int size);

    List<T> findAllByFilter(int size, LocalDate date, String str);

    List<T> findAllByPGShortFilter(int size, LocalDate date, String str);

    List<T> findAllByPgShortFilterJpql(LocalDate date, String str, Pageable page);
}
