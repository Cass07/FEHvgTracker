package wiki.feh.apitest.service.vginfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.domain.vginfo.VgInfo;
import wiki.feh.apitest.domain.vginfo.VgInfoQueryRepository;
import wiki.feh.apitest.domain.vginfo.VgInfoRepository;
import wiki.feh.apitest.web.dto.VgInfoGetDropdownDto;
import wiki.feh.apitest.web.dto.VgInfoGetDto;
import wiki.feh.apitest.web.dto.VgInfoSaveRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VgInfoService {
    private final VgInfoRepository vgInfoRepository;
    private final VgInfoQueryRepository vgInfoQueryRepository;

    @Transactional(readOnly = true)
    public VgInfoGetDto findbyId(Long id)
    {
        VgInfo entity = vgInfoRepository.findById(id).orElse(null);

        if(entity == null)
        {
            return null;
        }else
        {
            return new VgInfoGetDto(entity);
        }
    }

    @Transactional(readOnly = true)
    public VgInfoGetDto findbyVgnumber(int vgNumber)
    {
        VgInfo entity = vgInfoQueryRepository.findByVgnumber(vgNumber);
        if(entity == null)
        {
            return null;
        }else
        {
            return new VgInfoGetDto(entity);
        }
    }

    @Transactional(readOnly = true)
    public VgInfoGetDto getLatestVgInfo()
    {
        VgInfo entity = vgInfoQueryRepository.getLatestVgInfo();
        if(entity == null)
        {
            return null;
        }else
        {
            return new VgInfoGetDto(entity);
        }
    }

    @Transactional(readOnly = true)
    public List<VgInfoGetDto> findAllDesc()
    {
        return vgInfoQueryRepository.findAllDecs().stream()
                .map(VgInfoGetDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VgInfoGetDropdownDto> findAllDescDropdown()
    {
        List<VgInfoGetDropdownDto> vgInfoGetDropdownDtos = vgInfoQueryRepository.findAllDecs().stream()
                .map(VgInfoGetDropdownDto::new).collect(Collectors.toList());
        vgInfoGetDropdownDtos.add(new VgInfoGetDropdownDto((long) -1, "신규 데이터 추가"));
        vgInfoGetDropdownDtos.add(0, new VgInfoGetDropdownDto((long)0, "투표대전 리스트"));
        return vgInfoGetDropdownDtos;
    }

    @Transactional
    public Long save(VgInfoSaveRequestDto vgInfoSaveRequestDto)
    {
        return vgInfoRepository.save(vgInfoSaveRequestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, VgInfoSaveRequestDto vgInfoSaveRequestDto)
    {
        VgInfo entity = vgInfoRepository.findById(id).orElse(null);

        if(entity == null)
        {
            return null;
        }else
        {
            entity.update(vgInfoSaveRequestDto.getVgNumber(), vgInfoSaveRequestDto.getVgTitle(), vgInfoSaveRequestDto.getVgStartDate(),
                    vgInfoSaveRequestDto.getTeam1Id(), vgInfoSaveRequestDto.getTeam2Id(), vgInfoSaveRequestDto.getTeam3Id(),vgInfoSaveRequestDto.getTeam4Id(),
                    vgInfoSaveRequestDto.getTeam5Id(), vgInfoSaveRequestDto.getTeam6Id(), vgInfoSaveRequestDto.getTeam7Id(), vgInfoSaveRequestDto.getTeam8Id());
            return id;
        }

    }

}
