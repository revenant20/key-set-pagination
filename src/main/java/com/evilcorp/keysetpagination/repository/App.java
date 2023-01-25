package com.evilcorp.keysetpagination.repository;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "apps")
public class App {
    @Id
    private String id;
    private String text;
    private String type;

}
