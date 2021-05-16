package com.shandakova.documents.dto;

import com.shandakova.documents.entities.DocumentType;
import com.shandakova.documents.entities.enums.Importance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentDTO extends NodeDTO {
    private String description;
    private DocumentType type;
    private Importance importance;
    private Integer versionNumber;
    private Integer parentDocument;
}
