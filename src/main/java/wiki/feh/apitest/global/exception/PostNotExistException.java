package wiki.feh.apitest.global.exception;

public class PostNotExistException extends ViewException {
    public PostNotExistException() {
        super("해당 게시글이 존재하지 않습니다.");
    }
}
