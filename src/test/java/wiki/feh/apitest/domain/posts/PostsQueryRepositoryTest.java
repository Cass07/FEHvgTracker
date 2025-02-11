package wiki.feh.apitest.domain.posts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.dto.PostsGetWithPicDto;
import wiki.feh.apitest.global.config.TestQueryDslConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Import({TestQueryDslConfig.class, PostsQueryRepository.class})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostsQueryRepositoryTest {

    @Autowired
    private PostsQueryRepository postsQueryRepository;

    @DisplayName("게시글 모두 순서대로 조회 테스트")
    @Test
    void findAllDesc() {
        // when
        List<Posts> postsList = postsQueryRepository.findAllDecs();

        // then
        assertNotNull(postsList);
        assertFalse(postsList.isEmpty());
    }

    @DisplayName("게시글 제목으로 조회 테스트")
    @Test
    void findByTitle() {
        // given
        String title = "테스트 제목";

        // when
        List<Posts> postsList = postsQueryRepository.findByTitle(title);

        // then
        assertNotNull(postsList);
        assertFalse(postsList.isEmpty());
        assertEquals(title, postsList.getFirst().getTitle());
    }

    @DisplayName("게시글 조회 시 이미지 포함 테스트")
    @Test
    void getPostsWithPicture() {
        // given
        long id = 1L;

        // when
        List<PostsGetWithPicDto> postsList = postsQueryRepository.getPostsWithPicture(id);

        // then
        assertNotNull(postsList);
        assertFalse(postsList.isEmpty());
        assertEquals(id, postsList.getFirst().getId());
        assertFalse(postsList.getFirst().getPicture().isEmpty());
    }

}