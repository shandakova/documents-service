package com.shandakova.documents.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentFile {
    private Integer id;
    private LocalDateTime creationDateTime;
    private Integer documentId;
}
