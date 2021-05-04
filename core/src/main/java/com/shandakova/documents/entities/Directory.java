package com.shandakova.documents.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "directories")
@Entity
public class Directory extends Node {
}
