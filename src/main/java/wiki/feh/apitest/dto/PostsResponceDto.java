package wiki.feh.apitest.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.posts.Posts;

@Getter
public class PostsResponceDto {
    private final long id;
    private final String title;
    private final String content;
    private final String author;

    public PostsResponceDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
