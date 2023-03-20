package com.evilcorp.keysetpagination.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "apps")
@ToString
@Setter
@Getter
public class App implements Ent {
    @Id
    private String id;
    private String text;
    private String type;
    private LocalDate date;

}
