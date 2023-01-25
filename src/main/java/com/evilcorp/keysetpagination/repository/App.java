package com.evilcorp.keysetpagination.repository;


import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "apps")
@ToString
public class App {
    @Id
    private String id;
    private String text;
    private String type;

}
