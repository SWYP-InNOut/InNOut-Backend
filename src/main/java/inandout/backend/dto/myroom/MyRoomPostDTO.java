package inandout.backend.dto.myroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyRoomPostDTO {

    private Integer postId;
    private String imgUrl;
    private Integer inCount;
    private Integer outCount;
    private LocalDateTime createdAt;

}
