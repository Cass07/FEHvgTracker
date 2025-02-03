package wiki.feh.apitest.global.exception.business;

import org.springframework.http.HttpStatus;

public class VgDataAlreadyExistException extends BusinessException {
    public VgDataAlreadyExistException() {
        super(HttpStatus.BAD_REQUEST, "해당 투표대전의 라운드 데이터가 이미 존재합니다.");
    }
}
