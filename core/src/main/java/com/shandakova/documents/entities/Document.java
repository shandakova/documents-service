package com.shandakova.documents.entities;

import com.shandakova.documents.entities.enums.Importance;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "documents")
public class Document extends Node {
    @Column
    private String description;

    @Column(name = "type_id")
    private Integer typeId;
    @Enumerated(EnumType.STRING)
    private Importance importance;
    @Column(name = "version_number")
    private Integer versionNumber;
    @Column
    private boolean verified;
    @Column(name = "previous_version_id")
    private Integer previousVersionId;
}
