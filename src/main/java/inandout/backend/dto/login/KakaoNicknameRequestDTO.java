package inandout.backend.dto.login;

import lombok.Getter;

@Getter
public class KakaoNicknameRequestDTO {
    private int memberId;
    private String nickname;
}
