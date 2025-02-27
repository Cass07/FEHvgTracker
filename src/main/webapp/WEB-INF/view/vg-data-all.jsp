<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% request.setCharacterEncoding("UTF-8"); %>

<jsp:include page="layout/header_vgDataDetail.jsp">
    <jsp:param value="${header_title}" name="header_title"/>
</jsp:include>

<style>
    .table-center{
        width : 80%;
        margin-left : 10%;
        text-align: center;
    }
    h4{
        text-align: center;
    }
    .teamIconName{
        display: inline-block;
        margin: 5px 5px 0px 5px;
        text-align: center;
    }
    .teamIconName>span{
        display : inline-block;
    }
    .losing_1 td:nth-child(1){
        background-color: #ffe2e2;
    }
    .finish .losing_1 td:nth-child(2){
        background-color: #e2feff;
    }
    .finish .losing_2 td:nth-child(1){
        background-color: #e2feff;
    }
    .losing_2 td:nth-child(2){
        background-color: #ffe2e2;
    }
</style>

<div class = "container" style = "padding-top : 20px;">
    <div class = "col-md-12" style = "padding-top : 20px">

        <input type = "hidden" id = "team1Name" value = "${team1Name}">
        <input type = "hidden" id = "team1Id" value = "${team1Id}">
        <input type = "hidden" id = "team2Name" value = "${team2Name}">
        <input type = "hidden" id = "team2Id" value = "${team2Id}">
        <input type = "hidden" id = "team3Name" value = "${team3Name}">
        <input type = "hidden" id = "team3Id" value = "${team3Id}">
        <input type = "hidden" id = "team4Name" value = "${team4Name}">
        <input type = "hidden" id = "team4Id" value = "${team4Id}">
        <input type = "hidden" id = "team5Name" value = "${team5Name}">
        <input type = "hidden" id = "team5Id" value = "${team5Id}">
        <input type = "hidden" id = "team6Name" value = "${team6Name}">
        <input type = "hidden" id = "team6Id" value = "${team6Id}">
        <input type = "hidden" id = "team7Name" value = "${team7Name}">
        <input type = "hidden" id = "team7Id" value = "${team7Id}">
        <input type = "hidden" id = "team8Name" value = "${team8Name}">
        <input type = "hidden" id = "team8Id" value = "${team8Id}">


        <jsp:include page="layout/vg-data-menu.jsp">
            <jsp:param value="${customUri}" name="customUri"/>
        </jsp:include>

        <h2>전체 투표대전 조회</h2>
        <select id = "vdInfoDropdown" style = "width : 100%">
        </select>
        <br>

        <h3>${vgInfo.vgNumber}회 투표대전 : ${vgInfo.vgTitle}</h3>
        <br>
        <p>기간 : ${vgStartDateTimeStr} ~ ${vgEndDateTimeStr}</p>
        <div class="col-md-12">
            <jsp:include page="vg-data-information.jsp"/>
        </div>
    </div>
</div>

<script src = "https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>

<jsp:include page="layout/footer_vg.jsp"/>

<script type = "text/javascript">

    const team1IdName = [$("#team1Id").val(),$("#team1Name").val()];
    const team2IdName = [$("#team2Id").val(),$("#team2Name").val()];
    const team3IdName = [$("#team3Id").val(),$("#team3Name").val()];
    const team4IdName = [$("#team4Id").val(),$("#team4Name").val()];
    const team5IdName = [$("#team5Id").val(),$("#team5Name").val()];
    const team6IdName = [$("#team6Id").val(),$("#team6Name").val()];
    const team7IdName = [$("#team7Id").val(),$("#team7Name").val()];
    const team8IdName = [$("#team8Id").val(),$("#team8Name").val()];

    const customUri = "/voting";

    $(document).ready(function(){
        init_texts();
        init_dropdown_vglist();
    });

    function init_texts()
    {
        $(".team1").each(function () {
            $(this).html(makeTextbyTeamIdName(team1IdName));
        });
        $(".team2").each(function () {
            $(this).html(makeTextbyTeamIdName(team2IdName));
        });
        $(".team3").each(function () {
            $(this).html(makeTextbyTeamIdName(team3IdName));
        });
        $(".team4").each(function () {
            $(this).html(makeTextbyTeamIdName(team4IdName));
        });
        $(".team5").each(function () {
            $(this).html(makeTextbyTeamIdName(team5IdName));
        });
        $(".team6").each(function () {
            $(this).html(makeTextbyTeamIdName(team6IdName));
        });
        $(".team7").each(function () {
            $(this).html(makeTextbyTeamIdName(team7IdName));
        });
        $(".team8").each(function () {
            $(this).html(makeTextbyTeamIdName(team8IdName));
        });
        $(".score").each(function () {
            $(this).text(Number($(this).text()).toLocaleString("us"));
        });
    }

    $(document).delegate("#vdInfoDropdown", 'select2:select', function(e){

        let data = e.params.data;
        if(data.id >= 0)
        {
            window.location.href = customUri + '/vg/past/'+data.id;
        }

    });

    function init_dropdown_vglist()
    {
        $.ajax({
            url : customUri + '/api/v1/vginfo',
            type : "GET",
            dataType : "json",
            contentType: 'application/json; charset=utf-8',
            success:function(result){
                //console.log(result[2]['id']);
                result.pop();
                $('#vdInfoDropdown').html('');
                $('#vdInfoDropdown').select2({
                    data:result,
                    placeholder : "투표대전 데이터 리스트",
                    width : 'element'});
            },error:function(result)
            {
                //console.log(result);
            }
        });
    }

    function makeTextbyTeamIdName(teamIdName)
    {
        let imageStr = "<img alt=\""+teamIdName[0]+"\" src=\"https://feh.wiki/images/hero/"+teamIdName[0]+".webp\" height=\"60px\">";
        return "<div class = \"teamIconName\"><span>"+imageStr+"</span><br>"+teamIdName[1]+"</div>";
    }

</script>