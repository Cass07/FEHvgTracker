package wiki.feh.apitest.controller.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostsGetWithPicDto {

    private final long id;
    private final String title;
    private final String content;
    private final String author;
    private final String picture;
    private final String name;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public PostsGetWithPicDto(long id, String title, String content, String author, String picture, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.picture = picture;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
