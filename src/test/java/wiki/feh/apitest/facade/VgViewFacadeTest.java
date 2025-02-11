package wiki.feh.apitest.facade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.domain.vgdata.VgData;
import wiki.feh.apitest.domain.vginfo.VgInfo;
import wiki.feh.apitest.dto.VgDataGetDto;
import wiki.feh.apitest.dto.VgDataResultGetDto;
import wiki.feh.apitest.dto.VgInfoGetDto;
import wiki.feh.apitest.dto.VgViewDto;
import wiki.feh.apitest.global.exception.view.VgDataIllegalException;
import wiki.feh.apitest.global.exception.view.VgNotExistException;
import wiki.feh.apitest.service.vgdata.VgDataService;
import wiki.feh.apitest.service.vginfo.VgInfoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class VgViewFacadeTest {
    @InjectMocks
    private VgViewFacade vgViewFacade;

    @Mock
    private VgDataService vgDataService;

    @Mock
    private VgInfoService vgInfoService;

    private static final VgInfo VG_INFO = VgInfo.builder()
            .vgStartDate(LocalDate.of(2021, 1, 1))
            .vgNumber(1)
            .vgTitle("title")
            .team1Id("team1#1")
            .team2Id("team2#2")
            .team3Id("team3#3")
            .team4Id("team4#4")
            .team5Id("team5#5")
            .team6Id("team6#6")
            .team7Id("team7#7")
            .team8Id("team8#8")
            .build();

    private static final List<VgDataResultGetDto> VGDATA_LIST_0 = List.of();

    private static final List<VgDataResultGetDto> VGDATA_LIST_4 = List.of(
            new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("3").team2Score("4").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("5").team2Score("6").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("7").team2Score("8").build())
    );

    private static final List<VgDataResultGetDto> VGDATA_LIST_6 = List.of(
            new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("3").team2Score("4").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("5").team2Score("6").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("7").team2Score("8").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("5").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("6").team2Score("8").build())
    );

    private static final List<VgDataResultGetDto> VGDATA_LIST_7 = List.of(
            new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("3").team2Score("4").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("5").team2Score("6").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("7").team2Score("8").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("5").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("6").team2Score("8").build()),
            new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("8").build())
    );

    @DisplayName("id로 VgInfoGetDto를 조회")
    @Test
    void getVgInfoById() {
        // given
        long id = 1L;
        doReturn(Optional.of(VG_INFO)).when(vgInfoService).findById(id);

        // when
        VgInfoGetDto vgInfoGetDto = vgViewFacade.getVgInfoById(id);

        // then
        assertNotNull(vgInfoGetDto);
        assertEquals(VG_INFO.getVgStartDate(), vgInfoGetDto.getVgStartDate());
        assertEquals(VG_INFO.getVgNumber(), vgInfoGetDto.getVgNumber());
    }

    @DisplayName("vgNumber, roundNumber, tournamentIndex로 VgDataGetDto를 조회")
    @Test
    void getFirstVgDataByNumRoundTour() {
        // given
        int vgNumber = 1;
        int roundNumber = 1;
        int tournamentIndex = 1;
        VgData vgData = VgData.builder()
                .team1Score("1")
                .team2Score("2")
                .team1Index(1)
                .team2Index(2)
                .build();
        doReturn(Optional.of(vgData)).when(vgDataService).getFirstVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // when
        VgDataGetDto vgDataGetDto = vgViewFacade.getFirstVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // then
        assertNotNull(vgDataGetDto);
        assertEquals(vgData.getTeam1Score(), vgDataGetDto.getTeam1Score());
        assertEquals(vgData.getTeam2Score(), vgDataGetDto.getTeam2Score());
    }

    @DisplayName("VgMain 조회 - id로 조회 - vgInfo 없음")
    @Test
    void getVgMainById_NoVgInfo() {
        // given
        long id = 1L;
        doReturn(Optional.empty()).when(vgInfoService).findById(id);

        // when
        assertThrows(VgNotExistException.class, () -> vgViewFacade.getVgMainByid(id));
    }

    @DisplayName("VgMain 조회 - id로 조회 - vgData 수가 이상함")
    @Test
    void getVgMainById_InvalidVgDataSize() {
        // given
        long id = 1L;
        doReturn(Optional.of(VG_INFO)).when(vgInfoService).findById(id);

        List<VgDataResultGetDto> vgDataList = List.of(
                new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build()),
                new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build()),
                new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build()),
                new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build()),
                new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build())
        );
        doReturn(vgDataList).when(vgDataService).getLatestVgDataListByVgNumber(VG_INFO.getVgNumber());

        // when
        assertThrows(VgDataIllegalException.class, () -> vgViewFacade.getVgMainByid(id));
    }

    private static Stream<Arguments> vgDataListProvider() {
        return Stream.of(
                Arguments.of(VGDATA_LIST_0, 1),
                Arguments.of(VGDATA_LIST_4, 2),
                Arguments.of(VGDATA_LIST_6, 3),
                Arguments.of(VGDATA_LIST_7, 0)
        );
    }

    @DisplayName("VgMain 조회 - id로 조회 - 정상")
    @ParameterizedTest
    @MethodSource("vgDataListProvider")
    void getVgMainById(List<VgDataResultGetDto> vgDataList, int round) {
        // given
        long id = 1L;
        doReturn(Optional.of(VG_INFO)).when(vgInfoService).findById(id);
        doReturn(vgDataList).when(vgDataService).getLatestVgDataListByVgNumber(VG_INFO.getVgNumber());

        // when
        VgViewDto vgViewDto = vgViewFacade.getVgMainByid(id);

        // then
        assertNotNull(vgViewDto);
        assertEquals("vg-data-main", vgViewDto.getViewString());
    }

    @DisplayName("VgFirst 조회 - id로 조회 - vgInfo 없음")
    @Test
    void getVgFirstById_NoVgInfo() {
        // given
        long id = 1L;
        doReturn(Optional.empty()).when(vgInfoService).findById(id);

        // when
        assertThrows(VgNotExistException.class, () -> vgViewFacade.getVgFirstById(id));
    }

    @DisplayName("VgFirst 조회 - id로 조회 - vgData 수가 이상함")
    @Test
    void getVgFirstById_InvalidVgDataSize() {
        // given
        long id = 1L;
        doReturn(Optional.of(VG_INFO)).when(vgInfoService).findById(id);

        List<VgDataResultGetDto> vgDataList = List.of(
                new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build()),
                new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build()),
                new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build()),
                new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build()),
                new VgDataResultGetDto(VgData.builder().team1Score("1").team2Score("2").build())
        );
        doReturn(vgDataList).when(vgDataService).getFirstVgDataResultListByVgNumber(VG_INFO.getVgNumber());

        // when
        assertThrows(VgDataIllegalException.class, () -> vgViewFacade.getVgFirstById(id));
    }


    @DisplayName("VgFirst 조회 - vg first 조회")
    @ParameterizedTest
    @MethodSource("vgDataListProvider")
    void getVgFirstById(List<VgDataResultGetDto> vgDataList, int round) {
        // given
        long id = 1L;
        doReturn(Optional.of(VG_INFO)).when(vgInfoService).findById(id);
        doReturn(vgDataList).when(vgDataService).getFirstVgDataResultListByVgNumber(VG_INFO.getVgNumber());

        // when
        VgViewDto vgViewDto = vgViewFacade.getVgFirstById(id);

        // then
        assertNotNull(vgViewDto);
        assertEquals("vg-data-main", vgViewDto.getViewString());
    }
}