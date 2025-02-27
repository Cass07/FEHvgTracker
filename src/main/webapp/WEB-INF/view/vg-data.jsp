<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% request.setCharacterEncoding("UTF-8"); %>

<jsp:include page="layout/header_vgDataDetail.jsp">
    <jsp:param value="${header_title}" name="header_title"/>
</jsp:include>

<style>
    .losing_1 td:nth-child(7) {
        background-color: #ffe2e2;
    }

    .losing_2 td:nth-child(8) {
        background-color: #ffe2e2;
    }

    .score {
        text-align: right;
    }

    #mainTable {
        font-variant-numeric: tabular-nums;
        -moz-font-feature-settings: "tnum";
        -webkit-font-feature-settings: "tnum";
        font-feature-settings: "tnum";
    }

</style>

<div class="container" style="padding-top : 20px;">
    <div class="col-md-12" style = "padding-top : 20px">
        <h3>${vgNumber}회 투표대전 : ${vg_info.vgTitle}</h3>
        <h4>${roundNumber}라운드 : ${team1_name} vs ${team2_name}</h4>
        <canvas id=basicGraph></canvas>
        <br>
        <canvas id=compareGraph></canvas>
        <br>
        <div class="form-row col-md-12">
            <input type=hidden id=vgnum value= ${vgNumber}>
            <input type=hidden id=round value= ${roundNumber}>
            <input type=hidden id=tournum value= ${tournum}>
            <input type=hidden id=team1name value="${team1_name}">
            <input type=hidden id=team2name value="${team2_name}">
            <table id="mainTable" class="table table-hover col-md-12" style = "font-size : 70%">
                <thead>
                <tr style = "text-align: center">
                    <th>시간</th>
                    <th>경과<br>시간</th>
                    <th>열세<br>배수</th>
                    <th>배수</th>
                    <th>${team1_name} 점수</th>
                    <th>${team2_name} 점수</th>
                    <th colspan=2>우열세 배율</th>
                    <th colspan="2">점수 증가량</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="layout/footer_vg.jsp"/>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.6.0/chart.min.js"></script>

<script type="text/javascript">
    //TODO :: 데이터 조회된 거 없으면 그래프는 출력하지말고 테이블에 데이터 없다고 표시할것

    const vgnum = $("#vgnum").val();
    const round = $("#round").val();
    const tournum = $("#tournum").val();
    const team1Name = $("#team1name").val();
    const team2Name = $("#team2name").val();

    const losingMul = [3.2, 3.4, 3.6, 3.8, 4, 4.2, 4.4, 4.6, 4.8, 5, 5.2, 5.4, 5.6, 5.8, 6, 6.2, 6.4,
        6.6, 6.8, 7, 7.2, 7.4, 7.6, 7.8, 8, 8.2, 8.4, 8.6, 8.8, 9, 9.2, 9.4, 9.6, 9.8, 10, 10.2,
        10.4, 10.6, 10.8, 11, 11.2, 11.4, 11.6, 11.8, 12,""];
    const winningMul = [1.05, 1.1, 1.15, 1.2, 1.25, 1.3, 1.35, 1.4, 1.45, 1.5, 1.55, 1.6, 1.65, 1.7,
        1.75, 1.8, 1.85, 1.9, 1.95, 2, 2.05, 2.1, 2.15, 2.2, 2.25, 2.3, 2.35, 2.4, 2.45, 2.5,
        2.55, 2.6, 2.65, 2.7, 2.75, 2.8, 2.85, 2.9, 2.95, 3, 3.05, 3.1, 3.15, 3.2, 3.25,""];

    //const customUri = "/voting";
    const customUri = "";

    $(document).ready(function () {

        let vgData = getVgScoreData(vgnum, round, tournum);

        /*
        $(".score").each(function () {
            $(this).text(Number($(this).text()).toLocaleString("us"));
        });

         */

        updateTablebyData(vgData);

        let chartData = makeDatasetsByScoredata(vgData, team1Name, team2Name);

        new Chart(
            document.getElementById("basicGraph"),
            {
                data: chartData.basicGraph,
                options: {
                    scales: {
                        y: {
                            ticks: {
                                display: false
                            }
                        }
                    },
                    borderWidth: 1,
                    pointRadius: 1.5
                }
            }
        );

        new Chart(
            document.getElementById("compareGraph"),
            {
                data: chartData.compareGraph,
                options: {
                    scales: {
                        y: {
                            ticks: {
                                display: false
                            }
                        }
                    },
                    borderWidth: 1,
                    pointRadius: 1.3
                }
            }
        );

    });

    function updateTablebyData(data)
    {
        let lastscoreTeam1 = 0;
        let lastscoreTeam2 = 0;

        for(key in data)
        {
            let team1score = data[key].team1Score;
            let team2score = data[key].team2Score;
            $("#mainTable>tbody:last").append(makeTrbyDatas(data[key].timeIndex, (Number)(team1score).toLocaleString('us'),
                (Number)(team2score).toLocaleString('us'), data[key].losing,
                data[key].team1Rate, data[key].team2Rate, (Number)(team1score - lastscoreTeam1).toLocaleString('us'),
                (Number)(team2score - lastscoreTeam2).toLocaleString('us')));
            lastscoreTeam1 = team1score;
            lastscoreTeam2 = team2score;
        }
    }


    function makeTrbyDatas(time,team1score, team2score, losing, team1rate, team2rate, team1scorecom, team2scorecom)
    {
        let realtime = (time + 16) % 24;
        return "<tr class = \"losing_" + losing + "\">"
            + "<td>" + realtime + "</td><td>" + time + "</td>"
            + "<td>" + losingMul[time] +"</td><td>" + winningMul[time] + "</td>"
            + "<td class = \"score\">" + team1score + "</td><td class = \"score\">" + team2score + "</td>"
            + "<td>" + team1rate + "</td><td>" + team2rate + "</td>"
            + "<td class = \"score\">" + team1scorecom + "</td><td class = \"score\">" + team2scorecom + "</td>"
            + "</tr>";
    }

    function makeDatasetsByScoredata(data, team1name, team2name) {
        let label = [];
        let team1score = [];
        let team2score = [];
        let scoreComp = [];
        let ifTeam1Lose = [];
        let ifTeam2Lose = [];
        let losingScore = [];
        let losingColor = [];

        let output = {};

        let losing1String = "rgb(119,39,39)";
        let losing2String = "rgb(39,71,119)";
        let losingNot = "rgb(255,255,255)"
        let compareColor = "rgb(75,119,39)";

        //라벨과 숫자 데이터 array로 따로 저장
        //비교그래프용으로 팀1-팀2, 팀1점수 동일할때 팀2가 우세하기 위해 필요한점수차, 반대
        label.push(16);
        team1score.push(0);
        team2score.push(0);
        scoreComp.push(0);
        ifTeam1Lose.push(0);
        ifTeam2Lose.push(0);
        losingScore.push(0);
        losingColor.push(losingNot);

        for (key in data) {
            let team1Score = data[key].team1Score;
            let team2Score = data[key].team2Score;

            label.push(((Number)(data[key].timeIndex) + 16) % 24);
            team1score.push(team1Score);
            team2score.push(team2Score);
            scoreComp.push(team1Score - team2Score);
            ifTeam1Lose.push(Number(team1Score) * 0.01);
            ifTeam2Lose.push(Number(team2Score) * -0.01);

            let losingCurrent = (Number)(data[key].losing);
            switch (losingCurrent) {
                case 1:
                    losingScore.push(team1Score);
                    losingColor.push(losing1String);
                    break;
                case 2:
                    losingScore.push(team2Score);
                    losingColor.push(losing2String);
                    break;
                default:
                    losingScore.push(0);
                    losingColor.push(losingNot);
                    break;
            }
        }

        let basicGraph = {};

        basicGraph.labels = label;
        basicGraph.datasets = [{
            label: team1name,
            type: "line",
            backgroundColor: losing1String,
            borderColor: losing1String,
            data: team1score,
        },
            {
                label: team2name,
                type: "line",
                backgroundColor: losing2String,
                borderColor: losing2String,
                data: team2score,
            },
            {
                label: "열세",
                type: "bar",
                backgroundColor: losingColor,
                data: losingScore,
                barThickness: 1
            }];

        let compareGraph = {};
        compareGraph.labels = label;
        compareGraph.datasets = [
            {
                label: "점수차",
                type: "line",
                backgroundColor: compareColor,
                borderColor: compareColor,
                data: scoreComp,
            },
            {
                label: team1name,
                type: "line",
                backgroundColor: losing1String,
                borderColor: losing1String,
                data: ifTeam1Lose,
            },
            {
                label: team2name,
                type: "line",
                backgroundColor: losing2String,
                borderColor: losing2String,
                data: ifTeam2Lose,
            }
        ];

        output.basicGraph = basicGraph;
        output.compareGraph = compareGraph;
        return output;
    }

    function getVgScoreData(vgnum, round, tournum) {
        let data = null;
        $.ajax({
            url: customUri + '/api/v1/vgdata/vgnum/' + vgnum + '/round/' + round + '/tournum/' + tournum,
            type: "GET",
            dataType: "json",
            async: false,
            success: function (result) {
                data = result;
            }
            , error: function (result) {
                alert("오류가 발생했습니다.");
                //TODO :: 메인페이지로 보낼것
            }
        })

        return data;
    }

</script>