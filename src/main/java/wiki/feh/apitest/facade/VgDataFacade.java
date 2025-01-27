package wiki.feh.apitest.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.controller.dto.PostsSaveRequestDto;
import wiki.feh.apitest.controller.dto.VgDataSaveDto;
import wiki.feh.apitest.domain.vginfo.VgInfo;
import wiki.feh.apitest.global.exception.VgDataNotExistException;
import wiki.feh.apitest.global.exception.VgTermException;
import wiki.feh.apitest.service.posts.PostsService;
import wiki.feh.apitest.service.vgdata.VgDataService;
import wiki.feh.apitest.service.vginfo.VgInfoService;
import wiki.feh.apitest.util.VgDataCrawl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class VgDataFacade {
    private final VgDataService vgDataService;
    private final VgInfoService vgInfoService;
    private final PostsService postsService;
    private final VgDataCrawl vgDataCrawl;

    @Transactional
    public void updateVgData() {
        LocalDateTime currentTime = LocalDateTime.now();
        log.info("updateVgData : {}", currentTime);
        VgInfo currentVgInfo = vgInfoService.getLatestVgInfo();

        if (currentVgInfo == null || !currentVgInfo.isValidTime(currentTime)) {
            log.info("VgInfo Not Exist or Invalid Time : {}", currentTime);
            return;
        }

        int vgNumber = currentVgInfo.getVgNumber();
        int round = currentVgInfo.getRoundByTime(currentTime);
        int timeDiff = currentVgInfo.getRoundTimeDiff(currentTime, round);

        // 현재 vg, round, timeDiff 데이터가 저장되어 있는지 여부 조회
        if (vgDataService.getVgDatabyNumRoundTourTimeIndex(vgNumber, round, 1, timeDiff) == null) {
            log.info("VgData Already Exist : {} {} {}", vgNumber, round, timeDiff);
            return;
        }
        try {
            vgDataService.saveAll(makeVgData(round, timeDiff, vgNumber));
            log.info("VgData Save : {} {} {}", vgNumber, round, timeDiff);
        } catch (VgTermException | VgDataNotExistException e) {
            log.info(e.getMessage());
        }

    }

    private List<VgDataSaveDto> makeVgData(int round, int timeDiff, int vgNumber) {

        if (round == 1 && timeDiff == 0) {
            postsService.save(new PostsSaveRequestDto("로그 : " + vgNumber, "1라운드 시간차 0입니다.", "kjh95828@gmail.com"));
            throw new VgTermException();
        }

        if ((round == 1 || round == 2) && (timeDiff >= 45 && timeDiff <= 47)) {
            postsService.save(new PostsSaveRequestDto("로그 : " + vgNumber, round + "라운드 시간차 " + timeDiff + "입니다.", "kjh95828@gmail.com"));
            //계산중일때는 <p>태그 하나에 계산중입니다 이거만써잇음
            throw new VgTermException();
        }

        List<Map<String, String>> vgDataList = vgDataCrawl.getMapListByCrawl(vgNumber);

        int dataSize = vgDataList.size();
        if (dataSize == 4) {
            return getRound1VgDataSaveList(vgDataList, round, timeDiff, vgNumber);
        }
        if (dataSize == 6) {
            return getRound2VgDataSaveList(vgDataList, round, timeDiff, vgNumber);
        }
        if (dataSize == 7) {
            return getRound3VgDataSaveList(vgDataList, round, timeDiff, vgNumber);
        }

        throw new VgDataNotExistException();
    }

    private List<VgDataSaveDto> getRound1VgDataSaveList(List<Map<String, String>> vgDataList, int round, int timeDiff, int vgNumber) {
        List<VgDataSaveDto> vgDataSaveDtoList = new ArrayList<>();
        if (timeDiff == 48) {
            timeDiff = 45;
        }
        for (int i = 0; i < 4; i++) {
            vgDataSaveDtoList.add(makeVgDataSaveDto(vgDataList.get(i), round, timeDiff, vgNumber, i + 1));
        }
        return vgDataSaveDtoList;
    }

    // round 1, 2의 결과는 1~4시동안 공개되지 않고, 4시에 공개됨 그래서 round 1, 2일 때 데이터가 6개 혹은 7개가 될 수 있으며 (이 경우 round 2, 3데이터는 조회되나 빈 문자열임), 이 때에는 round 1, 2의 결과 데이터를 입력함
    private List<VgDataSaveDto> getRound2VgDataSaveList(List<Map<String, String>> vgDataList, int round, int timeDiff, int vgNumber) {
        List<VgDataSaveDto> vgDataSaveDtoList = new ArrayList<>();
        if (timeDiff == 48) {
            timeDiff = 45;
        }
        if (round == 1) {
            for (int i = 2; i < 6; i++) {
                vgDataSaveDtoList.add(makeVgDataSaveDto(vgDataList.get(i), round, timeDiff, vgNumber, i - 1));
            }
        } else {
            for (int i = 0; i < 2; i++) {
                vgDataSaveDtoList.add(makeVgDataSaveDto(vgDataList.get(i), round, timeDiff, vgNumber, i + 1));
            }
        }
        return vgDataSaveDtoList;
    }

    private List<VgDataSaveDto> getRound3VgDataSaveList(List<Map<String, String>> vgDataList, int round, int timeDiff, int vgNumber) {
        List<VgDataSaveDto> vgDataSaveDtoList = new ArrayList<>();
        if (round == 2 && timeDiff == 48) {
            timeDiff = 45;
            for (int i = 1; i < 3; i++) {
                vgDataSaveDtoList.add(makeVgDataSaveDto(vgDataList.get(i), round, timeDiff, vgNumber, i));
            }
        } else {
            vgDataSaveDtoList.add(makeVgDataSaveDto(vgDataList.getFirst(), round, timeDiff, vgNumber, 1));
        }
        return vgDataSaveDtoList;
    }

    private VgDataSaveDto makeVgDataSaveDto(Map<String, String> data, int round, int timeDiff, int vgNumber, int tournamentIndex) {
        return new VgDataSaveDto(vgNumber, data.get("team1Score"), data.get("team2Score"),
                Integer.parseInt(data.get("team1Index")), Integer.parseInt(data.get("team2Index")),
                round, tournamentIndex, timeDiff);
    }

}
