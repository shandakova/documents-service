package entities;

import entities.enums.Access;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permission {
    private int id;
    private int userId;
    private int objectId;
    private Access access;

}
