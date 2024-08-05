package inandout.backend.dto.myroom;

import lombok.Getter;

@Getter
public class LinkResponseDTO {
    private final String link;
    private final boolean isAnonymous;

    public LinkResponseDTO(String link, boolean isAnonymous) {
        this.link = link;
        this.isAnonymous = isAnonymous;
    }
}
