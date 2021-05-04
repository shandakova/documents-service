package com.shandakova.documents.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "nodes")
public abstract class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "node_seq")
    @SequenceGenerator(name = "node_seq",
            sequenceName = "table_nodes_id_seq", allocationSize = 1)
    @Column(updatable = false, nullable = false)
    private Integer id;
    @Column
    private String name;
    @Column
    private boolean available;
    @Column(name = "parent_id")
    private Integer parentId;
    @Column(name = "creation_datetime")
    private LocalDateTime creationDateTime;
}
