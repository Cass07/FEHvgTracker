package wiki.feh.apitest.service.vgdata;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.dto.VgDataGetDto;
import wiki.feh.apitest.dto.VgDataResultGetDto;
import wiki.feh.apitest.dto.VgDataSaveDto;
import wiki.feh.apitest.domain.vgdata.VgData;
import wiki.feh.apitest.domain.vgdata.VgDataQueryRepository;
import wiki.feh.apitest.domain.vgdata.VgDataRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class VgDataService {
    private final VgDataRepository vgDataRepository;
    private final VgDataQueryRepository vgDataQueryRepository;

    @Transactional
    public long save(VgDataSaveDto vgDataSaveDto) {
        return vgDataRepository.save(vgDataSaveDto.toEntity()).getId();
    }

    @Transactional
    public void saveAll(List<VgDataSaveDto> vgDataSaveDtoList) {
        vgDataRepository.saveAll(vgDataSaveDtoList.stream().map(VgDataSaveDto::toEntity).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public List<VgDataGetDto> getVgDataListByNumRoundTour(int vgNumber, int roundNumber, int tournamentIndex) {
        return vgDataQueryRepository.getVgDataListByNumRoundTour(vgNumber, roundNumber, tournamentIndex).stream()
                .map(VgDataGetDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VgDataGetDto getLatestVgDataByNumRoundTour(int vgNumber, int roundNumber, int tournamentIndex) {
        VgData entity = vgDataQueryRepository.getLatestVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);
        if (entity == null) {
            return null;
        }
        return new VgDataGetDto(entity);
    }

    @Transactional(readOnly = true)
    public Optional<VgData> getVgDataByNumRoundTourTimeIndex(int vgNumber, int roundNumber, int tournamentIndex, int timeIndex) {
        return vgDataRepository.findByVgNumberAndRoundNumberAndTournamentIndexAndTimeIndex(vgNumber, roundNumber, tournamentIndex, timeIndex);
    }

    @Transactional(readOnly = true)
    public Optional<VgData> getFirstVgDataByNumRoundTour(int vgNumber, int roundNumber, int tournamentIndex) {
        return vgDataQueryRepository.getFirstVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex);
    }

    //초동 데이터 출력용 전체 라운드 5시 데이터
    @Transactional(readOnly = true)
    public List<VgDataGetDto> getFirstVgDataListByVgNumber(int vgNumber) {
        return vgDataQueryRepository.getFirstVgDataListByVgNumber(vgNumber).stream()
                .map(VgDataGetDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VgDataResultGetDto> getFirstVgDataResultListByVgNumber(int vgNumber) {
        return vgDataQueryRepository.getFirstVgDataListByVgNumber(vgNumber).stream()
                .map(VgDataResultGetDto::new).collect(Collectors.toList());
    }

    //결과값 출력용 전체 라운드 결과 데이터
    @Transactional(readOnly = true)
    public List<VgDataResultGetDto> getLatestVgDataListByVgNumber(int vgNumber) {
        return vgDataQueryRepository.getLatestVgDataListByVgNumber(vgNumber).stream()
                .map(VgDataResultGetDto::new).collect(Collectors.toList());
    }

    //현재상황값 출력용 특정 라운드 제일 최신시간의 전체데이터
    @Transactional(readOnly = true)
    public List<VgDataGetDto> getLatestVgDataListByVgNumberRound(int vgNumber, int roundNumber) {
        return vgDataQueryRepository.getLatestVgDataListByVgNumberRound(vgNumber, roundNumber).stream()
                .map(VgDataGetDto::new).collect(Collectors.toList());
    }

}
