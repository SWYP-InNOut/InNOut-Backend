package inandout.backend.dto.login.oauth2;

import lombok.Getter;

@Getter
public class UserDTO {
    private Integer memberId;
    private String email;
    private Boolean isActive;

    public UserDTO(Integer memberId, String email, Boolean isActive) {
        this.memberId = memberId;
        this.email = email;
        this.isActive = isActive;
    }
}
