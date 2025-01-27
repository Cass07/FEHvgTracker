package wiki.feh.apitest.util;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import wiki.feh.apitest.event.PostEvent;
import wiki.feh.apitest.global.exception.VgConnectionFailedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class VgDataCrawl {
    private final ApplicationEventPublisher applicationEventPublisher;

    protected Document getDocument(int vgNumber) {
        Document doc;
        try {
            doc = Jsoup.connect("https://support.fire-emblem-heroes.com/voting_gauntlet/tournaments/" + vgNumber).get();
        } catch (Exception e) {
            throw new VgConnectionFailedException();
        }
        return doc;
    }

    public List<Map<String, String>> getMapListByCrawl(int vgNumber){
        Document doc = getDocument(vgNumber);
        Elements tbs = doc.select(".tournaments-battle");
        List<Map<String, String>> output = new ArrayList<>();
        StringBuilder logPostBuilder = new StringBuilder();

        for (Element tb : tbs) {
            // tournament battle마다 한 배틀에 참여하는 두 팀의 div가 들어있다
            // div 안의 p 요소에 이름과 점수 데이터가 들어 있다.
            // 점수 데이터는 콤마가 포함된 정수이거나 빈 문자열이다

            Element team1_div = tb.children().first();
            String team1_number = team1_div.attr("class").replace("tournaments-art-left art-" + vgNumber + "-", "")
                    .replace("-behind", "").replace("-normal", "");
            String team1_name = team1_div.select("p").get(0).text();
            String team1_score = team1_div.select("p").get(1).text();


            Element team2_div = tb.children().get(1);
            String team2_number = team2_div.attr("class").replace("tournaments-art-right art-" + vgNumber + "-", "")
                    .replace("-behind", "").replace("-normal", "");
            String team2_name = team2_div.select("p").get(0).text();
            String team2_score = team2_div.select("p").get(1).text();


            logPostBuilder.append("<br>")
                    .append(team1_number)
                    .append(team1_name)
                    .append(team1_score)
                    .append("<br>")
                    .append(team2_number)
                    .append(team2_name)
                    .append(team2_score);

            output.add(new HashMap<>() {{
                put("team1Index", team1_number);
                put("team1Score", team1_score.replace(",", ""));
                put("team2Index", team2_number);
                put("team2Score", team2_score.replace(",", ""));
            }});
        }
        applicationEventPublisher.publishEvent(new PostEvent( "로그 : " + vgNumber + " 수집완료", logPostBuilder.toString(), "kjh95828@gmail.com"));
        return output;
    }
}
