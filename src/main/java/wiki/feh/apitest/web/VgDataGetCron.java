package wiki.feh.apitest.web;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wiki.feh.apitest.domain.vginfo.VgInfo;
import wiki.feh.apitest.service.posts.PostsService;
import wiki.feh.apitest.service.vgdata.VgDataService;
import wiki.feh.apitest.service.vginfo.VgInfoService;
import wiki.feh.apitest.web.dto.PostsSaveRequestDto;
import wiki.feh.apitest.web.dto.VgDataSaveDto;
import wiki.feh.apitest.web.dto.VgInfoGetDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Cron을 실행시키는 이 클래스는 어떤 패키지로 보내는 게 좋은가? Global? 생각해볼것
@RequiredArgsConstructor
@Component
public class VgDataGetCron {
    private final VgDataService vgDataService;
    private final VgInfoService vgInfoService;

    private final PostsService postsService;

    private VgInfoGetDto currentVgInfo = null;

    @Scheduled(cron = "0 5 * * * *") //* 5 * * * * 매 시 5분, 15분에 실행 (한시간에 한번만 실행할까?)
    public void GetVgData() throws Exception {

        initCurrentVgInfo(LocalDateTime.now());

        if (currentVgInfo != null)
        {
            LocalDateTime currentTime = LocalDateTime.now();

            Long timeDiffAll = getTimeDiff(currentVgInfo.getVgStartDate(), currentTime);
            //라운드 구하기
            int roundNumber = getRoundbyTimediff(timeDiffAll);

            Long timeDiff = getTimeDiff(currentVgInfo.getVgStartDate(), currentTime, roundNumber);

            saveVgdatabyRoundTimediffVgnum(roundNumber, timeDiff, currentVgInfo.getVgNumber());
            //라운드와 시간인덱스 가지고 저장할 데이터 리스트 색출해서 저장 (1, 2라운드 시간인덱스 45 46 47은 휴식해야함 다음라운드 0에서 결과 받아오게됨)

            if(timeDiffAll == 141) {
                currentVgInfo = null;
            }
            //마지막 데이터라면 (시간인덱스 141) 종료처리하기 (null값으로)
        }
    }

    private void initCurrentVgInfo(LocalDateTime currentTime)
    {
        //currentvginfo가 null이면 새로 조회해서 값을 저장하고 값이 잇다면 걍넘김 (vg가 끝나면 null값으로 다시 초기화함)
        if(currentVgInfo == null) {
            currentVgInfo = vgInfoService.getLatestVgInfo();
            if(currentVgInfo != null) {
                long diff = getTimeDiff(currentVgInfo.getVgStartDate(), currentTime);
                if (diff <= 0 || diff > 141)//시작날짜 이전이거나 시간날짜로부터 마지막 라운드가 끝낫다면 null로함.
                {
                    if(diff <= 0)
                    {
                        postsService.save(new PostsSaveRequestDto("로그 : null", "없음", "kjh95828@gmail.com"));
                    }
                    currentVgInfo = null;
                }
            }
        }
    }

    private boolean saveVgdatabyRoundTimediffVgnum(int round, Long timeDiff, int vgNumber) throws Exception {
        int timeDiffReal = timeDiff.intValue();
        if(round == 1 && timeDiff == 0)
        {
            postsService.save(new PostsSaveRequestDto("로그 : " + vgNumber, "1라운드 시간차 0입니다.", "kjh95828@gmail.com"));
            return false;
        }
        if((round == 1 || round == 2) && (timeDiff >= 45 && timeDiff <= 47))
        {
            postsService.save(new PostsSaveRequestDto("로그 : " + vgNumber, round + "라운드 시간차 "+ timeDiff +"입니다.", "kjh95828@gmail.com"));
            //계산중일때는 <p>태그 하나에 계산중입니다 이거만써잇음
            return false;
        }else
        {
            List<Map<String, String>> data = getMapListbyCrawl(vgNumber);
            int dataSize = data.size();
            if(dataSize == 4)
            {
                if(timeDiff == 48)
                {
                    timeDiffReal = 45;
                }

                for(int i = 0; i < 4; i++)
                {
                    saveFromMap(data.get(i), round, timeDiffReal, vgNumber, i+1);
                }
            }else if(dataSize == 6)
            {
                if(timeDiff == 48)
                {
                    timeDiffReal = 45;
                }
                if(round == 1)
                {
                    for(int i = 2; i < 6; i++)
                    {
                        saveFromMap(data.get(i), round, timeDiffReal, vgNumber, i-1);
                    }
                }else
                {
                    for(int i = 0; i < 2; i++)
                    {
                        saveFromMap(data.get(i), round, timeDiffReal, vgNumber, i+1);
                    }
                }
            }else if(dataSize == 7)
            {
                if(round == 2 && timeDiff == 48)
                {
                    timeDiffReal = 45;
                    for(int i = 1; i < 3; i++)
                    {
                        saveFromMap(data.get(i), round, timeDiffReal, vgNumber, i);
                    }
                }else
                {
                    saveFromMap(data.get(0), round, timeDiffReal, vgNumber, 1);
                }
            }
            return true;
        }
    }

    private void saveFromMap(Map<String, String> data, int round, int timeDiff, int vgNumber, int tournamentIndex)
    {
        vgDataService.save(new VgDataSaveDto(vgNumber, data.get("team1Score"), data.get("team2Score"),
                Integer.parseInt(data.get("team1Index")), Integer.parseInt(data.get("team2Index")),
                round, tournamentIndex, timeDiff));
    }

    private List<Map<String, String>> getMapListbyCrawl(int vgNumber) throws Exception
    {
        Document doc = Jsoup.connect("https://support.fire-emblem-heroes.com/voting_gauntlet/tournaments/" + vgNumber).get();
        Elements tb_ = doc.select(".tournaments-battle");
        List<Map<String, String>> output = new ArrayList<>();
        String toPost = "";

        for(Element tb : tb_)
        {
            //tournament battle마다 한 배틀에 참여하는 두 팀의 div가 들어있다
            //div 안의 p 요소에 이름과 점수 데이터가 들어 있다.

            //TODO :: VG 라운드 변경날짜 16~17시일때, 현재라운드 데이터는 없는상태인데 요소는 존재하는지 (즉 0으로 되어잇는지 아예없는지?) 확인필요
            // 이거 확인해본결과 16~17시에 현재라운드 요소는 존재하고 대신 표수 <p> 태그엔 값없었음

            Element team1_div = tb.children().first();
            String team1_number = team1_div.attr("class").replace("tournaments-art-left art-" + vgNumber+"-","")
                    .replace("-behind", "").replace("-normal", "");
            String team1_name = team1_div.select("p").get(0).text();
            String team1_score = team1_div.select("p").get(1).text();


            Element team2_div = tb.children().get(1);
            String team2_number = team2_div.attr("class").replace("tournaments-art-right art-" + vgNumber+"-","")
                    .replace("-behind", "").replace("-normal", "");
            String team2_name = team2_div.select("p").get(0).text();
            String team2_score = team2_div.select("p").get(1).text();


            toPost = toPost + "<br>" +  team1_number + team1_name + team1_score + "<br>"
                    + team2_number + team2_name + team2_score;

            output.add(new HashMap<String, String>(){{
                put("team1Index", team1_number);
                put("team1Score", team1_score.replace(",", ""));
                put("team2Index", team2_number);
                put("team2Score", team2_score.replace(",", ""));
            }});
        }
        postsService.save(new PostsSaveRequestDto("로그 : " + vgNumber + " 수집완료", toPost, "kjh95828@gmail.com"));
        return output;
    }

    private int getRoundbyTimediff(Long timeDiff)
    {
        return ((timeDiff.intValue() - 1)/ 48) + 1;
    }

    private Long getTimeDiff(LocalDate vgStartDate, LocalDateTime currentTime)
    {
        return ChronoUnit.HOURS.between(vgStartDate.atTime(16,0), currentTime);
    }

    private Long getTimeDiff(LocalDate vgStartDate, LocalDateTime currentTime, int round)
    {
        return ChronoUnit.HOURS.between(vgStartDate.plusDays(round * 2 - 2).atTime(16,0), currentTime);
    }
}
