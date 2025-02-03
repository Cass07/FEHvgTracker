package wiki.feh.apitest.domain.vginfo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.global.config.TestQueryDslConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Import({TestQueryDslConfig.class, VgInfoQueryRepository.class})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VgInfoQueryRepositoryTest {
    @Autowired
    private VgInfoQueryRepository vgInfoQueryRepository;

    @DisplayName("제일 최신의 VgInfo 조회")
    @Test
    void getLatestVgInfo() {
        // given
        int latestVgNumber = 91;

        // when
        VgInfo vgInfo = vgInfoQueryRepository.getLatestVgInfo().orElse(null);

        // then
        assertNotNull(vgInfo);
        assertEquals(latestVgNumber, vgInfo.getVgNumber());
    }

    @DisplayName("모든 VgInfo 리스트 조회")
    @Test
    void findAllDesc() {
        // when
        List<VgInfo> vgInfoList = vgInfoQueryRepository.findAllDesc();

        // then
        assertNotNull(vgInfoList);
        assertFalse(vgInfoList.isEmpty());
    }

    @DisplayName("vgNumber로 VgInfo 조회")
    @Test
    void findByVgNumber() {
        // given
        int vgNumber = 91;

        // when
        VgInfo vgInfo = vgInfoQueryRepository.findByVgNumber(vgNumber);

        // then
        assertNotNull(vgInfo);
        assertEquals(vgNumber, vgInfo.getVgNumber());
    }

}