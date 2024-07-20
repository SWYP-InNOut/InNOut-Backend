package inandout.backend.common.exception_handler;

import inandout.backend.common.exception.BaseException;
import inandout.backend.common.response.BaseErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

import static inandout.backend.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@RestControllerAdvice
public class BaseExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({BaseException.class})
    public BaseErrorResponse handle_BaseException(BaseException e) {
        log.error("[handle_BadRequest]", e);
        return new BaseErrorResponse(e.getExceptionStatus());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({TypeMismatchException.class})
    public BaseErrorResponse handle_TypeMismatchException(Exception e) {
        log.error("[handle_BadRequest]", e);
        return new BaseErrorResponse(INAPPROPRIATE_TYPE_DATA);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({NoHandlerFoundException.class})
    public BaseErrorResponse handle_NoHandlerFoundException(Exception e) {
        log.error("[handle_BadRequest]", e);
        return new BaseErrorResponse(URL_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseErrorResponse handle_HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("[handle_HttpRequestMethodNotSupportedException]", e);
        return new BaseErrorResponse(METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({IllegalStateException.class, IOException.class})
    public BaseErrorResponse handle_IllegalArgumentException(Exception e) {
        log.error("[handle_IllegalArgumentException]", e);
        return new BaseErrorResponse(BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class, MissingServletRequestPartException.class})
    public BaseErrorResponse handle_MethodArgumentNotValidException(Exception e) {
        log.error("[handle_MethodArgumentNotValidException]", e);
        return new BaseErrorResponse(INAPPROPRIATE_DATA);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public BaseErrorResponse handle_RuntimeException(Exception e) {
        log.error("[handle_RuntimeException]", e);
        return new BaseErrorResponse(SERVER_ERROR);
    }
}
