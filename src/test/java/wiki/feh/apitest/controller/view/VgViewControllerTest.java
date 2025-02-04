package wiki.feh.apitest.controller.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;
import wiki.feh.apitest.dto.TeamDto;
import wiki.feh.apitest.dto.VgInfoGetDto;
import wiki.feh.apitest.dto.VgViewDto;
import wiki.feh.apitest.domain.vginfo.VgInfo;
import wiki.feh.apitest.facade.VgViewFacade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class VgViewControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private VgViewController vgViewController;

    @Mock
    private VgViewFacade vgViewFacade;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(vgViewController)
                .build();
    }

    @DisplayName("메인 페이지 로딩")
    @Test
    public void Mainpage_loading_1() throws Exception {
        // given
        String url = "/vg";

        VgInfoGetDto vgInfoEntity = new VgInfoGetDto(
                VgInfo.builder().
                        vgNumber(1).
                        vgTitle("title").
                        vgNumber(1).
                        vgStartDate(LocalDate.of(2025, 1, 1)).
                        team1Id("team1#팀1").
                        team2Id("team2#팀2").
                        team3Id("team3#팀3").
                        team4Id("team4#팀4").
                        team5Id("team5#팀5").
                        team6Id("team6#팀6").
                        team7Id("team7#팀7").
                        team8Id("team8#팀8").
                        build()
        );

        List<TeamDto> teamList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            teamList.add(new TeamDto(vgInfoEntity.getTeamIdbyIndex(i), i));
        }

        Map<String, String> viewModel = new HashMap<>(
                Map.of("round1StartNot", "1라운드 대진표",
                        "vgStartDateTimeStr", vgInfoEntity.getVgStartDate().atTime(16, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        "vgEndDateTimeStr", vgInfoEntity.getVgStartDate().atTime(13, 0).plusDays(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        )
        );


        VgViewDto vgViewDto = VgViewDto.builder()
                .viewString("vg-data-main")
                .vgInfoEntity(vgInfoEntity)
                .viewModel(viewModel)
                .currentRoundVgdata(null)
                .round1Vgdata(null)
                .round2Vgdata(null)
                .round3Vgdata(null)
                .teamList(teamList)
                .build();

        doReturn(vgViewDto).when(vgViewFacade).getVgMainbyid(-1);

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .with(csrf()))
                .andDo(print());

        // then
        ModelAndView modelAndView = resultActions.andReturn().getModelAndView();
        assertNotNull(modelAndView);

        ModelAndViewAssert.assertViewName(modelAndView, "vg-data-main");

    }


    /**
     * view 레이어가 원하는 값을 뱉었는지는 테스트하지 않고
     * controller가 알맞은 model을 받았는지만 테스트하면 될 것 같다
     *
     * JSP류의 테스트는 클라이언트 측 테스트 프레임워크를 이용하라고 한다
     */
}