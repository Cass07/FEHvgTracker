package wiki.feh.apitest.domain.posts;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import wiki.feh.apitest.global.config.TestQueryDslConfig;

@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yaml"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({TestQueryDslConfig.class, PostsQueryRepository.class})
@DataJpaTest
public class PostsRepositoryTest {

	@Autowired
	PostsRepository postsRepository;

	@Autowired
	PostsQueryRepository postsQueryRepository;

	@AfterAll
	public void cleanup() {
		postsRepository.deleteAll();
	}

	@Test
	public void post_get() {
		String title = "테스트 게시물 2";
		String content = "테스트 본문 2";

		postsRepository.save(Posts.builder()
			.title(title)
			.content(content)
			.author("testAuthor")
			.build());

		List<Posts> postsList = postsRepository.findAll();

		Posts posts = postsList.get(postsList.size() - 1);
		assertThat(posts.getTitle()).isEqualTo(title);
		assertThat(posts.getContent()).isEqualTo(content);
	}

	@Test
	public void post_get_querydsl() {
		String title = "테스트 게시물";
		String content = "테스트 본문";

		postsRepository.save(Posts.builder()
			.title(title)
			.content(content)
			.author("testAuthor")
			.build());

		List<Posts> postsList = postsQueryRepository.findByTitle(title);

		Posts posts = postsList.get(0);
		assertThat(posts.getTitle()).isEqualTo(title);
		assertThat(posts.getContent()).isEqualTo(content);
	}

}
