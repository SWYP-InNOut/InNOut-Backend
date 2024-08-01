package inandout.backend.dto.myroom;

import lombok.Getter;

@Getter
public class LinkResponseDTO {
    private final String link;

    public LinkResponseDTO(String link) {
        this.link = link;
    }
}
