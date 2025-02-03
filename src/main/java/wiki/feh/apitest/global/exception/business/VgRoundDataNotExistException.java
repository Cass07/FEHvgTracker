package wiki.feh.apitest.global.exception.business;

import org.springframework.http.HttpStatus;

public class VgRoundDataNotExistException extends BusinessException {
    public VgRoundDataNotExistException() {
        super(HttpStatus.BAD_REQUEST, "조회된 VG 라운드 데이터가 없습니다.");
    }
}
