package inandout.backend.dto.post;

public class InOutResponseDTO {
    private Integer inCount;
    private Integer outCount;

    public InOutResponseDTO(Integer inCount, Integer outCount) {
        this.inCount = inCount;
        this.outCount = outCount;
    }
}
