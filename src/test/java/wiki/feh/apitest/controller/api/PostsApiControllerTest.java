package wiki.feh.apitest.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wiki.feh.apitest.dto.PostsSaveRequestDto;
import wiki.feh.apitest.dto.PostsUpdateRequestDto;
import wiki.feh.apitest.service.posts.PostsService;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Spring Security를 거치지 않는 Rest Controller Layer 테스트
 */
@ActiveProfiles("test")
@ExtendWith({MockitoExtension.class})
public class PostsApiControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private PostApiController postApiController;

	@Mock
	private PostsService postsService;


	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.standaloneSetup(postApiController)
				.build();
	}

	@DisplayName("포스트 등록")
	@WithMockUser(roles = "ADMIN", username = "name1")
	@Test
	public void Posts_post() throws Exception {
		// given
		String title = "title";
		String content = "content";
		PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
			.title(title)
			.content(content)
			.author("author")
			.build();

		String url = "/api/v1/posts";

		// when
		ResultActions resultActions = mockMvc.perform(post(url)
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(requestDto))
			.with(csrf()));

		//then
		resultActions.andExpect(status().isOk());
	}

	@DisplayName("포스트 수정")
	@WithMockUser(roles = "ADMIN", username = "name1")
	@Test
	public void Posts_edit() throws Exception {
		// given
		long updateId = 1L;
		String expectedTitle = "title2";
		String expectedContent = "content2";

		PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
			.title(expectedTitle)
			.content(expectedContent)
			.build();

		doReturn(updateId).when(postsService).update(eq(updateId), any(PostsUpdateRequestDto.class));

		String url = "/api/v1/posts/" + updateId;

		// when
		ResultActions resultActions = mockMvc.perform(put(url)
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(requestDto))
			.with(csrf()));

		// then
		resultActions.andExpect(status().isOk())
				.andExpect(content().string(String.valueOf(updateId)));

	}

	@DisplayName("포스트 삭제")
	@WithMockUser(roles = "ADMIN", username = "name1")
	@Test
	public void Posts_delete() throws Exception {
		// given
		long deleteId = 10L;

		String url = "/api/v1/posts/" + deleteId;

		doNothing().when(postsService).delete(deleteId);

		// when
		ResultActions resultActions = mockMvc.perform(delete(url)
			.with(csrf()));

		// then
		resultActions.andExpect(status().isOk())
				.andExpect(content().string(String.valueOf(deleteId)));
	}
}
