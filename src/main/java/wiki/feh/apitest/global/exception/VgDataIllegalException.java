package wiki.feh.apitest.global.exception;

public class VgDataIllegalException extends ViewException {
    public VgDataIllegalException() {
        super("오류가 발생했습니다.<br>해당 투표대전의 조회된 라운드 데이터 값에 오류가 있음.");
    }
}
