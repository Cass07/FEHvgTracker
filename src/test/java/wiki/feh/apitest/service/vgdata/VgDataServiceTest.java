package wiki.feh.apitest.service.vgdata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.controller.dto.VgDataGetDto;
import wiki.feh.apitest.controller.dto.VgDataResultGetDto;
import wiki.feh.apitest.controller.dto.VgDataSaveDto;
import wiki.feh.apitest.domain.vgdata.VgData;
import wiki.feh.apitest.domain.vgdata.VgDataQueryRepository;
import wiki.feh.apitest.domain.vgdata.VgDataRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class VgDataServiceTest {
    @InjectMocks
    private VgDataService vgDataService;

    @Mock
    private VgDataRepository vgDataRepository;

    @Mock
    private VgDataQueryRepository vgDataQueryRepository;

    @DisplayName("VgDataSaveDto 저장")
    @Test
    void save() {
        // given
        VgDataSaveDto vgDataSaveDto = VgDataSaveDto.builder()
                .vgNumber(1)
                .team1Score("1")
                .team2Score("2")
                .team1Index(1)
                .team2Index(2)
                .roundNumber(1)
                .tournamentIndex(1)
                .timeIndex(1).build();

        VgData entity = vgDataSaveDto.toEntity();

        doReturn(entity).when(vgDataRepository).save(any(VgData.class));

        // when
        long saveId = vgDataService.save(vgDataSaveDto);

        // then
        assert(saveId >= 0);
    }

    @DisplayName("VgDataSaveDto 리스트 저장")
    @Test
    void saveAll() {
        // given
        VgDataSaveDto vgDataSaveDto = VgDataSaveDto.builder()
                .vgNumber(1)
                .team1Score("1")
                .team2Score("2")
                .team1Index(1)
                .team2Index(2)
                .roundNumber(1)
                .tournamentIndex(1)
                .timeIndex(1).build();

        List<VgData> entityList = List.of(vgDataSaveDto.toEntity());

        doReturn(entityList).when(vgDataRepository).saveAll(Mockito.anyList());

        // when
        vgDataService.saveAll(List.of(vgDataSaveDto));

        // then
        verify(vgDataRepository).saveAll(anyList());
    }

    @DisplayName("VgData 리스트 조회 - vgNumber, roundNumber, tournamentIndex")
    @Test
    void getVgDataListByNumRoundTour() {
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

        doReturn(List.of(data)).when(vgDataQueryRepository).getVgDataListByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // when
        List<VgDataGetDto> vgDataList = vgDataService.getVgDataListByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // then
        assertNotNull(vgDataList);
        assertEquals(vgDataList.size(), 1);
    }

    @DisplayName("VgData 최신 데이터 조회 - vgNumber, roundNumber, tournamentIndex")
    @Test
    void getLatestVgDataNumRoundTour() {
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

        doReturn(data).when(vgDataQueryRepository).getLatestVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // when
        VgDataGetDto vgData = vgDataService.getLatestVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // then
        assertNotNull(vgData);
    }

    @DisplayName("VgData 최신 데이터 조회 - vgNumber, roundNumber, tournamentIndex - 데이터 없음")
    @Test
    void getLatestVgDataNumRoundTourNoData() {
        // given
        int vgNumber = 1;
        int roundNumber = 1;
        int tournamentIndex = 1;

        doReturn(null).when(vgDataQueryRepository).getLatestVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // when
        VgDataGetDto vgData = vgDataService.getLatestVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // then
        assertNull(vgData);
    }

    @DisplayName("VgData 조회 - vgNumber, roundNumber, tournamentIndex, timeIndex")
    @Test
    void getVgDataNumRoundTourTimeIndex() {
        // given
        int vgNumber = 1;
        int roundNumber = 1;
        int tournamentIndex = 1;
        int timeIndex = 1;

        VgData data = VgData.builder()
                .vgNumber(vgNumber)
                .team1Score("1")
                .team2Score("2")
                .team1Index(1)
                .team2Index(2)
                .roundNumber(roundNumber)
                .tournamentIndex(tournamentIndex)
                .timeIndex(timeIndex).build();

        doReturn(Optional.of(data)).when(vgDataRepository).findByVgNumberAndRoundNumberAndTournamentIndexAndTimeIndex(vgNumber, roundNumber, tournamentIndex, timeIndex);

        // when
        VgData vgData = vgDataService.getVgDataByNumRoundTourTimeIndex(vgNumber, roundNumber, tournamentIndex, timeIndex);

        // then
        assertNotNull(vgData);
        assertEquals(vgData.getVgNumber(), vgNumber);
    }

    @DisplayName("VgData 최초 데이터 조회 - vgNumber, roundNumber, tournamentIndex")
    @Test
    void getFirstVgDataNumRoundTour() {
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

        doReturn(data).when(vgDataQueryRepository).getFirstVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // when
        VgDataGetDto vgData = vgDataService.getFirstVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // then
        assertNotNull(vgData);
        assertEquals(vgNumber, vgData.getVgNumber());
    }

    @DisplayName("VgData 최초 데이터 조회 - vgNumber, roundNumber, tournamentIndex - 데이터 없음")
    @Test
    void getFirstVgDataNumRoundTourNoData() {
        // given
        int vgNumber = 1;
        int roundNumber = 1;
        int tournamentIndex = 1;

        doReturn(null).when(vgDataQueryRepository).getFirstVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // when
        VgDataGetDto vgData = vgDataService.getFirstVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // then
        assertNull(vgData);
    }

    @DisplayName("전체 라운드 5시 VgData 리스트 조회 - vgNumber")
    @Test
    void getFirstVgDataListVgNumber() {
        // given
        int vgNumber = 1;

        VgData data = VgData.builder()
                .vgNumber(vgNumber)
                .team1Score("1")
                .team2Score("2")
                .team1Index(1)
                .team2Index(2)
                .roundNumber(1)
                .tournamentIndex(1)
                .timeIndex(1).build();

        doReturn(List.of(data)).when(vgDataQueryRepository).getFirstVgDataListByVgNumber(vgNumber);

        // when
        List<VgDataGetDto> vgDataList = vgDataService.getFirstVgDataListByVgNumber(vgNumber);

        // then
        assertNotNull(vgDataList);
        assertEquals(1, vgDataList.size());
    }

    @DisplayName("전체 라운드 5시 VgData 결과 리스트 조회 - vgNumber - VgDataResultGetDto")
    @Test
    void getFirstVgDataResultListVgNumber() {
        // given
        int vgNumber = 1;

        VgData data = VgData.builder()
                .vgNumber(vgNumber)
                .team1Score("1")
                .team2Score("2")
                .team1Index(1)
                .team2Index(2)
                .roundNumber(1)
                .tournamentIndex(1)
                .timeIndex(1).build();

        doReturn(List.of(data)).when(vgDataQueryRepository).getFirstVgDataListByVgNumber(vgNumber);

        // when
        List<VgDataResultGetDto> vgDataList = vgDataService.getFirstVgDataResultListByVgNumber(vgNumber);

        // then
        assertNotNull(vgDataList);
        assertEquals(1, vgDataList.size());
    }

    @DisplayName("전체 라운드 45시 VgData 결과 리스트 조회 - vgNumber")
    @Test
    void getLatestVgDataListVgNumber() {
        // given
        int vgNumber = 1;

        VgData data = VgData.builder()
                .vgNumber(vgNumber)
                .team1Score("1")
                .team2Score("2")
                .team1Index(1)
                .team2Index(2)
                .roundNumber(1)
                .tournamentIndex(1)
                .timeIndex(45).build();

        doReturn(List.of(data)).when(vgDataQueryRepository).getLatestVgDataListByVgNumber(vgNumber);

        // when
        List<VgDataResultGetDto> vgDataList = vgDataService.getLatestVgDataListByVgNumber(vgNumber);

        // then
        assertNotNull(vgDataList);
        assertEquals(1, vgDataList.size());
    }

    @DisplayName("특정 라운드 제일 최신시간의 전체데이터 VgData 리스트 조회 - vgNumber, roundNumber")
    @Test
    void getLatestVgDataListByVgNumberRound() {
        // given
        int vgNumber = 1;
        int roundNumber = 1;

        VgData data = VgData.builder()
                .vgNumber(vgNumber)
                .team1Score("1")
                .team2Score("2")
                .team1Index(1)
                .team2Index(2)
                .roundNumber(roundNumber)
                .tournamentIndex(1)
                .timeIndex(1).build();

        doReturn(List.of(data)).when(vgDataQueryRepository).getLatestVgDataListByVgNumberRound(vgNumber, roundNumber);

        // when
        List<VgDataGetDto> vgDataList = vgDataService.getLatestVgDataListByVgNumberRound(vgNumber, roundNumber);

        // then
        assertNotNull(vgDataList);
        assertEquals(1, vgDataList.size());
    }
}