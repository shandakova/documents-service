package com.shandakova.documents.entities;

import com.shandakova.documents.entities.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentServiceUser {
    private Integer id;
    private String login;
    private String passwordHash;
    private Role role;
    private String mail;
}
