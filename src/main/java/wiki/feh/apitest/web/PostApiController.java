package wiki.feh.apitest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wiki.feh.apitest.service.posts.PostsService;
import wiki.feh.apitest.web.dto.PostsResponceDto;
import wiki.feh.apitest.web.dto.PostsSaveRequestDto;
import wiki.feh.apitest.web.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor
@RestController
public class PostApiController {
    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto)
    {
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto) {
        return postsService.update(id, requestDto);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id)
    {
        postsService.delete(id);
        return id;
    }
}
