package wiki.feh.apitest.controller.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
public class PostsGetDto {

    private final long id;
    private final String title;
    private final String author;
    private final String content;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public PostsGetDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.content = entity.getContent();
        this.createdDate = entity.getCreateDate();
        this.modifiedDate = entity.getModifiedDate();
    }
}
