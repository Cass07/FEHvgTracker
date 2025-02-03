package wiki.feh.apitest.global.exception.business;

import org.springframework.http.HttpStatus;

public class VgTermException extends BusinessException {
    public VgTermException() {
        super(HttpStatus.BAD_REQUEST, "VG 중간이라 데이터가 없습니다.");
    }
}
