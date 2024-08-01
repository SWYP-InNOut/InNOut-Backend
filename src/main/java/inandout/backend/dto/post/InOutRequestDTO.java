package inandout.backend.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class InOutRequestDTO {
    private Integer postId;
    private Boolean isMember;
    private Boolean in;
    private Boolean out;
}
