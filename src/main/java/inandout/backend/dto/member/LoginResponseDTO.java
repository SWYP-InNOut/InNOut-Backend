package inandout.backend.dto.member;

import lombok.Getter;

@Getter
public class LoginResponseDTO {
    private Integer memberId;
    private String nickname;
    private Integer memberImageId;

    public LoginResponseDTO(Integer memberId, String nickname, Integer memberImageId) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.memberImageId = memberImageId;
    }
}
