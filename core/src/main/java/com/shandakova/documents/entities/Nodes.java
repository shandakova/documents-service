package com.shandakova.documents.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Nodes {
    private int id;
    private String name;
    private boolean available;
    private int parent_id;
}
