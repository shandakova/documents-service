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
    @Column(name = "login", nullable = false, unique = true)
    private String login;
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "mail", nullable = false)
    private String mail;

    @Transient
    private String password;
}
