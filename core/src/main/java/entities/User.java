package entities;

import entities.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private int id;
    private String login;
    private String passwordHash;
    private Role role;
    private String mail;
}
