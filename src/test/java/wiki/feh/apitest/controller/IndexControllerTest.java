package wiki.feh.apitest.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import wiki.feh.apitest.domain.user.Role;
import wiki.feh.apitest.domain.user.User;
import wiki.feh.apitest.global.config.auth.dto.SessionUser;
import wiki.feh.apitest.global.exception.PostNotExistException;
import wiki.feh.apitest.global.exception.PostNotOwnedException;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class IndexControllerTest {

    private MockMvc mockMvc;
    protected MockHttpSession session;

    @BeforeEach
    public void setup(WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .apply(springSecurity())
                .build();
    }

    @DisplayName("Board 메인 페이지 로딩")
    @WithMockUser(roles = "ADMIN", username = "admin")
    @Test
    public void Mainpage_loading() throws Exception {
        // given
        String url = "/admin/board/";
        String title = "로그 게시판";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .with(csrf()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(title)));
    }

    @DisplayName("board 메인 페이지 page 로딩")
    @WithMockUser(roles = "ADMIN", username = "admin")
    @Test
    public void Mainpage_page_loading() throws Exception {
        // given
        String url = "/admin/board/page/1";
        String title = "로그 게시판";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .with(csrf()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(title)));
    }

    @DisplayName("Board 포스트 페이지 권한 없음")
    @WithMockUser(roles = "GUEST", username = "user")
    @Test
    public void Mainpage_no_auth() throws Exception {
        // given
        String url = "/admin/board/posts/1";
        String title = "로그 게시판";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .with(csrf()));

        // then
        resultActions.andExpect(status().isForbidden());
    }

    @DisplayName("Board 포스트 페이지 권한 있음")
    @WithMockUser(roles = "ADMIN", username = "admin")
    @Test
    public void Mainpage_auth() throws Exception {
        // given
        String url = "/admin/board/posts/1";
        String title = "글 내용 보기";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .with(csrf()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(title)));
    }

    @DisplayName("Board 포스트 존재하지 않음")
    @WithMockUser(roles = "ADMIN", username = "admin")
    @Test
    public void Mainpage_no_posts() throws Exception {
        // given
        String url = "/admin/board/posts/-1";
        String title = new PostNotExistException().getMessage();

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .with(csrf()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(title)));
    }

    @DisplayName("Board 포스트 저장 페이지 로딩")
    @WithMockUser(roles = "ADMIN", username = "admin")
    @Test
    public void Mainpage_posts_save() throws Exception {
        // given
        String url = "/admin/board/posts/save";
        String title = "글 등록";
        SessionUser user = new SessionUser(new User("admin", "test@testmail.com", "test", Role.ADMIN));
        session = new MockHttpSession();
        session.setAttribute("user", user);


        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .session(session)
                .with(csrf()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(title)));
    }

    @DisplayName("Board 포스트 수정 페이지 로딩")
    @WithMockUser(roles = "ADMIN", username = "admin")
    @Test
    public void Mainpage_posts_update() throws Exception {
        // given
        String url = "/admin/board/posts/update/1";
        String title = "글 수정하기";
        String board = "테스트 제목2";

        SessionUser user = new SessionUser(new User("admin", "test@testmail.com", "test", Role.ADMIN));
        session = new MockHttpSession();
        session.setAttribute("user", user);

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .session(session)
                .with(csrf()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(title)))
                .andExpect(content().string(org.hamcrest.Matchers.containsString(board)));
    }

    @DisplayName("Board 포스트 수정 페이지 자신이 쓴 글이 아님")
    @WithMockUser(roles = "ADMIN", username = "admin1234")
    @Test
    public void Mainpage_posts_update_no_auth() throws Exception {
        // given
        String url = "/admin/board/posts/update/1";
        String board = new PostNotOwnedException().getMessage();

        SessionUser user = new SessionUser(new User("admin1234", "test1234@testmail.com", "test", Role.ADMIN));
        session = new MockHttpSession();
        session.setAttribute("user", user);

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .session(session)
                .with(csrf()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(board)));
    }

    @DisplayName("vgInfo 세팅 페이지")
    @WithMockUser(roles = "ADMIN", username = "admin")
    @Test
    public void Mainpage_vginfo() throws Exception {
        // given
        String url = "/admin/vginfo";
        String title = "투표대전 데이터 세팅";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .with(csrf()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(title)));
    }
}
