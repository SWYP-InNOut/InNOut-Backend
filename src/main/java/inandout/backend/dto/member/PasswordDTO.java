package inandout.backend.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDTO {
    private String password;

    public PasswordDTO(String password) {
        this.password = password;
    }
}
