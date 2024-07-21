package inandout.backend.dto.myroom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyRoomPostDTO {

    private Integer postId;
    private String imgUrl;
    private LocalDateTime createdAt;

}
