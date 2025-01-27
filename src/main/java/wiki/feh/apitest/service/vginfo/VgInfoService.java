package wiki.feh.apitest.service.vginfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.domain.vginfo.VgInfo;
import wiki.feh.apitest.domain.vginfo.VgInfoQueryRepository;
import wiki.feh.apitest.domain.vginfo.VgInfoRepository;
import wiki.feh.apitest.controller.dto.VgInfoGetDropdownDto;
import wiki.feh.apitest.controller.dto.VgInfoGetDto;
import wiki.feh.apitest.controller.dto.VgInfoSaveRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VgInfoService {
    private final VgInfoRepository vgInfoRepository;
    private final VgInfoQueryRepository vgInfoQueryRepository;

    @Transactional(readOnly = true)
    public VgInfoGetDto findbyId(long id) {
        VgInfo entity = vgInfoRepository.findById(id).orElse(null);

        if (entity == null) {
            return null;
        } else {
            return new VgInfoGetDto(entity);
        }
    }

    @Transactional(readOnly = true)
    public VgInfoGetDto findbyVgnumber(int vgNumber) {
        VgInfo entity = vgInfoQueryRepository.findByVgnumber(vgNumber);
        if (entity == null) {
            return null;
        } else {
            return new VgInfoGetDto(entity);
        }
    }

    @Transactional(readOnly = true)
    public VgInfoGetDto getLatestVgInfoDto() {
        VgInfo entity = vgInfoQueryRepository.getLatestVgInfo();
        if (entity == null) {
            return null;
        } else {
            return new VgInfoGetDto(entity);
        }
    }

    @Transactional(readOnly = true)
    public VgInfo getLatestVgInfo() {
        return vgInfoQueryRepository.getLatestVgInfo();
    }

    @Transactional(readOnly = true)
    public List<VgInfoGetDto> findAllDesc() {
        return vgInfoQueryRepository.findAllDecs().stream()
                .map(VgInfoGetDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VgInfoGetDropdownDto> findAllDescDropdown() {
        List<VgInfoGetDropdownDto> vgInfoGetDropdownDtos = vgInfoQueryRepository.findAllDecs().stream()
                .map(VgInfoGetDropdownDto::new).collect(Collectors.toList());
        vgInfoGetDropdownDtos.add(new VgInfoGetDropdownDto(-1, "신규 데이터 추가"));
        vgInfoGetDropdownDtos.addFirst(new VgInfoGetDropdownDto(0, "투표대전 리스트"));
        return vgInfoGetDropdownDtos;
    }

    @Transactional
    public long save(VgInfoSaveRequestDto vgInfoSaveRequestDto) {
        return vgInfoRepository.save(vgInfoSaveRequestDto.toEntity()).getId();
    }

    @Transactional
    public long update(long id, VgInfoSaveRequestDto vgInfoSaveRequestDto) {
        VgInfo entity = vgInfoRepository.findById(id).orElse(null);

        if (entity == null) {
            return -1;
        } else {
            entity.update(vgInfoSaveRequestDto.getVgNumber(), vgInfoSaveRequestDto.getVgTitle(), vgInfoSaveRequestDto.getVgStartDate(),
                    vgInfoSaveRequestDto.getTeam1Id(), vgInfoSaveRequestDto.getTeam2Id(), vgInfoSaveRequestDto.getTeam3Id(), vgInfoSaveRequestDto.getTeam4Id(),
                    vgInfoSaveRequestDto.getTeam5Id(), vgInfoSaveRequestDto.getTeam6Id(), vgInfoSaveRequestDto.getTeam7Id(), vgInfoSaveRequestDto.getTeam8Id());
            return id;
        }

    }

}
