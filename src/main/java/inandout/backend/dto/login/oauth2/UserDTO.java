package inandout.backend.dto.login.oauth2;

import lombok.Getter;

@Getter
public class UserDTO {
    private Integer memberId;
    private String email;

    public UserDTO(Integer memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }
}
