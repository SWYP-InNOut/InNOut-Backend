package inandout.backend.dto.login;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakoLoginResponseDTO {

    private String accessToken;
//    private String refreshToken;
//    private String name;
    private boolean isMember;
    private Integer memberId;



}
