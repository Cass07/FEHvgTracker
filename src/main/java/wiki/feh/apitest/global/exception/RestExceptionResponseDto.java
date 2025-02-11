package wiki.feh.apitest.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wiki.feh.apitest.global.exception.business.BusinessException;

@Getter
public class RestExceptionResponseDto {
    private final HttpStatus status;
    private final String message;

    public RestExceptionResponseDto(BusinessException e) {
        this.status = e.getStatus();
        this.message = e.getMessage();
    }
}
