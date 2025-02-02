package wiki.feh.apitest.service.posts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.controller.dto.*;
import wiki.feh.apitest.domain.posts.Posts;
import wiki.feh.apitest.domain.posts.PostsQueryRepository;
import wiki.feh.apitest.domain.posts.PostsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PostsServiceTest {

    @InjectMocks
    private PostsService postsService;

    @Mock
    private PostsRepository postsRepository;

    @Mock
    private PostsQueryRepository postsQueryRepository;

    @DisplayName("PostsSaveRequestDto 저장")
    @Test
    void save() {
        // given
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();

        Posts entity = requestDto.toEntity();

        doReturn(entity).when(postsRepository).save(any(Posts.class));

        // when
        long saveId = postsService.save(requestDto);

        // then
        assert(saveId >= 0);
    }

    @DisplayName("Posts 저장")
    @Test
    void saveEntity() {
        // given
        Posts entity = Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();

        doReturn(entity).when(postsRepository).save(any(Posts.class));

        // when
        long saveId = postsService.save(entity);

        // then
        assert(saveId >= 0);
    }

    @DisplayName("Posts 수정 - PostsUpdateRequestDto")
    @Test
    void update() {
        // given
        Posts entity = Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();

        long updateId = 1;


        doReturn(Optional.of(entity)).when(postsRepository).findById(updateId);

        // when
        long resultId = postsService.update(updateId, PostsUpdateRequestDto.builder()
                .title("title2")
                .content("content2")
                .build());

        // then
        assertEquals(updateId, resultId);
    }

    @DisplayName("Posts 수정 - 조회된 Posts 없음")
    @Test
    void updateNull() {
        // given
        long updateId = 1;

        doReturn(Optional.empty()).when(postsRepository).findById(updateId);

        // when
        assertThrows(IllegalArgumentException.class, () -> postsService.update(updateId, PostsUpdateRequestDto.builder()
                .title("title2")
                .content("content2")
                .build()));
    }

    @DisplayName("id로 PostsResponce 조회")
    @Test
    void findById() {
        // given
        Posts entity = Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();

        long findId = 1;

        doReturn(Optional.of(entity)).when(postsRepository).findById(findId);

        // when
        PostsResponceDto resultDto = postsService.findById(findId);

        // then
        assertEquals(entity.getTitle(), resultDto.getTitle());
        assertEquals(entity.getContent(), resultDto.getContent());
        assertEquals(entity.getAuthor(), resultDto.getAuthor());
    }

    @DisplayName("id로 PostsResponce 조회 - 조회된 Posts 없음")
    @Test
    void findByIdNull() {
        // given
        long findId = 1;

        doReturn(Optional.empty()).when(postsRepository).findById(findId);

        // when
        assertThrows(IllegalArgumentException.class, () -> postsService.findById(findId));
    }

    @DisplayName("page로 모든 PostsListResponce 조회")
    @Test
    void findAllDescPage() {
        // given
        Posts entity = Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();

        Pageable pageable = PageRequest.of(0, 20);

        Page<Posts> pageEntity = new PageImpl<>(List.of(entity), pageable, 1);

        doReturn(pageEntity).when(postsRepository).findAll(any(Pageable.class));

        // when
        Page<PostsListResponceDto> resultPage = postsService.findAllDescPage(0);
        assertEquals(1, resultPage.getTotalElements());
    }

    @DisplayName("모든 PostsListResponce 조회")
    @Test
    void findAllDesc() {
        // given
        Posts entity = Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();

        doReturn(List.of(entity)).when(postsQueryRepository).findAllDecs();

        // when
        List<PostsListResponceDto> result = postsService.findAllDesc();

        // then
        assertEquals(1, result.size());
    }

    @DisplayName("id로 PostsGetDto 조회")
    @Test
    void getById() {
        // given
        Posts entity = Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();

        long findId = 1;

        doReturn(Optional.of(entity)).when(postsRepository).findById(findId);

        // when
        PostsResponceDto resultDto = postsService.findById(findId);

        // then
        assertEquals(entity.getTitle(), resultDto.getTitle());
        assertEquals(entity.getContent(), resultDto.getContent());
        assertEquals(entity.getAuthor(), resultDto.getAuthor());
    }

    @DisplayName("id로 PostsGetWithPicDto 조회")
    @Test
    void getByIdWithPic() {
        // given
        Posts entity = Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();

        long findId = 1;

        LocalDateTime createdDate = LocalDateTime.of(2021, 1, 1, 1, 1, 1);
        LocalDateTime modifiedDate = LocalDateTime.of(2021, 2, 1, 1, 1, 1);


        doReturn(List.of(new PostsGetWithPicDto(findId, "title", "content", "author", "pictureURL", "name" , createdDate, modifiedDate)))
                .when(postsQueryRepository).getPostsWithPicture(findId);

        // when
        PostsGetWithPicDto resultDto = postsService.getByIdWithPic(findId);

        // then
        assertEquals(entity.getTitle(), resultDto.getTitle());
        assertEquals(entity.getContent(), resultDto.getContent());
        assertEquals(entity.getAuthor(), resultDto.getAuthor());
    }

    @DisplayName("id조회 PostsGetWithPicDto 없음")
    @Test
    void getByIdWithPicNull() {
        // given
        long findId = 1;

        doReturn(List.of()).when(postsQueryRepository).getPostsWithPicture(findId);

        // when
        PostsGetWithPicDto resultDto = postsService.getByIdWithPic(findId);

        // then
        assertNull(resultDto);
    }

    @DisplayName("id로 Posts 삭제")
    @Test
    void delete() {
        // given
        Posts entity = Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();

        long deleteId = 1;

        doReturn(Optional.of(entity)).when(postsRepository).findById(deleteId);

        // when
        postsService.delete(deleteId);

        // then
        verify(postsRepository).delete(entity);
    }

}