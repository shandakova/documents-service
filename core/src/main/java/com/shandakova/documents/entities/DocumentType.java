package com.shandakova.documents.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "types")
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "type_seq")
    @SequenceGenerator(name = "type_seq",
            sequenceName = "table_types_id_seq", allocationSize = 1)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
}
