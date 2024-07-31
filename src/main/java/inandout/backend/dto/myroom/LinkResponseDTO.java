package inandout.backend.dto.myroom;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class LinkResponseDTO {
    private final String link;

    public LinkResponseDTO(String link) {
        this.link = link;
    }
}
