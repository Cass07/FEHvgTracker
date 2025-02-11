package wiki.feh.apitest.global.exception.business;

import org.springframework.http.HttpStatus;

public class VgInfoNotExistException extends BusinessException {
    public VgInfoNotExistException() {
        super(HttpStatus.BAD_REQUEST, "해당 투표대전 정보가 존재하지 않습니다.");
    }
}
