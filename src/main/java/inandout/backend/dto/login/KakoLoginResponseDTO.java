package inandout.backend.dto.login;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakoLoginResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String email;


}
