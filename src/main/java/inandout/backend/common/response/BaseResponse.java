package inandout.backend.common.response;

import inandout.backend.common.response.status.ResponseStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseResponse implements ResponseStatus {
    private final int code;
    private final int status;
    private final String message;
    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
