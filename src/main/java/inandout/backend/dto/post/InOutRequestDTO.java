package inandout.backend.dto.post;

import lombok.Getter;

@Getter
public class InOutRequestDTO {
    private Integer postId;
    private Boolean isMember;
    private Boolean in;
    private Boolean out;
}
