package wiki.feh.apitest.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.domain.posts.Posts;
import wiki.feh.apitest.service.posts.PostsService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PostEventListenerTest {
    @InjectMocks
    private PostEventListener postEventListener;

    @Mock
    private PostsService postsService;

    @DisplayName("PostEvent 실행")
    @Test
    void postEvent() {
        // given
        PostEvent event = new PostEvent("test", "test", "test");

        // when
        postEventListener.postEvent(event);

        // then
        verify(postsService).save(any(Posts.class));
    }

}