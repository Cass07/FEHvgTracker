package wiki.feh.apitest.domain.vgdata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.global.config.TestQueryDslConfig;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Transactional
@Import({TestQueryDslConfig.class, VgDataQueryRepository.class})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VgDataQueryRepositoryTest {
    @Autowired
    private VgDataQueryRepository vgDataQueryRepository;

    @DisplayName("vgNumber, roundNumber, tourIndex로 제일 최신의 vgData 조회")
    @Test
    void getLatestVgDatabyNumRoundTour() {
        // given
        int vgNumber = 91;
        int roundNumber = 1;
        int tournamentIndex = 1;

        // when
        VgData vgData = vgDataQueryRepository.getLatestVgDatabyNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // then
        assertNotNull(vgData);
        assertEquals(vgNumber, vgData.getVgNumber());
        assertEquals(roundNumber, vgData.getRoundNumber());
        assertEquals(tournamentIndex, vgData.getTournamentIndex());
        assertEquals(3, vgData.getTimeIndex());
    }

    @DisplayName("vgNumber로 첫번째 vgData 리스트 조회")
    @Test
    void getFirstVgDataListbyVgNumber() {
        // given
        int vgNumber = 91;

        // when
        List<VgData> vgDataList = vgDataQueryRepository.getFirstVgDataListbyVgNumber(vgNumber);

        // then
        assertNotNull(vgDataList);
        assertFalse(vgDataList.isEmpty());
        assertEquals(3, vgDataList.size());
        assertEquals(vgNumber, vgDataList.getFirst().getVgNumber());
        assertEquals(1, vgDataList.getFirst().getTimeIndex());
    }

    @DisplayName("vgNumber, roundNumber, tourIndex로 제일 처음의 vgData 조회")
    @Test
    void getfirstVgDatabyNumRoundTour() {
        // given
        int vgNumber = 91;
        int roundNumber = 1;
        int tournamentIndex = 1;

        // when
        VgData vgData = vgDataQueryRepository.getfirstVgDatabyNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // then
        assertNotNull(vgData);
        assertEquals(vgNumber, vgData.getVgNumber());
        assertEquals(roundNumber, vgData.getRoundNumber());
        assertEquals(tournamentIndex, vgData.getTournamentIndex());
        assertEquals(1, vgData.getTimeIndex());
    }

    @DisplayName("vgNumber로 종료된 라운드의 마지막 vgData 리스트 조회")
    @Test
    void getLatestVgDataListbyVgNumber() {
        // given
        int vgNumber = 50;

        // when
        List<VgData> vgDataList = vgDataQueryRepository.getLatestVgDataListbyVgNumber(vgNumber);

        // then
        assertNotNull(vgDataList);
        assertFalse(vgDataList.isEmpty());
        assertEquals(4, vgDataList.size());
        assertEquals(vgNumber, vgDataList.getFirst().getVgNumber());
        assertEquals(45, vgDataList.getFirst().getTimeIndex());
    }

    private static Stream<Arguments> roundCountExtension() {
        return Stream.of(
                arguments(1, 4),
                arguments(2, 2),
                arguments(3, 1)
        );
    }

    @DisplayName("vgNumber, roundNumber로 제일 최신의 vgData 리스트 조회")
    @ParameterizedTest
    @MethodSource("roundCountExtension")
    void getNowtimeVgDataListbyVgNumberRound(int roundNumber, int resultSize) {
        // given
        int vgNumber = 51;

        // when
        List<VgData> vgDataList = vgDataQueryRepository.getNowtimeVgDataListbyVgNumberRound(vgNumber, roundNumber);

        // then
        assertNotNull(vgDataList);
        assertFalse(vgDataList.isEmpty());
        assertEquals(resultSize, vgDataList.size());
        assertEquals(vgNumber, vgDataList.getFirst().getVgNumber());
        assertEquals(roundNumber, vgDataList.getFirst().getRoundNumber());
        assertEquals(45, vgDataList.getFirst().getTimeIndex());
    }

    @DisplayName("vgNumber, roundNumber, tourIndex로 vgData 리스트 조회")
    @Test
    void getVgDataListbyNumRoundTour() {
        // given
        int vgNumber = 91;
        int roundNumber = 1;
        int tournamentIndex = 1;

        // when
        List<VgData> vgDataList = vgDataQueryRepository.getVgDataListbyNumRoundTour(vgNumber, roundNumber, tournamentIndex);

        // then
        assertNotNull(vgDataList);
        assertEquals(3, vgDataList.size());
        assertEquals(vgNumber, vgDataList.getFirst().getVgNumber());
    }
}