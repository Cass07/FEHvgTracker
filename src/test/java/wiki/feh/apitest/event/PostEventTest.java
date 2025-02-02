package wiki.feh.apitest.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.domain.posts.Posts;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PostEventTest {

    @DisplayName("PostEvent toString")
    @Test
    void postEventToString() {
        // given
        String title = "titleTest";
        String content = "contentTest";
        String author = "authorTest";
        PostEvent event = new PostEvent(title, content, author);

        // when
        String result = event.toString();

        // then
        String expected = "PostEvent{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                '}';
        assertEquals(expected, result);
    }

    @DisplayName("PostEvent toEntity")
    @Test
    void postEventToEntity() {
        // given
        String title = "titleTest";
        String content = "contentTest";
        String author = "authorTest";
        PostEvent event = new PostEvent(title, content, author);

        // when
        Posts result = event.toEntity();

        // then
        assertEquals(title, result.getTitle());
        assertEquals(content, result.getContent());
        assertEquals(author, result.getAuthor());
    }
}