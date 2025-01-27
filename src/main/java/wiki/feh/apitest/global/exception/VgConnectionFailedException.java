package wiki.feh.apitest.global.exception;

import org.springframework.http.HttpStatus;

public class VgConnectionFailedException extends BusinessException {
    public VgConnectionFailedException() {
        super(HttpStatus.BAD_REQUEST, "VG 서버와 연결이 실패했습니다.");
    }
}
