package com.shandakova.documents.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "files")
public class DocumentFile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_seq")
    @SequenceGenerator(name = "files_seq",
            sequenceName = "table_files_id_seq", allocationSize = 1)
    private Integer id;
    @Column(name = "creation_datetime", nullable = false)
    private LocalDateTime creationDateTime;
    @JoinColumn(name = "documents_id", nullable = false)
    @ManyToOne(targetEntity = Document.class)
    private Document document;
}
