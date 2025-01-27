package wiki.feh.apitest.domain.posts;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wiki.feh.apitest.domain.BaseTimeEntity;

@Getter
@NoArgsConstructor
@Table(name = "`posts`")
@Entity
public class Posts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 255)
    private String author;

    @Builder
    public Posts(String title, String content, String author)
    {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content)
    {
        this.title = title;
        this.content = content;
    }

}
