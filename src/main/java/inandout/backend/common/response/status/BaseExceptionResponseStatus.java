package inandout.backend.common.response.status;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BaseExceptionResponseStatus implements ResponseStatus {
    /**
     * 1000: 요청 성공 (OK)
     */
    SUCCESS(1000, HttpStatus.OK.value(), "요청에 성공하였습니다."),

    /**
     * 2000: Request 오류 (BAD_REQUEST)
     */
    BAD_REQUEST(2000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 요청입니다."),
    URL_NOT_FOUND(2001, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 URL 입니다."),
    METHOD_NOT_ALLOWED(2002, HttpStatus.METHOD_NOT_ALLOWED.value(), "해당 URL에서는 지원하지 않는 HTTP Method 입니다."),
    INAPPROPRIATE_DATA(2003, HttpStatus.BAD_REQUEST.value(), "입력한 정보가 올바르지 않습니다."),
    INAPPROPRIATE_TYPE_DATA(2004, HttpStatus.BAD_REQUEST.value(), "입력한 정보의 타입이 올바르지 않습니다."),

    /**
     * 3000: Server, Database 오류 (INTERNAL_SERVER_ERROR)
     */
    SERVER_ERROR(3000, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버에서 오류가 발생하였습니다."),
    DATABASE_ERROR(3001, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스에서 오류가 발생하였습니다."),
    BAD_SQL_GRAMMAR(3002, HttpStatus.INTERNAL_SERVER_ERROR.value(), "SQL에 오류가 있습니다."),
    TIME_OUT_OF_BOUND(3003, HttpStatus.INTERNAL_SERVER_ERROR.value(), "범위를 벗어난 날짜 정보가 존재합니다."),

    /**
     * 4000: Authorization 오류
     */
    JWT_ERROR(4000, HttpStatus.UNAUTHORIZED.value(), "JWT에서 오류가 발생하였습니다."),
    TOKEN_NOT_FOUND(4001, HttpStatus.BAD_REQUEST.value(), "토큰이 HTTP Header에 없습니다."),
    UNSUPPORTED_TOKEN_TYPE(4002, HttpStatus.UNAUTHORIZED.value(), "지원되지 않는 토큰 형식입니다."),
    UNSUPPORTED_ID_TOKEN_TYPE(4003, HttpStatus.UNAUTHORIZED.value(), "OAuth Identity Token의 형식이 올바르지 않습니다."),
    INVALID_TOKEN(4007, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 토큰입니다."),
    MALFORMED_TOKEN(4008, HttpStatus.UNAUTHORIZED.value(), "올바르지 않은 토큰입니다."),
    EXPIRED_TOKEN(4009, HttpStatus.UNAUTHORIZED.value(), "로그인 인증 유효 기간이 만료되었습니다."),
    TOKEN_MISMATCH(4010, HttpStatus.UNAUTHORIZED.value(), "로그인 정보가 토큰 정보와 일치하지 않습니다."),
    INVALID_CLAIMS(4011, HttpStatus.UNAUTHORIZED.value(), "OAuth Claims 값이 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(4012, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 Refresh Token입니다."),
    EXPIRED_ACCESSTOKEN(4013, HttpStatus.UNAUTHORIZED.value(), "AccessToken 유효 기간이 만료되었습니다."),
    EXPIRED_REFRESHTOKEN(4014, HttpStatus.UNAUTHORIZED.value(), "RefreshToken 유효 기간이 만료되었습니다."),
    NOT_FOUND_REFRESHTOKEN(4015, HttpStatus.UNAUTHORIZED.value(), "존재하지 않는 RefreshToken입니다."),
    FAILED_SEND_EMAIL(4016, HttpStatus.BAD_REQUEST.value(), "인증 메일 전송에 실패하였습니다."),
    FAILED_EMAIL_CERTIFICATION(4017, HttpStatus.UNAUTHORIZED.value(), "이메일 인증에 실패하였습니다."),

    /**
     * 5000: 회원 정보 오류
     */
    INVALID_PLATFORM(5001, HttpStatus.BAD_REQUEST.value(), "플랫폼 정보가 올바르지 않습니다."),
    NOT_FOUND_MEMBER(5002, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 사용자입니다."),
    INACTIVE_MEMBER(5003, HttpStatus.BAD_REQUEST.value(), "활성화 상태가 아닌 사용자입니다."),
    DUPLICATED_EMAIL(5004, HttpStatus.BAD_REQUEST.value(), "중복된 이메일입니다."),
    DUPLICATED_NICKNAME(5005, HttpStatus.BAD_REQUEST.value(), "중복된 닉네임입니다."),
    ACTIVE_MEMBER(5006, HttpStatus.BAD_REQUEST.value(), "이미 가입된 사용자입니다."),
    INVALID_PASSWORD(5007, HttpStatus.BAD_REQUEST.value(), "비밀번호가 올바르지 않습니다."),

    /**
     * 9000: 기타 오류
     */
    INVALID_STORAGE_DOMAIN(9000, HttpStatus.BAD_REQUEST.value(), "업로드할 이미지의 도메인이 올바르지 않습니다."),
    INVALID_FILE_EXTENSION(9002, HttpStatus.BAD_REQUEST.value(), "파일의 형식이 올바르지 않습니다."),
    NOT_FOUND_FILE(9003, HttpStatus.BAD_REQUEST.value(), "파일이 업로드되지 않았습니다.");

    private final int code;
    private final int status;
    private final String message;

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
