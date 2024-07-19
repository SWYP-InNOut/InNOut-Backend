package inandout.backend.common.exception;

import inandout.backend.common.response.status.ResponseStatus;
import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private final ResponseStatus exceptionStatus;

    public MemberException(ResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
