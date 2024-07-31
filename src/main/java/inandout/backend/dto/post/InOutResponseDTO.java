package inandout.backend.dto.post;

import lombok.Getter;

@Getter
public class InOutResponseDTO {
    private int inCount;
    private int outCount;
    private boolean in;
    private boolean out;

    public InOutResponseDTO(int inCount, int outCount, boolean in, boolean out) {
        this.inCount = inCount;
        this.outCount = outCount;
        this.in = in;
        this.out = out;
    }
}
