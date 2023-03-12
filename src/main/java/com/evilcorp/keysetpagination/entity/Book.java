package com.evilcorp.keysetpagination.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "books")
@ToString
@Setter
@Getter
public class Book {
    @Id
    private String id;
    private String description;
    private Integer rating;
}
