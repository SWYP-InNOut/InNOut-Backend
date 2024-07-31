package inandout.backend.dto.post;

import lombok.Getter;

@Getter
public class InOutResponseDTO {
    private int inCount;
    private int outCount;

    public InOutResponseDTO(Integer inCount, Integer outCount) {
        this.inCount = inCount;
        this.outCount = outCount;
    }
}
