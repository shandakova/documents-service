package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DirectoryDTO extends NodeDTO {
    public DirectoryDTO(Integer id, String name, Integer parentId, LocalDateTime creationDateTime) {
        super(id, name, parentId, creationDateTime);
    }
}
