package wiki.feh.apitest.global.exception.view;

public class VgRoundNotExistException extends ViewException {
    public VgRoundNotExistException() {
        super("오류가 발생했습니다.<br>해당 투표대전 라운드가 조회되지 않음.");
    }
}
