package com.shandakova.documents.entities;

import com.shandakova.documents.entities.enums.NodeType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "directories")
@DiscriminatorValue(NodeType.Values.DIRECTORY)
@Entity
public class Directory extends Node {
}
