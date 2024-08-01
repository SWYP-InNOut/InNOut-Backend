package inandout.backend.dto.member;

import lombok.Getter;

@Getter
public class ModifyUserRequestDTO {

    private Integer memberId;
    private String nickname;
    private Integer memberImageId;


}
