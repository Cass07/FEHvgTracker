package wiki.feh.apitest.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.dto.*;
import wiki.feh.apitest.global.exception.view.VgDataIllegalException;
import wiki.feh.apitest.global.exception.view.VgNotExistException;
import wiki.feh.apitest.global.exception.view.VgRoundNotExistException;
import wiki.feh.apitest.service.vgdata.VgDataService;
import wiki.feh.apitest.service.vginfo.VgInfoService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class VgViewFacade {
    private final VgInfoService vgInfoService;
    private final VgDataService vgDataService;

    @Transactional(readOnly = true)
    public VgInfoGetDto getVgInfoById(long id) {
        return new VgInfoGetDto(vgInfoService.findById(id).orElseThrow(
                VgNotExistException::new
        ));
    }

    @Transactional(readOnly = true)
    public VgDataGetDto getFirstVgDataByNumRoundTour(int vgNumber, int roundNumber, int tournamentIndex) {
        return new VgDataGetDto(vgDataService.getFirstVgDataByNumRoundTour(vgNumber, roundNumber, tournamentIndex).orElseThrow(
                VgRoundNotExistException::new
        ));
    }

    @Transactional(readOnly = true)
    public VgViewDto getVgMainByid(long id) {
        //vgnum이 -1이라면 제일 최신의 VgInfodata를 조회한다.
        VgInfoGetDto vginfoEntity;

        List<VgDataGetDto> currentRoundVgdata = null;
        List<VgDataResultGetDto> round1Vgdata = null;
        List<VgDataResultGetDto> round2Vgdata = null;
        List<VgDataResultGetDto> round3Vgdata = null;

        // id가 -1이라면 최신의 vg를, 아니라면 입력받은 vg를 출력한다
        if (id == -1) {
            vginfoEntity = new VgInfoGetDto(vgInfoService.getLatestVgInfo().orElseThrow(
                    VgNotExistException::new
            ));
        } else {
            vginfoEntity = new VgInfoGetDto(vgInfoService.findById(id).orElseThrow(
                    VgNotExistException::new
            ));
        }

        // viewModel에, heroName을 입력한다
        Map<String, String> viewModel = new HashMap<>();

        int vgNumber = vginfoEntity.getVgNumber();

        // 종료된 vg round의 데이터를 조회
        List<VgDataResultGetDto> resultVgDataList = vgDataService.getLatestVgDataListByVgNumber(vgNumber);
        int resultVgDataSize = resultVgDataList.size();

        // 조회한 vgData의 수에 따라서, 라운드 값을 역산함
        int round = switch (resultVgDataSize) {
            case 0 -> 1;
            case 4 -> 2;
            case 6 -> 3;
            case 7 -> 0;
            default -> -1;
        };

        // 정상적이지 않은 vgData 수가 나왔다면, 오류 페이지를 출력할 view를 넘겨줌
        if (round == -1) {
            log.error("round data error - vgDataSize : {}, round : {}", resultVgDataSize, round);
            throw new VgDataIllegalException();
        }

        // 현재 상황의 최신 vgData를 받음
        List<VgDataGetDto> currentVgDataList = vgDataService.getLatestVgDataListByVgNumberRound(vgNumber, round);

        if (currentVgDataList.isEmpty()) {
            //아예 시작안햇거나(라운드1), 이전라운드 종료되엇고 현재라운드 시작안햇거나, 모든 라운드 종료됨
            if (round == 1) {
                viewModel.put("round1StartNot", "1라운드 대진표");
            }
        } else {
            //진행중인 라운드 데이터 넘겨줌
            viewModel.put("currentRoundVgTitle", round + "라운드 진행 중");
            currentRoundVgdata = currentVgDataList;
        }

        //해당 라운드면 어쨌든 그 전 라운드는 종료된상태이니 종료라운드 데이터 넘겨줌
        if (round == 2) {
            viewModel.put("round1VgTitle", "1라운드 결과");
            round1Vgdata = resultVgDataList;
        } else if (round == 3) {
            viewModel.put("round2VgTitle", "2라운드 결과");
            round2Vgdata = resultVgDataList.subList(0, 2);
            viewModel.put("round1VgTitle", "1라운드 결과");
            round1Vgdata = resultVgDataList.subList(2, 6);
        } else if (round == 0) {
            viewModel.put("round3VgTitle", "3라운드 결과");
            round3Vgdata = resultVgDataList.subList(0, 1);
            viewModel.put("round2VgTitle", "2라운드 결과");
            round2Vgdata = resultVgDataList.subList(1, 3);
            viewModel.put("round1VgTitle", "1라운드 결과");
            round1Vgdata = resultVgDataList.subList(3, 7);
        }

        viewModel.put("vgStartDateTimeStr", vginfoEntity.getVgStartDate().atTime(16, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        viewModel.put("vgEndDateTimeStr", vginfoEntity.getVgStartDate().atTime(13, 0).plusDays(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return VgViewDto.builder()
                .viewString("vg-data-main")
                .currentRoundVgdata(currentRoundVgdata)
                .round1Vgdata(round1Vgdata)
                .round2Vgdata(round2Vgdata)
                .round3Vgdata(round3Vgdata)
                .vgInfoEntity(vginfoEntity)
                .viewModel(viewModel)
                .teamList(getTeamDtoListByVgInfo(vginfoEntity))
                .build();

    }

    @Transactional(readOnly = true)
    public VgViewDto getVgFirstById(long id) {
        VgInfoGetDto vginfoEntity;

        List<VgDataGetDto> currentRoundVgdata = null;
        List<VgDataResultGetDto> round1Vgdata = null;
        List<VgDataResultGetDto> round2Vgdata = null;
        List<VgDataResultGetDto> round3Vgdata = null;

        if (id == -1) {
            vginfoEntity = new VgInfoGetDto(vgInfoService.getLatestVgInfo().orElseThrow(
                    VgNotExistException::new
            ));
        } else {
            vginfoEntity = new VgInfoGetDto(vgInfoService.findById(id).orElseThrow(
                    VgNotExistException::new
            ));
        }

        Map<String, String> viewModel = new HashMap<>();

        viewModel.put("header_title", "투표대전 점수 트래커 - 초동 데이터");

        int firstVgNumber = vginfoEntity.getVgNumber();
        List<VgDataResultGetDto> firstVgDataList = vgDataService.getFirstVgDataResultListByVgNumber(firstVgNumber);
        int firstVgDataSize = firstVgDataList.size();

        int round = switch (firstVgDataSize) {
            case 4 -> 1;
            case 6 -> 2;
            case 7 -> 3;
            case 0 -> 0;
            default -> -1;
        };

        if (round == -1) {
            log.error("round data error - vgDataSize : {}, round : {}", firstVgDataSize, round);
            throw new VgDataIllegalException();
        }

        if (round == 1) {
            viewModel.put("round1VgTitle", "1라운드 5시 데이터");
            round1Vgdata = firstVgDataList;
        } else if (round == 2) {

            viewModel.put("round2VgTitle", "2라운드 5시 데이터");
            round2Vgdata = firstVgDataList.subList(0, 2);
            viewModel.put("round1VgTitle", "1라운드 5시 데이터");
            round1Vgdata = firstVgDataList.subList(2, 6);
        } else if (round == 3) {
            viewModel.put("round3VgTitle", "3라운드 5시 데이터");
            round3Vgdata = firstVgDataList.subList(0, 1);
            viewModel.put("round2VgTitle", "2라운드 5시 데이터");
            round2Vgdata = firstVgDataList.subList(1, 3);
            viewModel.put("round1VgTitle", "1라운드 5시 데이터");
            round1Vgdata = firstVgDataList.subList(3, 7);
        }

        viewModel.put("vgStartDateTimeStr", vginfoEntity.getVgStartDate().atTime(16, 0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        viewModel.put("vgEndDateTimeStr", vginfoEntity.getVgStartDate().atTime(13, 0).plusDays(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return VgViewDto.builder()
                .viewString("vg-data-main")
                .currentRoundVgdata(currentRoundVgdata)
                .round1Vgdata(round1Vgdata)
                .round2Vgdata(round2Vgdata)
                .round3Vgdata(round3Vgdata)
                .vgInfoEntity(vginfoEntity)
                .viewModel(viewModel)
                .teamList(getTeamDtoListByVgInfo(vginfoEntity))
                .build();
    }

    private List<TeamDto> getTeamDtoListByVgInfo(VgInfoGetDto vgInfoEntity) {
        List<TeamDto> teamDtoList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            teamDtoList.add(new TeamDto(vgInfoEntity.getTeamIdbyIndex(i), i));
        }

        return teamDtoList;
    }
}
