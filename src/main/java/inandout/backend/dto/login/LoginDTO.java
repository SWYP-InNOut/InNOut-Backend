package inandout.backend.dto.login;

import inandout.backend.entity.auth.Platform;
import inandout.backend.entity.member.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDTO {

    private String name;
    private String email;
    private String password;
    private Platform platform;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
    private MemberStatus status;
//    private boolean isPublic;

}
