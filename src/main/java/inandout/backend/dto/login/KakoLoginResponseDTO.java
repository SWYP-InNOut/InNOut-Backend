package inandout.backend.dto.login;


import lombok.*;

@Getter
@AllArgsConstructor
public class KakoLoginResponseDTO {

    private String accessToken;
//    private String refreshToken;
//    private String name;
    private boolean isMember;
    private Integer memberId;
    private Integer memberImageId;

}
