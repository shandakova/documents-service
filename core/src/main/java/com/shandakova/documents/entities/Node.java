package com.shandakova.documents.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class Node {
    private int id;
    private String name;
    private boolean available;
    private int parentId;
    private LocalDateTime creationDateTime;
}
