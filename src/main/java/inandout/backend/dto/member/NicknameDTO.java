package inandout.backend.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
public class NicknameDTO {
    private String nickname;

    public NicknameDTO(String nickname) {
        this.nickname = nickname;
    }
}
