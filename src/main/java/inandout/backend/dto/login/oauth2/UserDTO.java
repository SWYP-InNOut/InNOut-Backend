package inandout.backend.dto.login.oauth2;

import lombok.Getter;

@Getter
public class UserDTO {
    private Integer memberId;
    private String email;
    private Boolean isActive;
    private Integer imageId;

    public UserDTO(Integer memberId, String email, Boolean isActive, Integer imageId) {
        this.memberId = memberId;
        this.email = email;
        this.isActive = isActive;
        this.imageId = imageId;
    }
}
