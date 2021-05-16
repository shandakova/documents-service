package com.shandakova.documents.entities;

import com.shandakova.documents.entities.enums.Importance;
import com.shandakova.documents.entities.enums.NodeType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "documents")
@DiscriminatorValue(NodeType.Values.DOCUMENT)
public class Document extends Node {
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "type_id")
    private DocumentType type;
    @Column(name = "importance", nullable = false)
    @Enumerated(EnumType.STRING)
    private Importance importance;
    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;
    @Column(name = "verified")
    private boolean verified;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "previous_version_id")
    private Document previousVersion;
}
