package wiki.feh.apitest.event;

import wiki.feh.apitest.domain.posts.Posts;

public class PostEvent {
    private final String title;
    private final String content;
    private final String author;

    public PostEvent(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    @Override
    public String toString() {
        return "PostEvent{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    public Posts toEntity() {
        return new Posts(title, content, author);
    }
}
