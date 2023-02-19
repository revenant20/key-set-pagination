package com.evilcorp.keysetpagination.repository;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "deals")
@ToString
@Setter
@Getter
public class Deal {
    @Id
    private String id;
    private String text;
    private String type;

}
