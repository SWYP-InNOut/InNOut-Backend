package inandout.backend.dto.member;

import lombok.Getter;

@Getter
public class LoginResponseDTO {
    private String memberId;
    private String nickname;

    public LoginResponseDTO(String memberId, String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }
}
