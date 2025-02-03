package wiki.feh.apitest.facade;

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
import wiki.feh.apitest.global.exception.business.VgInfoNotExistException;
import wiki.feh.apitest.service.vginfo.VgInfoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class VgInfoFacadeTest {
    @InjectMocks
    private VgInfoFacade vgInfoFacade;

    @Mock
    private VgInfoService vgInfoService;


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

        doReturn(Optional.of(entity)).when(vgInfoService).findById(id);

        // when
        VgInfoGetDto dto = vgInfoFacade.findById(id);

        // then
        assertNotNull(dto);
        assertEquals(dto.getVgNumber(), vgNumber);
    }

    @DisplayName("VgInfo 조회 - vgInfo 없음")
    @Test
    void findById_NoVgInfo() {
        // given
        long id = 1;

        doReturn(Optional.empty()).when(vgInfoService).findById(id);

        // when
        assertThrows(VgInfoNotExistException.class, () -> vgInfoFacade.findById(id));
    }

    @DisplayName("VgInfo dropdown list 조회")
    @Test
    void getVgInfoDropdownList() {
        // given
        int vgNumber = 2;
        String vgTitle = "title";
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle(vgTitle)
                .vgStartDate(startDate)
                .build();

        doReturn(List.of(entity)).when(vgInfoService).findAllDesc();

        // when
        List<VgInfoGetDropdownDto> result = vgInfoFacade.getVgInfoDropdownList();

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.size(), 3);
        assertEquals(0, result.getFirst().getId());
        assertEquals(-1, result.getLast().getId());
    }

    @DisplayName("VgInfo update")
    @Test
    void updateVgInfo() {
        // given
        long id = 1;
        int vgNumber = 2;
        String vgTitle = "title";
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle(vgTitle)
                .vgStartDate(startDate)
                .build();
        VgInfoSaveRequestDto dto = VgInfoSaveRequestDto.builder()
                .vgNumber(vgNumber)
                .vgTitle("title2")
                .vgStartDate(startDate)
                .build();

        doReturn(Optional.of(entity)).when(vgInfoService).findById(id);

        // when
        long result = vgInfoFacade.vgInfoUpdate(id, dto);

        // then
        assertEquals(id, result);
    }

    @DisplayName("VgInfo update - VgInfo 없음")
    @Test
    void updateVgInfo_NoVgInfo() {
        // given
        long id = 1;
        int vgNumber = 2;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfoSaveRequestDto dto = VgInfoSaveRequestDto.builder()
                .vgNumber(vgNumber)
                .vgTitle("title2")
                .vgStartDate(startDate)
                .build();

        doReturn(Optional.empty()).when(vgInfoService).findById(id);

        // when
        assertThrows(VgInfoNotExistException.class, () -> vgInfoFacade.vgInfoUpdate(id, dto));
    }

    @DisplayName("VgInfo save")
    @Test
    void saveVgInfo() {
        // given
        int vgNumber = 2;
        String vgTitle = "title";
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        VgInfo entity = VgInfo.builder()
                .vgNumber(vgNumber)
                .vgTitle(vgTitle)
                .vgStartDate(startDate)
                .build();
        VgInfoSaveRequestDto dto = VgInfoSaveRequestDto.builder()
                .vgNumber(vgNumber)
                .vgTitle(vgTitle)
                .vgStartDate(startDate)
                .build();

        doReturn(entity).when(vgInfoService).save(any(VgInfo.class));

        // when
        long result = vgInfoFacade.save(dto);

        // then
        assert(result >= 0);
    }
}