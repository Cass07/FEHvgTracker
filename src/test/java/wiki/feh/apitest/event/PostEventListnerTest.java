package wiki.feh.apitest.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
@RecordApplicationEvents
class PostEventListnerTest {

    @MockBean
    private PostEventListner postEventListner;

    @Autowired
    private ApplicationEventPublisher testEventPublisher;

    @DisplayName("postEvent 발행 시 리스너 테스트")
    @Test
    void postEvent() {
        // given
        PostEvent event = new PostEvent("test", "test", "test");

        // when
        testEventPublisher.publishEvent(event);

        // then
        verify(postEventListner).postEvent(any());
    }

}