package wiki.feh.apitest.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import wiki.feh.apitest.domain.posts.Posts;
import wiki.feh.apitest.domain.posts.PostsRepository;
import wiki.feh.apitest.service.posts.PostsService;
import wiki.feh.apitest.web.dto.PostsResponceDto;
import wiki.feh.apitest.web.dto.PostsSaveRequestDto;
import wiki.feh.apitest.web.dto.PostsUpdateRequestDto;

@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yaml"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@WebMvcTest(controllers = PostApiController.class)
public class PostsApiControllerTest {

	private final int port = 8080;

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private MockMvc mockMvc;

	@MockBean(name = "postsRepository")
	private PostsRepository postsRepository;

	@MockBean(name = "postsService")
	private PostsService postsService;

	@BeforeAll
	public void setup() {
	}

	@Test
	@WithMockUser(roles = "USER", username = "name1", authorities = {"ROLE_USER"})
	public void Posts_post() throws Exception {
		// given
		String title = "title";
		String content = "content";
		PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
			.title(title)
			.content(content)
			.author("author")
			.build();

		String url = "http://localhost:" + port + "/api/v1/posts";

		// when
		ResultActions resultActions = mockMvc.perform(post(url)
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(requestDto))
			.with(csrf()));

		//then
		resultActions.andExpect(status().isOk());
	}

	@WithMockUser(roles = "USER", username = "name1", authorities = {"ROLE_USER"})
	@Test
	public void Posts_edit() throws Exception {
		// given
		String title = "title5";
		String content = "content5";

		Posts savedPosts = Posts.builder()
			.title(title)
			.content(content)
			.author("name1")
			.build();

		long updateId = 1L;
		String expectedTitle = "title2";
		String expectedContent = "content2";

		PostsResponceDto postsResponceDto = new PostsResponceDto(savedPosts);

		given(postsService.findbyId(1L)).willReturn(postsResponceDto);

		PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
			.title(expectedTitle)
			.content(expectedContent)
			.build();

		String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

		// when
		ResultActions resultActions = mockMvc.perform(put(url)
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(requestDto))
			.with(csrf()));

		// then
		resultActions.andExpect(status().isOk());
	}
}
