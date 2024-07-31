package inandout.backend.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import inandout.backend.common.response.status.ResponseStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@JsonPropertyOrder({"code", "status", "message", "timestamp"})
public class BaseErrorResponse extends Throwable implements ResponseStatus {
    private final int code;
    private final int status;
    private final String message;
//    private final LocalDateTime timestamp;

    public BaseErrorResponse(ResponseStatus status){
        this.code = status.getCode();
        this.status = status.getStatus();
        this.message = status.getMessage();
//        this.timestamp = LocalDateTime.now();
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
