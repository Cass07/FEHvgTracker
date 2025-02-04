package wiki.feh.apitest.facade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.domain.vgdata.VgData;
import wiki.feh.apitest.domain.vginfo.VgInfo;
import wiki.feh.apitest.dto.VgDataGetDto;
import wiki.feh.apitest.global.exception.business.*;
import wiki.feh.apitest.service.posts.PostsService;
import wiki.feh.apitest.service.vgdata.VgDataService;
import wiki.feh.apitest.service.vginfo.VgInfoService;
import wiki.feh.apitest.util.VgDataCrawl;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class VgDataFacadeTest {
    @Spy
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

    @Mock
    private Clock clock;

    private final Clock STATIC_CLOCK = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("Asia/Seoul"));

    private static final List<Map<String, String>> VGDATALIST_4 = List.of(
            Map.of("team1Score", "1", "team2Score", "2", "team1Index", "1", "team2Index", "2"),
            Map.of("team1Score", "3", "team2Score", "4", "team1Index", "3", "team2Index", "4"),
            Map.of("team1Score", "5", "team2Score", "6", "team1Index", "5", "team2Index", "6"),
            Map.of("team1Score", "7", "team2Score", "8", "team1Index", "7", "team2Index", "8")
    );

    private static final List<Map<String, String>> VGDATALIST_6 = List.of(
            Map.of("team1Score", "1", "team2Score", "2", "team1Index", "1", "team2Index", "2"),
            Map.of("team1Score", "3", "team2Score", "4", "team1Index", "3", "team2Index", "4"),
            Map.of("team1Score", "5", "team2Score", "6", "team1Index", "5", "team2Index", "6"),
            Map.of("team1Score", "7", "team2Score", "8", "team1Index", "7", "team2Index", "8"),
            Map.of("team1Score", "1", "team2Score", "3", "team1Index", "1", "team2Index", "3"),
            Map.of("team1Score", "5", "team2Score", "7", "team1Index", "5", "team2Index", "7")
    );

    private static final List<Map<String, String>> VGDATALIST_7 = List.of(
            Map.of("team1Score", "1", "team2Score", "2", "team1Index", "1", "team2Index", "2"),
            Map.of("team1Score", "3", "team2Score", "4", "team1Index", "3", "team2Index", "4"),
            Map.of("team1Score", "5", "team2Score", "6", "team1Index", "5", "team2Index", "6"),
            Map.of("team1Score", "7", "team2Score", "8", "team1Index", "7", "team2Index", "8"),
            Map.of("team1Score", "1", "team2Score", "3", "team1Index", "1", "team2Index", "3"),
            Map.of("team1Score", "5", "team2Score", "7", "team1Index", "5", "team2Index", "7"),
            Map.of("team1Score", "1", "team2Score", "7", "team1Index", "1", "team2Index", "7")
    );

    void mockStaticClock() {
        doReturn(STATIC_CLOCK.instant()).when(clock).instant();
        doReturn(STATIC_CLOCK.getZone()).when(clock).getZone();
    }

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
        // given
        mockStaticClock();
        doReturn(Optional.empty()).when(vgInfoService).getLatestVgInfo();

        // when
        assertThrows(VgInfoNotExistException.class, () -> vgDataFacade.updateVgData());
    }

    @DisplayName("updateVgData - VgInfo 시간 불일치")
    @Test
    void updateVgData_InvalidTime() {
        // given
        mockStaticClock();
        LocalDate startTime = LocalDate.now(STATIC_CLOCK).minusDays(10);
        VgInfo vgInfo = VgInfo.builder().vgNumber(1).vgStartDate(startTime).build();
        doReturn(Optional.of(vgInfo)).when(vgInfoService).getLatestVgInfo();

        // when
        assertThrows(VgInfoInvalidTimeException.class, () -> vgDataFacade.updateVgData());
    }

    @DisplayName("updateVgData - VgData 이미 존재")
    @Test
    void updateVgData_AlreadyExist() {
        // given
        mockStaticClock();
        LocalDate startTime = LocalDate.now(STATIC_CLOCK).minusDays(1);
        VgInfo vgInfo = VgInfo.builder().vgNumber(1).vgStartDate(startTime).build();

        doReturn(Optional.of(vgInfo)).when(vgInfoService).getLatestVgInfo();
        doReturn(Optional.of(new VgData())).when(vgDataService).getVgDataByNumRoundTourTimeIndex(1, 1, 1, 17);

        // when
        assertThrows(VgDataAlreadyExistException.class, () -> vgDataFacade.updateVgData());
    }

    @DisplayName("updateVgData - VgData 저장 - vgDataCrawl 결과 오류")
    @Test
    void updateVgData_Save() {
        // given
        mockStaticClock();
        LocalDate startTime = LocalDate.now(STATIC_CLOCK).minusDays(1);
        VgInfo vgInfo = VgInfo.builder().vgNumber(1).vgStartDate(startTime).build();

        doReturn(Optional.of(vgInfo)).when(vgInfoService).getLatestVgInfo();
        doReturn(Optional.empty()).when(vgDataService).getVgDataByNumRoundTourTimeIndex(1, 1, 1, 17);
        doReturn(List.of()).when(vgDataCrawl).getMapListByCrawl(1);

        // when
        assertThrows(VgDataNotExistException.class, () -> vgDataFacade.updateVgData());
    }

    private static Stream<Arguments> vgDataListProvider() {
        return Stream.of(
                Arguments.of(VGDATALIST_4, 1),
                Arguments.of(VGDATALIST_6, 2),
                Arguments.of(VGDATALIST_7, 3)
        );
    }

    private static Stream<Arguments> vgDataListProviderRound2() {
        return Stream.of(
                Arguments.of(VGDATALIST_6, 1),
                Arguments.of(VGDATALIST_7, 2)
        );
    }

    @DisplayName("updateVgData - VgData 저장")
    @ParameterizedTest
    @MethodSource("vgDataListProvider")
    void updateVgData_Save(List<Map<String, String>> vgDataList, int round) {
        // given
        mockStaticClock();
        LocalDate startTime = LocalDate.now(STATIC_CLOCK).minusDays(round * 2L - 1);
        VgInfo vgInfo = VgInfo.builder().vgNumber(1).vgStartDate(startTime).build();

        doReturn(Optional.of(vgInfo)).when(vgInfoService).getLatestVgInfo();
        doReturn(Optional.empty()).when(vgDataService).getVgDataByNumRoundTourTimeIndex(1, round, 1, 17);
        doReturn(vgDataList).when(vgDataCrawl).getMapListByCrawl(1);
        doNothing().when(vgDataService).saveAll(any());

        // when
        vgDataFacade.updateVgData();

        // then
        verify(vgDataService).saveAll(any());
    }

    @DisplayName("updateVgData - VgData 저장 - timediff 48")
    @ParameterizedTest
    @MethodSource("vgDataListProviderRound2")
    void updateVgData_Save_TimeDiff_48(List<Map<String, String>> vgDataList, int round) {
        // given
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 3, 16, 1);
        Clock nowClock48 = Clock.fixed(currentTime.atZone(ZoneId.of("Asia/Seoul")).toInstant(), ZoneId.of("Asia/Seoul"));
        doReturn(nowClock48.instant()).when(clock).instant();
        doReturn(nowClock48.getZone()).when(clock).getZone();
        LocalDate startTime = LocalDate.now(nowClock48).minusDays(round * 2L);

        VgInfo vgInfo = VgInfo.builder().vgNumber(1).vgStartDate(startTime).build();

        doReturn(Optional.of(vgInfo)).when(vgInfoService).getLatestVgInfo();
        doReturn(Optional.empty()).when(vgDataService).getVgDataByNumRoundTourTimeIndex(1, round, 1, 48);
        doReturn(vgDataList).when(vgDataCrawl).getMapListByCrawl(1);
        doNothing().when(vgDataService).saveAll(any());

        // when
        vgDataFacade.updateVgData();

        // then
        verify(vgDataService).saveAll(any());
    }

}