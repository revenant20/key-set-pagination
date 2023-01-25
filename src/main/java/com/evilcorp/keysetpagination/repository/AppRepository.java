package com.evilcorp.keysetpagination.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AppRepository extends JpaRepository<App, String> {

    Page<App> findAll(Pageable pageable);
}
