package entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Object {
    private int id;
    private String name;
    private boolean available;
}
