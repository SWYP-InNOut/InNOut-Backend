package inandout.backend.dto.member;

import lombok.Getter;

@Getter
public class LoginResponseDTO {
    private Integer memberId;
    private String nickname;

    public LoginResponseDTO(Integer memberId, String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }
}
