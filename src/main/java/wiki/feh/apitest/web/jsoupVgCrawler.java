package wiki.feh.apitest.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class jsoupVgCrawler {

    public String getDataFromWeb() throws IOException {
        Document doc = Jsoup.connect("https://support.fire-emblem-heroes.com/voting_gauntlet/tournaments/53").get();
        Elements p_ = doc.select("p");
        StringBuilder output = new StringBuilder();
        for(Element value : p_)
        {
            log.info(value.text());
            output.append(value.text()).append("<br>");
        }

        return output.toString();
    }

    //title - section-part-title div child span에 있음

    public String getDivFromWeb(int VGnum) throws IOException{
        Document doc = Jsoup.connect("https://support.fire-emblem-heroes.com/voting_gauntlet/tournaments/53").get();
        Elements tb_ = doc.select(".tournaments-battle");

        String output = "";

        for(Element tb : tb_)
        {
            //tournament battle마다 한 배틀에 참여하는 두 팀의 div가 들어있다
            //div 안의 p 요소에 이름과 점수 데이터가 들어 있다.

            Element team1_div = tb.children().first();
            String team1_number = team1_div.attr("class").replace("tournaments-art-left art-" + VGnum+"-","")
                    .replace("-behind", "").replace("-normal", "");
            String team1_name = team1_div.select("p").get(0).text();
            String team1_score = team1_div.select("p").get(1).text();

            log.info(team1_number + team1_name + team1_score);


            Element team2_div = tb.children().get(1);
            String team2_number = team2_div.attr("class").replace("tournaments-art-right art-" + VGnum+"-","")
                    .replace("-behind", "").replace("-normal", "");
            String team2_name = team2_div.select("p").get(0).text();
            String team2_score = team2_div.select("p").get(1).text();

            log.info(team2_number + team2_name + team2_score);

            output = output + team1_number + " : " + team1_name + " " + team1_score + "<br>"
            + team2_number + " : " +team2_name + " " + team2_score + "<br>";
        }
        return output;
    }
}
