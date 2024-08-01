package inandout.backend.dto.others;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OthersResponseDTO {

    private String memberName;
    private Integer memberId;
    private String imageUrl;
    private Integer memberImageId;

}
