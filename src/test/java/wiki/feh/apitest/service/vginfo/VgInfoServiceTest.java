package wiki.feh.apitest.service.vginfo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import wiki.feh.apitest.controller.dto.VgInfoGetDropdownDto;
import wiki.feh.apitest.controller.dto.VgInfoGetDto;
import wiki.feh.apitest.controller.dto.VgInfoSaveRequestDto;
import wiki.feh.apitest.domain.vginfo.VgInfo;
import wiki.feh.apitest.domain.vginfo.VgInfoQueryRepository;
import wiki.feh.apitest.domain.vginfo.VgInfoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
        VgInfoGetDto dto = vgInfoService.findById(id);

        // then
        assertNotNull(dto);
        assertEquals(dto.getVgNumber(), vgNumber);
    }

    @DisplayName("VgInfo 조회 - id - 데이터 없음")
    @Test
    void findByIdNoData() {
        // given
        long id = 1;

        doReturn(Optional.empty()).when(vgInfoRepository).findById(id);

        // when
        VgInfoGetDto dto = vgInfoService.findById(id);

        // then
        assertNull(dto);
    }

    @DisplayName("VgInfo 조회 - vgNumber")
    @Test
    void findByVgNumber() {
        // given
        int vgNumber = 2;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle("title")
                .vgStartDate(startDate)
                .build();

        doReturn(entity).when(vgInfoQueryRepository).findByVgnumber(vgNumber);

        // when
        VgInfoGetDto dto = vgInfoService.findByVgNumber(vgNumber);

        // then
        assertNotNull(dto);
        assertEquals(dto.getVgNumber(), vgNumber);
    }

    @DisplayName("VgInfo 조회 - vgNumber - 데이터 없음")
    @Test
    void findByVgNumberNoData() {
        // given
        int vgNumber = 2;

        doReturn(null).when(vgInfoQueryRepository).findByVgnumber(vgNumber);

        // when
        VgInfoGetDto dto = vgInfoService.findByVgNumber(vgNumber);

        // then
        assertNull(dto);
    }

    @DisplayName("VgInfo 조회 - 최신 데이터")
    @Test
    void getLatestVgInfoDto() {
        // given
        int vgNumber = 2;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle("title")
                .vgStartDate(startDate)
                .build();

        doReturn(entity).when(vgInfoQueryRepository).getLatestVgInfo();

        // when
        VgInfoGetDto dto = vgInfoService.getLatestVgInfoDto();

        // then
        assertNotNull(dto);
        assertEquals(dto.getVgNumber(), vgNumber);
    }

    @DisplayName("VgInfo 조회 - 최신 데이터 - 데이터 없음")
    @Test
    void getLatestVgInfoDtoNoData() {
        // given
        doReturn(null).when(vgInfoQueryRepository).getLatestVgInfo();

        // when
        VgInfoGetDto dto = vgInfoService.getLatestVgInfoDto();

        // then
        assertNull(dto);
    }

    @DisplayName("VgInfo 조회 - 최신 데이터 - VgInfo")
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

        doReturn(entity).when(vgInfoQueryRepository).getLatestVgInfo();

        // when
        VgInfo dto = vgInfoService.getLatestVgInfo();

        // then
        assertNotNull(dto);
        assertEquals(dto.getVgNumber(), vgNumber);
    }

    @DisplayName("VgInfo list 조회 - 전체 데이터")
    @Test
    void findAllDescList() {
        // given
        int vgNumber = 2;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle("title")
                .vgStartDate(startDate)
                .build();

        doReturn(List.of(entity)).when(vgInfoQueryRepository).findAllDecs();

        // when
        List<VgInfoGetDto> result = vgInfoService.findAllDesc();

        // then
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.getFirst().getVgNumber(), vgNumber);
    }

    @DisplayName("VgInfo list 조회 - 전체 데이터 - dropdown")
    @Test
    void findAllDescDropdown() {
        // given
        int vgNumber = 2;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle("title")
                .vgStartDate(startDate)
                .build();

        doReturn(List.of(entity)).when(vgInfoQueryRepository).findAllDecs();

        // when
        List<VgInfoGetDropdownDto> result = vgInfoService.findAllDescDropdown();

        // then
        assertNotNull(result);
        assertEquals(result.size(), 3);
        assertEquals(result.getFirst().getId(), 0);
        assertEquals(result.getLast().getId(), -1);
    }

    @DisplayName("VgInfo 저장")
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
        VgInfoSaveRequestDto dto = VgInfoSaveRequestDto.builder()
                .vgNumber(vgNumber)
                .vgTitle("title")
                .vgStartDate(startDate)
                .build();

        doReturn(entity).when(vgInfoRepository).save(any(VgInfo.class));

        // when
        long result = vgInfoService.save(dto);

        // then
        assert (result >= 0);
    }

    @DisplayName("VgInfo 수정")
    @Test
    void update() {
        // given
        long id = 1;
        int vgNumber = 2;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle("title")
                .vgStartDate(startDate)
                .build();
        VgInfoSaveRequestDto dto = VgInfoSaveRequestDto.builder()
                .vgNumber(vgNumber)
                .vgTitle("title2")
                .vgStartDate(startDate)
                .build();

        doReturn(Optional.of(entity)).when(vgInfoRepository).findById(id);

        // when
        long result = vgInfoService.update(id, dto);

        // then
        assert(result >= 0);
        verify(vgInfoRepository).findById(id);
        // Entity의 method가 call됐음을 체킹하려면?
    }

    @DisplayName("VgInfo 수정 - 데이터 없음")
    @Test
    void updateNoData() {
        // given
        long id = 1;
        VgInfoSaveRequestDto dto = VgInfoSaveRequestDto.builder()
                .vgNumber(2)
                .vgTitle("title2")
                .vgStartDate(LocalDate.of(2025, 1, 1))
                .build();

        doReturn(Optional.empty()).when(vgInfoRepository).findById(id);

        // when
        long result = vgInfoService.update(id, dto);

        // then
        assertEquals(result, -1);
    }
}