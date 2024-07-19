package inandout.backend.common.exception_handler;

import inandout.backend.common.exception.MemberException;
import inandout.backend.common.response.BaseErrorResponse;
import jakarta.annotation.Priority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Priority(0)
@RestControllerAdvice
public class MemberExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MemberException.class)
    public BaseErrorResponse handel_MemberException(MemberException e) {
        log.error("[handel_MemberException]", e);
        return new BaseErrorResponse(e.getExceptionStatus());
    }
}
