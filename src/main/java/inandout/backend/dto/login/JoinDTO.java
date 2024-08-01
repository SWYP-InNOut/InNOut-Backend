package inandout.backend.dto.login;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDTO {
    private String username;

    private String email;

    private String password;

    private Integer memberImageId;
}
