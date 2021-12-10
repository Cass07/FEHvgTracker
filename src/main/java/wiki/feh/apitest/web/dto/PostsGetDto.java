package wiki.feh.apitest.web.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
public class PostsGetDto {

    private Long id;
    private String title;
    private String author;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostsGetDto(Posts entity)
    {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.content = entity.getContent();
        this.createdDate = entity.getCreateDate();
        this.modifiedDate = entity.getModifiedDate();
    }
}
