package wiki.feh.apitest.service.vginfo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.domain.vginfo.VgInfo;
import wiki.feh.apitest.domain.vginfo.VgInfoQueryRepository;
import wiki.feh.apitest.domain.vginfo.VgInfoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class VgInfoServiceTest {
    @InjectMocks
    private VgInfoService vgInfoService;

    @Mock
    private VgInfoRepository vgInfoRepository;

    @Mock
    private VgInfoQueryRepository vgInfoQueryRepository;

    @DisplayName("VgInfo 조회 - id")
    @Test
    void findById() {
        // given
        long id = 1;
        int vgNumber = 2;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle("title")
                .vgStartDate(startDate)
                .build();


        doReturn(Optional.of(entity)).when(vgInfoRepository).findById(id);

        // when
        Optional<VgInfo> result = vgInfoService.findById(id);

        // then
        assertTrue(result.isPresent());
        assertEquals(result.get().getVgNumber(), vgNumber);
    }

    @DisplayName("VgInfo 조회 - id - 데이터 없음")
    @Test
    void findByIdNoData() {
        // given
        long id = 1;

        doReturn(Optional.empty()).when(vgInfoRepository).findById(id);

        // when
        Optional<VgInfo> result = vgInfoService.findById(id);


        // then
        assertFalse(result.isPresent());
    }

    @DisplayName("VgInfo 조회 - 최신 데이터 - VgInfo")
    @Test
    void findAllDescDto() {
        // given
        int vgNumber = 2;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle("title")
                .vgStartDate(startDate)
                .build();

        doReturn(Optional.of(entity)).when(vgInfoQueryRepository).getLatestVgInfo();

        // when
        VgInfo dto = vgInfoService.getLatestVgInfo().orElse(null);

        // then
        assertNotNull(dto);
        assertEquals(dto.getVgNumber(), vgNumber);
    }

    @DisplayName("VgInfo list 조회 - vginfo")
    @Test
    void findAllDesc() {
        // given
        int vgNumber = 2;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle("title")
                .vgStartDate(startDate)
                .build();

        doReturn(List.of(entity)).when(vgInfoQueryRepository).findAllDesc();

        // when
        List<VgInfo> result = vgInfoService.findAllDesc();

        // then
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.getFirst().getVgNumber(), vgNumber);
    }

    @DisplayName("VgInfo Save")
    @Test
    void save() {
        // given
        int vgNumber = 2;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle("title")
                .vgStartDate(startDate)
                .build();

        doReturn(entity).when(vgInfoRepository).save(entity);

        // when
        VgInfo result = vgInfoService.save(entity);

        // then
        assertNotNull(result);
        assertEquals(result.getVgNumber(), vgNumber);
    }
}