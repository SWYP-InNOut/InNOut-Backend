package inandout.backend.dto.others;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OthersResponseDTO {

    private String memberName;
    private Integer memberId;
    private Integer postCount;
    private Integer inCount;
    private Integer outCount;
//    private Integer userCount; // 조회수
    private LocalDateTime recentPostDate;


}
