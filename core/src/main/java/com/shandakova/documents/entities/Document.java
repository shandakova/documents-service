package com.shandakova.documents.entities;

import com.shandakova.documents.entities.enums.Importance;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Document extends Nodes {
    private String description;
    private int type_id;
    private Importance importance;
    private int version_number;
}
