package wiki.feh.apitest.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class jsouptest {

    public String getDataFromWeb() throws IOException {
        Document doc = Jsoup.connect("https://support.fire-emblem-heroes.com/voting_gauntlet/tournaments/53").get();
        Elements p_ = doc.select("p");
        StringBuilder output = new StringBuilder();
        for(Element value : p_)
        {
            System.out.println(value);
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

            //TODO :: VG 라운드 변경날짜 16~17시일때, 현재라운드 데이터는 없는상태인데 요소는 존재하는지 (즉 0으로 되어잇는지 아예없는지?) 확인필요 일단은 예외처리 해놓기

            Element team1_div = tb.children().first();
            String team1_number = team1_div.attr("class").replace("tournaments-art-left art-" + VGnum+"-","")
                    .replace("-behind", "").replace("-normal", "");
            String team1_name = team1_div.select("p").get(0).text();
            String team1_score = team1_div.select("p").get(1).text();

            System.out.println (team1_number + team1_name + team1_score);


            Element team2_div = tb.children().get(1);
            String team2_number = team2_div.attr("class").replace("tournaments-art-right art-" + VGnum+"-","")
                    .replace("-behind", "").replace("-normal", "");
            String team2_name = team2_div.select("p").get(0).text();
            String team2_score = team2_div.select("p").get(1).text();

            System.out.println (team2_number + team2_name + team2_score);

            output = output + team1_number + " : " + team1_name + " " + team1_score + "<br>"
            + team2_number + " : " +team2_name + " " + team2_score + "<br>";
        }
        return output;
    }

    public void timetest()
    {
        LocalDate startdate = LocalDate.of(2021,11,24);
        LocalDateTime nowdatetime1 = LocalDateTime.of(2021, 11, 24, 17, 5);

        LocalDateTime nowdatetime2 = LocalDateTime.of(2021, 11, 30, 13, 5);

        LocalDateTime startdate_withTime = startdate.atTime(16,0);

        Long diff = ChronoUnit.HOURS.between(startdate_withTime, nowdatetime1);
        Long diff2 = ChronoUnit.HOURS.between(startdate_withTime, nowdatetime2);

        System.out.println(diff + "\n" + diff2);
    }

    //@Scheduled(cron = "*/10 * * * * *")
    public void crontest()
    {
        timetest();
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
