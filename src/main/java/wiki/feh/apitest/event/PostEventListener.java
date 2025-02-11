package wiki.feh.apitest.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import wiki.feh.apitest.service.posts.PostsService;

@Slf4j
@RequiredArgsConstructor
@Component
public class PostEventListener {
    private final PostsService postsService;

    @EventListener()
    public void postEvent(PostEvent postEvent) {
        log.info("PostEventListner postEvent");
        log.info(postEvent.toString());
        postsService.save(postEvent.toEntity());
    }
}
