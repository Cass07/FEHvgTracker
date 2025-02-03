package wiki.feh.apitest.facade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.controller.dto.VgDataGetDto;
import wiki.feh.apitest.domain.vgdata.VgData;
import wiki.feh.apitest.global.exception.business.VgInfoNotExistException;
import wiki.feh.apitest.global.exception.business.VgRoundDataNotExistException;
import wiki.feh.apitest.service.posts.PostsService;
import wiki.feh.apitest.service.vgdata.VgDataService;
import wiki.feh.apitest.service.vginfo.VgInfoService;
import wiki.feh.apitest.util.VgDataCrawl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class VgDataFacadeTest {
    @InjectMocks
    private VgDataFacade vgDataFacade;

    @Mock
    private VgDataService vgDataService;

    @Mock
    private VgInfoService vgInfoService;

    @Mock
    private PostsService postsService;

    @Mock
    private VgDataCrawl vgDataCrawl;

    @DisplayName("FirstVgData 조회 - vgNum, round, index")
    @Test
    void getFirstVgDataByNumRoundTour() {
        // given
        int vgNumber = 1;
        int roundNumber = 1;
        int tournamentIndex = 1;

        VgData data = VgData.builder()
                .vgNumber(vgNumber)
                .team1Score("1")
                .team2Score("2")
                .team1Index(1)
                .team2Index(2)
                .roundNumber(roundNumber)
                .tournamentIndex(tournamentIndex)
                .timeIndex(1).build();

        doReturn(Optional.of(data)).when(vgDataService).getFirstVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // when
        VgDataGetDto dto = vgDataFacade.getFirstVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // then
        assertEquals(dto.getVgNumber(), vgNumber);
    }

    @DisplayName("FirstVgData 조회 - 없음 - vgNum, round, index")
    @Test
    void getFirstVgDataByNumRoundTour_NotExist() {
        // given
        int vgNumber = 1;
        int roundNumber = 1;
        int tournamentIndex = 1;

        // when
        assertThrows(VgRoundDataNotExistException.class, () -> vgDataFacade.getFirstVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex));
    }

    @DisplayName("updateVgData - VgInfo 없음")
    @Test
    void updateVgData_NoVgInfo() {
        /**
         * TODO
         * return; 대신 RuntimeException을 던지도록 수정
         */

        // given
        doReturn(Optional.empty()).when(vgInfoService).getLatestVgInfo();

        // when
        assertThrows(VgInfoNotExistException.class, () -> vgDataFacade.updateVgData());
    }

}