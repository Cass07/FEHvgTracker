package wiki.feh.apitest.service.global;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiki.feh.apitest.service.vgdata.VgDataService;
import wiki.feh.apitest.service.vginfo.VgInfoService;
import wiki.feh.apitest.web.dto.VgDataGetDto;
import wiki.feh.apitest.web.dto.VgDataResultGetDto;
import wiki.feh.apitest.web.dto.VgInfoGetDto;
import wiki.feh.apitest.web.dto.VgViewDto;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class VgViewService {
    //뷰에 출력할 vgdata와 vginfo를 조회해서 넘겨줄 서비스임 그래서 vginfoservice랑 vgdataservice 주입받앗음
    private final VgInfoService vgInfoService;
    private final VgDataService vgDataService;

    @Transactional(readOnly = true)
    public VgViewDto getVgMainbyid(Long id)
    {
        //vgnum이 -1이라면 제일 최신의 VgInfodata를 조회한다.
        Map<String, String> viewModel = new HashMap<String, String>();
        VgInfoGetDto vginfoEntity;

        List<VgDataGetDto> currentRoundVgdata = null;
        List<VgDataResultGetDto> round1Vgdata = null;
        List<VgDataResultGetDto> round2Vgdata = null;
        List<VgDataResultGetDto> round3Vgdata = null;
        
        if(id == -1)
        {
            vginfoEntity = vgInfoService.getLatestVgInfo();
        }else
        {
            vginfoEntity = vgInfoService.findbyId(id);
        }

        if(vginfoEntity == null){
            return VgViewDto.builder()
                    .viewString("posts-error")
                    .viewModel(new HashMap<String, String>(){{put("errorMessage", "오류가 발생했습니다.<br>해당 투표대전 조회되지 않음.");}})
                    .build();
        }

        putHeroNamdIdtoMapByVginfo(viewModel, vginfoEntity);

        int latestVgNumber = vginfoEntity.getVgNumber();
        List<VgDataResultGetDto> resultVgDataList = vgDataService.getLatestVgDataListbyVgNumber(latestVgNumber);
        int resultVgDataSize = resultVgDataList.size();

        int round;

        switch(resultVgDataSize)
        {
            case 0 :
                round = 1;
                break;
            case 4:
                round = 2;
                break;
            case 6:
                round = 3;
                break;
            case 7:
                round = 0;
                break;
            default:
                round = -1;
        }

        if(round == -1)
        {
            return VgViewDto.builder()
                    .viewString("posts-error")
                    .viewModel(new HashMap<String, String>(){{put("errorMessage", "오류가 발생했습니다.<br>해당 투표대전의 종료된 라운드 데이터 값에 오류가 있음.");}})
                    .build();
        }

        List<VgDataGetDto> currentVgDataList = vgDataService.getNowtimeVgDataListbyVgNumberRound(latestVgNumber, round);

        if(currentVgDataList.size() == 0)
        {
            //아예 시작안햇거나(라운드1), 이전라운드 종료되엇고 현재라운드 시작안햇거나, 모든 라운드 종료됨
            if(round == 1) {
                //아예 시작안함 1라운드 예정 대진표만 출력
                //데이터
                viewModel.put("round1StartNot", "1라운드 대진표");
            }
        }else{
            //진행중인 라운드 데이터 넘겨줌
            viewModel.put("currentRoundVgTitle", round+"라운드 진행 중");
            currentRoundVgdata = currentVgDataList;
        }

        //해당 라운드면 어쨌든 그 전 라운드는 종료된상태이니 종료라운드 데이터 넘겨줌
        if(round == 2)
        {
            viewModel.put("round1VgTitle", "1라운드 결과");
            round1Vgdata = resultVgDataList;
        }
        else if(round == 3)
        {
            viewModel.put("round2VgTitle", "2라운드 결과");
            round2Vgdata = resultVgDataList.subList(0,2);
            viewModel.put("round1VgTitle", "1라운드 결과");
            round1Vgdata = resultVgDataList.subList(2,6);
        }else if(round == 0)
        {
            viewModel.put("round3VgTitle", "3라운드 결과");
            round3Vgdata = resultVgDataList.subList(0,1);
            viewModel.put("round2VgTitle", "2라운드 결과");
            round2Vgdata = resultVgDataList.subList(1,3);
            viewModel.put("round1VgTitle", "1라운드 결과");
            round1Vgdata = resultVgDataList.subList(3,7);
        }

        viewModel.put("vgStartDateTimeStr", vginfoEntity.getVgStartDate().atTime(16,0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        viewModel.put("vgEndDateTimeStr", vginfoEntity.getVgStartDate().atTime(13,0).plusDays(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return VgViewDto.builder()
                .viewString("vg-data-main")
                .currentRoundVgdata(currentRoundVgdata)
                .round1Vgdata(round1Vgdata)
                .round2Vgdata(round2Vgdata)
                .round3Vgdata(round3Vgdata)
                .vgInfoEntity(vginfoEntity)
                .viewModel(viewModel)
                .build();

    }

    @Transactional(readOnly = true)
    public VgViewDto getVgFirstbyId(Long id)
    {
        Map<String, String> viewModel = new HashMap<String, String>();
        VgInfoGetDto vginfoEntity;

        List<VgDataGetDto> currentRoundVgdata = null;
        List<VgDataResultGetDto> round1Vgdata = null;
        List<VgDataResultGetDto> round2Vgdata = null;
        List<VgDataResultGetDto> round3Vgdata = null;

        if(id == -1)
        {
            vginfoEntity = vgInfoService.getLatestVgInfo();
        }else
        {
            vginfoEntity = vgInfoService.findbyId(id);
        }

        if(vginfoEntity == null){
            return VgViewDto.builder()
                    .viewString("posts-error")
                    .viewModel(new HashMap<String, String>(){{put("errorMessage", "오류가 발생했습니다.<br>해당 투표대전 조회되지 않음.");}})
                    .build();
        }

        putHeroNamdIdtoMapByVginfo(viewModel, vginfoEntity);

        viewModel.put("header_title", "투표대전 점수 트래커 - 초동 데이터");

        int firstVgNumber = vginfoEntity.getVgNumber();
        List<VgDataResultGetDto> firstVgDataList = vgDataService.getFirstVgDataResultListbyVgNumber(firstVgNumber);
        int firstVgDataSize = firstVgDataList.size();

        int round;

        switch(firstVgDataSize)
        {
            case 4 :
                round = 1;
                break;
            case 6:
                round = 2;
                break;
            case 7:
                round = 3;
                break;
            case 0:
                round = 0;
                break;
            default:
                round = -1;
        }

        if(round == -1)
        {
            return VgViewDto.builder()
                    .viewString("posts-error")
                    .viewModel(new HashMap<String, String>(){{put("errorMessage", "오류가 발생했습니다.<br>해당 투표대전의 초동 데이터 값에 오류가 있음.");}})
                    .build();
        }

        if(round == 1)
        {
            viewModel.put("round1VgTitle", "1라운드 5시 데이터");
            round1Vgdata = firstVgDataList;
        }
        else if(round == 2)
        {

            viewModel.put("round2VgTitle", "2라운드 5시 데이터");
            round2Vgdata = firstVgDataList.subList(0,2);
            viewModel.put("round1VgTitle", "1라운드 5시 데이터");
            round1Vgdata = firstVgDataList.subList(2,6);
        }else if(round == 3)
        {
            viewModel.put("round3VgTitle", "3라운드 5시 데이터");
            round3Vgdata = firstVgDataList.subList(0,1);
            viewModel.put("round2VgTitle", "2라운드 5시 데이터");
            round2Vgdata = firstVgDataList.subList(1,3);
            viewModel.put("round1VgTitle", "1라운드 5시 데이터");
            round1Vgdata = firstVgDataList.subList(3,7);
        }

        viewModel.put("vgStartDateTimeStr", vginfoEntity.getVgStartDate().atTime(16,0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        viewModel.put("vgEndDateTimeStr", vginfoEntity.getVgStartDate().atTime(13,0).plusDays(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return VgViewDto.builder()
                .viewString("vg-data-main")
                .currentRoundVgdata(currentRoundVgdata)
                .round1Vgdata(round1Vgdata)
                .round2Vgdata(round2Vgdata)
                .round3Vgdata(round3Vgdata)
                .vgInfoEntity(vginfoEntity)
                .viewModel(viewModel)
                .build();
    }

    //vginfo에서 캐릭터 아이디랑 이름 데이터 받아서 model 데이터 저장하는 map에다 저장해줌
    private void putHeroNamdIdtoMapByVginfo(Map<String, String> map, VgInfoGetDto vgInfoEntity)
    {
        for(int i = 1; i <= 8; i++)
        {
            String[] tmp = vgInfoEntity.getTeamIdbyIndex(i).split("#");
            map.put("team"+i+"Id", tmp[0]);
            map.put("team"+i+"Name", tmp[1]);
        }
    }


}
