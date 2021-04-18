package entities;

import entities.enums.Importance;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Document extends Object {
    private String description;
    private int type_id;
    private Importance importance;
    private int version_number;
    private int previous_version_id;
}
