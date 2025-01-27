package wiki.feh.apitest.global.exception;

import org.springframework.http.HttpStatus;

public class VgDataNotExistException extends BusinessException {
    public VgDataNotExistException() {
        super(HttpStatus.BAD_REQUEST, "조회된 VG 데이터가 없습니다.");
    }
}
