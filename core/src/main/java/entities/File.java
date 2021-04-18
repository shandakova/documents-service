package entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class File {
    private int id;
    private LocalDateTime creationDateTime;
    private int documentId;
}
