package com.shandakova.documents.entities;

import com.shandakova.documents.entities.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class DocumentServiceUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @Column
    private String login;
    @Column
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column
    private String mail;
}
