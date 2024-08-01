package inandout.backend.dto.member;

import lombok.Getter;

@Getter
public class JoinResponseDTO {
    private String result;
    private Integer memberImageId;

    public JoinResponseDTO(String result, Integer memberImageId) {
        this.result = result;
        this.memberImageId = memberImageId;
    }
}
