package inandout.backend.dto.others;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OthersResponseDTO {

    private String memberName;
    private Integer memberId;
    private String imageUrl;
    private Integer memberImageId;

}
