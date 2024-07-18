<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<c:if test="${!empty round1StartNot}">
    <h4>${round1StartNot}</h4>
    <table id="currentTable" class = "table table-center">
        <colgroup>
            <col width = "50%">
            <col width = "50%">
        </colgroup>
        <thead>
        </thead>
        <tbody>
        <tr>
            <td class = "team1"></td>
            <td class = "team2"></td>
        </tr>
        <tr>
            <td class = "team3"></td>
            <td class = "team4"></td>
        </tr>
        <tr>
            <td class = "team5"></td>
            <td class = "team6"></td>
        </tr>
        <tr>
            <td class = "team7"></td>
            <td class = "team8"></td>
        </tr>
        </tbody>
    </table>
</c:if>

<c:if test="${!empty currentRoundVgTitle}">
    <h4>${currentRoundVgTitle}</h4>
    <table id="currentTable" class = "table table-center">
    <colgroup>
        <col width = "50%">
        <col width = "50%">
    </colgroup>
    <thead>
    </thead>
    <tbody>
    <c:if test="${!empty currentRoundVgdata}">
        <c:forEach var="vgData" items="${currentRoundVgdata}">
            <tr>
                <td class = "team${vgData.team1Index}"></td>
                <td class = "team${vgData.team2Index}"></td>
            </tr>
            <tr class = "losing_${vgData.losing}">
                <td class = "score">${vgData.team1Score}</td>
                <td class = "score">${vgData.team2Score}</td>
            </tr>
            <tr>
                <td colspan="2">${vgData.currentMul}</td>
            </tr>
            <tr>
                <td colspan="2">
                    <a href = "${customUri}/vg/vgnum/${vgData.vgNumber}/round/${vgData.roundNumber}/tournum/${vgData.tournamentIndex}" target='_blank'>세부 데이터 보기</a>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${empty currentRoundVgdata}">
        <tr>
            <td colspan="2">진행중인 라운드가 없습니다.</td>
        </tr>
    </c:if>
    </tbody>
    </table>

    <br><br>
</c:if>

<c:if test="${!empty round3VgTitle}">
    <h4>${round3VgTitle}</h4>

    <table id="round3ResultTable" class = "table table-center finish">
        <colgroup>
            <col width = "50%">
            <col width = "50%">
        </colgroup>
        <thead>
        </thead>
        <tbody>
    <c:if test="${!empty round3Vgdata}">
        <c:forEach var="vgData" items="${round3Vgdata}">
            <tr>
                <td class = "team${vgData.team1Index}"></td>
                <td class = "team${vgData.team2Index}"></td>
            </tr>
            <tr class = "losing_${vgData.losing}">
                <td class = "score">${vgData.team1Score}</td>
                <td class = "score">${vgData.team2Score}</td>
            </tr>
            <tr>
                <td colspan="2">${vgData.currentMul}</td>
            </tr>
            <tr>
                <td colspan="2">
                    <a href = "${customUri}/vg/vgnum/${vgData.vgNumber}/round/${vgData.roundNumber}/tournum/${vgData.tournamentIndex}" target='_blank'>세부 데이터 보기</a>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${empty round3Vgdata}">
    <tr>
        <td colspan="2">결과가 없습니다.</td>
    </tr>
    </c:if>
    </tbody>
</table>
<br>
</c:if>

<c:if test="${!empty round2VgTitle}">
    <h4>${round2VgTitle}</h4>

    <table id="round3ResultTable" class = "table table-center finish">
        <colgroup>
            <col width = "50%">
            <col width = "50%">
        </colgroup>
        <thead>
        </thead>
        <tbody>
        <c:if test="${!empty round2Vgdata}">
            <c:forEach var="vgData" items="${round2Vgdata}">
                <tr>
                    <td class = "team${vgData.team1Index}"></td>
                    <td class = "team${vgData.team2Index}"></td>
                </tr>
                <tr class = "losing_${vgData.losing}">
                    <td class = "score">${vgData.team1Score}</td>
                    <td class = "score">${vgData.team2Score}</td>
                </tr>
                <tr>
                    <td colspan="2">${vgData.currentMul}</td>
                </tr>
                <tr>
                    <td colspan="2">
                        <a href = "${customUri}/vg/vgnum/${vgData.vgNumber}/round/${vgData.roundNumber}/tournum/${vgData.tournamentIndex}" target='_blank'>세부 데이터 보기</a>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        <c:if test="${empty round2Vgdata}">
            <tr>
                <td colspan="2">결과가 없습니다.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
    <br>
</c:if>

<c:if test="${!empty round1VgTitle}">
    <h4>${round1VgTitle}</h4>

    <table id="round3ResultTable" class = "table table-center finish">
        <colgroup>
            <col width = "50%">
            <col width = "50%">
        </colgroup>
        <thead>
        </thead>
        <tbody>
        <c:if test="${!empty round1Vgdata}">
            <c:forEach var="vgData" items="${round1Vgdata}">
                <tr>
                    <td class = "team${vgData.team1Index}"></td>
                    <td class = "team${vgData.team2Index}"></td>
                </tr>
                <tr class = "losing_${vgData.losing}">
                    <td class = "score">${vgData.team1Score}</td>
                    <td class = "score">${vgData.team2Score}</td>
                </tr>
                <tr>
                    <td colspan="2">${vgData.currentMul}</td>
                </tr>
                <tr>
                    <td colspan="2">
                        <a href = "${customUri}/vg/vgnum/${vgData.vgNumber}/round/${vgData.roundNumber}/tournum/${vgData.tournamentIndex}" target='_blank'>세부 데이터 보기</a>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        <c:if test="${empty round1Vgdata}">
            <tr>
                <td colspan="2">결과가 없습니다.</td>
            </tr>
        </c:if>
        </tbody>
    </table>
    <br>
</c:if>