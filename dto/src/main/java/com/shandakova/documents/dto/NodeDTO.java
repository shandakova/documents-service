package com.shandakova.documents.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NodeDTO {
    private Integer id;
    private String name;
    private Integer parentId;
    private LocalDateTime creationDateTime;
    private String nodeType;

}
