package dto;

import com.shandakova.documents.entities.Node;
import com.shandakova.documents.entities.enums.Importance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentDTO extends Node {
    private String description;
    private Integer typeId;
    private Importance importance;
    private Integer versionNumber;
}
