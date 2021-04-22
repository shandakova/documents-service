package com.shandakova.documents.entities;

import com.shandakova.documents.entities.enums.Importance;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Document extends Node {
    private String description;
    private int typeId;
    private Importance importance;
    private int versionNumber;
    private boolean verified;
    private Integer previousVersionId;
}
