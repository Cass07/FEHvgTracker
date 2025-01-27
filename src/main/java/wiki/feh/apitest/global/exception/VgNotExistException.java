package wiki.feh.apitest.global.exception;

public class VgNotExistException extends ViewException {
    public VgNotExistException() {
        super("오류가 발생했습니다.<br>해당 투표대전 조회되지 않음.");
    }
}
