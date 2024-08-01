package inandout.backend.dto.myroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class MyRoomRequestDTO {
    private Integer ownerId;
    private Integer filterType;
}
