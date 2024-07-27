package inandout.backend.dto.member;

import lombok.Getter;

@Getter
public class IsPublicDTO {
    private boolean isPublic;

    public IsPublicDTO(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
