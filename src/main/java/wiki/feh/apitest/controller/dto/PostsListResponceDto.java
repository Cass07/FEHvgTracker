package wiki.feh.apitest.controller.dto;

import lombok.Getter;
import wiki.feh.apitest.domain.posts.Posts;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class PostsListResponceDto {
    private final long id;
    private final String title;
    private final String author;
    private final LocalDateTime modifiedDate;
    private final String modifiedDateString;

    public PostsListResponceDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
        this.modifiedDateString = this.modifiedDate.format(DateTimeFormatter.ofPattern("MM/dd HH:mm:ss"));
    }
}
