package wiki.feh.apitest.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.dto.VgInfoGetDropdownDto;
import wiki.feh.apitest.dto.VgInfoGetDto;
import wiki.feh.apitest.dto.VgInfoSaveRequestDto;
import wiki.feh.apitest.domain.vginfo.VgInfo;
import wiki.feh.apitest.global.exception.business.VgInfoNotExistException;
import wiki.feh.apitest.service.vginfo.VgInfoService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class VgInfoFacade {
    private final VgInfoService vgInfoService;

    @Transactional(readOnly = true)
    public VgInfoGetDto findById(long id) {
        return new VgInfoGetDto(vgInfoService.findById(id).orElseThrow(VgInfoNotExistException::new));
    }

    @Transactional(readOnly = true)
    public List<VgInfoGetDropdownDto> getVgInfoDropdownList() {
        List<VgInfoGetDropdownDto> vgInfoGetDropdownDtoList = vgInfoService.findAllDesc().stream()
                .map(VgInfoGetDropdownDto::new).collect(Collectors.toList());
        vgInfoGetDropdownDtoList.add(new VgInfoGetDropdownDto(-1, "신규 데이터 추가"));
        vgInfoGetDropdownDtoList.addFirst(new VgInfoGetDropdownDto(0, "투표대전 리스트"));
        return vgInfoGetDropdownDtoList;
    }

    @Transactional
    public long vgInfoUpdate(long id, VgInfoSaveRequestDto vgInfoSaveRequestDto) {
        VgInfo entity = vgInfoService.findById(id).orElseThrow(VgInfoNotExistException::new);
        entity.update(vgInfoSaveRequestDto);
        return id;
    }

    @Transactional
    public long save(VgInfoSaveRequestDto vgInfoSaveRequestDto) {
        return vgInfoService.save(vgInfoSaveRequestDto.toEntity()).getId();
    }
}
