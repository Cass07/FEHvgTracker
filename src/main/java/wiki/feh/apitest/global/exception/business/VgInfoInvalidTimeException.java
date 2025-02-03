package wiki.feh.apitest.global.exception.business;

import org.springframework.http.HttpStatus;

public class VgInfoInvalidTimeException extends BusinessException {
    public VgInfoInvalidTimeException() {
        super(HttpStatus.BAD_REQUEST, "해당 투표대전이 개최 전이거나 이미 종료되었습니다.");
    }
}
