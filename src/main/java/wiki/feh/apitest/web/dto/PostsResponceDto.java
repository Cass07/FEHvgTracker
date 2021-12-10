package wiki.feh.apitest.web.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.posts.Posts;

@Getter
public class PostsResponceDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponceDto(Posts entity)
    {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
