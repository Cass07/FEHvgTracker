package wiki.feh.apitest.service.posts;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.domain.posts.Posts;
import wiki.feh.apitest.domain.posts.PostsQueryRepository;
import wiki.feh.apitest.domain.posts.PostsRepository;
import wiki.feh.apitest.controller.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    private final PostsQueryRepository postsQueryRepository;

    @Transactional
    public long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public long save(Posts entity) {
        return postsRepository.save(entity).getId();
    }

    @Transactional
    public long update(long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public PostsResponceDto findById(long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id = " + id));

        return new PostsResponceDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<PostsListResponceDto> findAllDescPage(int page) {
        return postsRepository.findAll(PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "id")))
                .map(PostsListResponceDto::new);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponceDto> findAllDesc() {
        return postsQueryRepository.findAllDecs().stream()
                .map(PostsListResponceDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostsGetDto getById(long id) {
        Posts entity = postsRepository.findById(id).orElse(null);

        if (entity == null)
            return null;
        return new PostsGetDto(entity);
    }

    @Transactional(readOnly = true)
    public PostsGetWithPicDto getByIdWithPic(long id) {
        List<PostsGetWithPicDto> entity = postsQueryRepository.getPostsWithPicture(id);
        if (!entity.isEmpty()) {
            return entity.getFirst();
        } else {
            return null;
        }
    }

    @Transactional
    public void delete(long id) {
        postsRepository.findById(id).ifPresent(postsRepository::delete);
    }

}
