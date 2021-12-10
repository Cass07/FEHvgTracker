package wiki.feh.apitest.service.vgdata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.domain.vgdata.VgData;
import wiki.feh.apitest.domain.vgdata.VgDataQueryRepository;
import wiki.feh.apitest.domain.vgdata.VgDataRepository;
import wiki.feh.apitest.web.dto.VgDataGetDto;
import wiki.feh.apitest.web.dto.VgDataResultGetDto;
import wiki.feh.apitest.web.dto.VgDataSaveDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VgDataService {
    private final VgDataRepository vgDataRepository;
    private final VgDataQueryRepository vgDataQueryRepository;

    @Transactional
    public Long save(VgDataSaveDto vgDataSaveDto)
    {
        return vgDataRepository.save(vgDataSaveDto.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public List<VgDataGetDto> getVgDataListbyNumRoundTour(int vgNumber, int roundNumber, int tournamentIndex)
    {
        return vgDataQueryRepository.getVgDataListbyNumRoundTour(vgNumber, roundNumber, tournamentIndex).stream()
                .map(VgDataGetDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VgDataGetDto getLatestVgDatabyNumRoundTour (int vgNumber, int roundNumber, int tournamentIndex)
    {
        VgData entity = vgDataQueryRepository.getLatestVgDatabyNumRoundTour(vgNumber, roundNumber, tournamentIndex);
        if(entity == null)
        {
            return null;
        }else
        {
            return new VgDataGetDto(entity);
        }
    }

    @Transactional(readOnly = true)
    public VgDataGetDto getFirstVgDatabyNumRoundTour (int vgNumber, int roundNumber, int tournamentIndex)
    {
        VgData entity = vgDataQueryRepository.getfirstVgDatabyNumRoundTour(vgNumber, roundNumber, tournamentIndex);
        if(entity == null)
        {
            return null;
        }else
        {
            return new VgDataGetDto(entity);
        }
    }

    //초동 데이터 출력용 전체 라운드 5시 데이터
    @Transactional(readOnly = true)
    public List<VgDataGetDto> getFirstVgDataListbyVgNumber(int vgNumber)
    {
        return vgDataQueryRepository.getFirstVgDataListbyVgNumber(vgNumber).stream()
                .map(VgDataGetDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VgDataResultGetDto> getFirstVgDataResultListbyVgNumber(int vgNumber)
    {
        return vgDataQueryRepository.getFirstVgDataListbyVgNumber(vgNumber).stream()
                .map(VgDataResultGetDto::new).collect(Collectors.toList());
    }

    //결과값 출력용 전체 라운드 결과 데이터
    @Transactional(readOnly = true)
    public List<VgDataResultGetDto> getLatestVgDataListbyVgNumber(int vgNumber)
    {
        return vgDataQueryRepository.getLatestVgDataListbyVgNumber(vgNumber).stream()
                .map(VgDataResultGetDto::new).collect(Collectors.toList());
    }

    //현재상황값 출력용 특정 라운드 제일 최신시간의 전체데이터
    @Transactional(readOnly = true)
    public List<VgDataGetDto> getNowtimeVgDataListbyVgNumberRound(int vgNumber, int roundNumber)
    {
        return vgDataQueryRepository.getNowtimeVgDataListbyVgNumberRound(vgNumber, roundNumber).stream()
                .map(VgDataGetDto::new).collect(Collectors.toList());
    }

}
