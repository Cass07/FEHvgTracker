package wiki.feh.apitest.global.exception;

public class PostNotOwnedException extends ViewException {
    public PostNotOwnedException() {
        super("해당 게시글을 수정할 권한이 없습니다.");
    }
}
