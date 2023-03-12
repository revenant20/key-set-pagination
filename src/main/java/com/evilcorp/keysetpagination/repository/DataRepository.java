package com.evilcorp.keysetpagination.repository;

import com.evilcorp.keysetpagination.entity.Deal;
import com.evilcorp.keysetpagination.entity.Ent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface DataRepository<T extends Ent, M> extends CrudRepository<T, M> {

    List<T> findAllKeySet(String id, int size);

    List<T> findFirst(int size);

    Page<T> findAll(Pageable pageable);

    Slice<Deal> findAllDealsBy(Pageable page);
}
