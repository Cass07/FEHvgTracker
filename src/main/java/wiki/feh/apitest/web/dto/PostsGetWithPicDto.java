package wiki.feh.apitest.web.dto;

import com.querydsl.core.Tuple;
import lombok.Getter;
import wiki.feh.apitest.domain.posts.Posts;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static wiki.feh.apitest.domain.posts.QPosts.posts;
import static wiki.feh.apitest.domain.user.QUser.user;

@Getter
public class PostsGetWithPicDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private String picture;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostsGetWithPicDto(Long id, String title, String content, String author, String picture, String name, LocalDateTime createdDate, LocalDateTime modifiedDate)
    {
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
