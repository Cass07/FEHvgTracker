package wiki.feh.apitest.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wiki.feh.apitest.global.exception.business.BusinessException;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<RestExceptionResponseDto> handleBusinessException(BusinessException e) {
        log.error(e.getMessage());
        return response(e);
    }

    private ResponseEntity<RestExceptionResponseDto> response(BusinessException e) {
        return ResponseEntity.status(e.getStatus()).body(new RestExceptionResponseDto(e));
    }
}
