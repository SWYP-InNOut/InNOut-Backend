package inandout.backend.dto.others;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InOutRequestDTO {
    private Integer postId;
    private Boolean isMember;
    private Integer memberId;
    private Integer inoutType;
}
