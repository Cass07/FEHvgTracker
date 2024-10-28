<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% request.setCharacterEncoding("UTF-8"); %>

<jsp:include page="layout/header.jsp">
    <jsp:param value="${header_title}" name="header_title"/>
</jsp:include>

<link rel = "stylesheet" href = "${customUri}/js/app/assets/bootstrap-datepicker.min.css">

<div class = "container" style = "padding-top : 20px;">
    <div class = "col-md-12">
        <h2>투표대전 데이터 설정</h2>
        <div>
            <select id = "vdInfoDropdown" style = "width : 90%">
            </select>
        </div>
        <form>
            <div class = "form-group" style = "display :none">
                <input type = text id = "id" readonly>
            </div>
            <div class = "form-group">
                <label for = "vg_number">회차수</label>
                <input type = "number" class = "form-control" id = "vg_number">
            </div>
            <div class = "form-group">
                <label for = "vg_start_date">시작 날짜</label>
                <input type = "text" class = "form-control" id = "vg_start_date">
            </div>
            <div class = "form-group">
                <label for = "vg_title">제목</label>
                <input type = "text" class = "form-control" id = "vg_title">
            </div>
            <div class = "form-row">
                <div class = "form-group col-md-6">
                    <label for = "team1id">팀1</label>
                    <select id = "team1id" class = "heroDropdown" style = "width : 100%">
                    </select>
                </div>
                <div class = "form-group col-md-6">
                    <label for = "team5id">팀5</label>
                    <select id = "team5id" class = "heroDropdown" style = "width : 100%">
                    </select>
                </div>
            </div>
            <div class = "form-row">
                <div class = "form-group col-md-6">
                    <label for = "team2id">팀2</label>
                    <select id = "team2id" class = "heroDropdown" style = "width : 100%">
                    </select>
                </div>
                <div class = "form-group col-md-6">
                    <label for = "team6id">팀6</label>
                    <select id = "team6id" class = "heroDropdown" style = "width : 100%">
                    </select>
                </div>
            </div>
            <div class = "form-row">
                <div class = "form-group col-md-6">
                    <label for = "team3id">팀3</label>
                    <select id = "team3id" class = "heroDropdown" style = "width : 100%">
                    </select>
                </div>
                <div class = "form-group col-md-6">
                    <label for = "team7id">팀7</label>
                    <select id = "team7id" class = "heroDropdown" style = "width : 100%">
                    </select>
                </div>
            </div>
            <div class = "form-row">
                <div class = "form-group col-md-6">
                    <label for = "team4id">팀4</label>
                    <select id = "team4id" class = "heroDropdown" style = "width : 100%">
                    </select>
                </div>
                <div class = "form-group col-md-6">
                    <label for = "team8id">팀8</label>
                    <select id = "team8id" class = "heroDropdown" style = "width : 100%">
                    </select>
                </div>
            </div>
            </form>
            <a href = "/voting/admin/board/" role = "button" class = "btn btn-secondary">취소</a>
            <button type = "button" class = "btn btn-primary" id = "VginfoSave" style = "display : none">등록</button>
            <button type = "button" class = "btn btn-success" id = "VginfoUpdate" style = "display : none">수정</button>
    </div>
</div>

<jsp:include page="layout/footer_vg.jsp"/>

<script src="${customUri}/js/app/assets/bootstrap-datepicker.min.js"></script>
<script src="${customUri}/js/app/assets/bootstrap-datepicker.ko.min.js"></script>
<script type = "text/javascript">

    let $submitButton = $("#VginfoSave");
    let $updateButton = $("#VginfoUpdate");

    const customUri = "/voting";

    $submitButton.on("click", function(){
        vginfo_save();
    });

    $updateButton.on("click", function(){
        vginfo_update();
    });

    $(document).delegate("#vdInfoDropdown", 'select2:select', function(e){

        let data = e.params.data;
        if(data.id > 0)
        {
            $submitButton.css("display", "none");
            $updateButton.css("display", "inline-block");
            set_vfInfoForm(data.id);
        }else if(data.id == -1)
        {
            $submitButton.css("display", "inline-block");
            $updateButton.css("display", "none");
            console.log("새데이터입력");
            $('#id').val("");
            $('#vg_number').val("");
            $('#vg_start_date').val("");
            $('#vg_title').val("");
            $('#team1id').val("");
            $('#team2id').val("");
            $('#team3id').val("");
            $('#team4id').val("");
            $('#team5id').val("");
            $('#team6id').val("");
            $('#team7id').val("");
            $('#team8id').val("");
        }

    });

    $(document).ready(function(){
        init_dropdown_vglist();
        init_dropdown_herolist();
        $('#vg_start_date').datepicker({
            format: "yyyy-mm-dd",
            language: "ko",
            autoclose : true
        });

    });

    function set_vfInfoForm(id)
    {
        $.ajax({
            url : customUri + '/api/v1/vginfo/' + id,
            type : "GET",
            dataType : "json",
            success:function(result){
                //console.log(result);
                $('#id').val(result["id"]);
                $('#vg_number').val(result.vgNumber);
                $('#vg_start_date').val(result.vgStartDate);
                $('#vg_title').val(result.vgTitle);
                update_herodropdown("team1id", result.team1Id);
                update_herodropdown("team2id", result.team2Id);
                update_herodropdown("team3id", result.team3Id);
                update_herodropdown("team4id", result.team4Id);
                update_herodropdown("team5id", result.team5Id);
                update_herodropdown("team6id", result.team6Id);
                update_herodropdown("team7id", result.team7Id);
                update_herodropdown("team8id", result.team8Id);
            }
            ,error:function(result){
                alert("오류낫슴");
            }
        });
    }

    function init_dropdown_vglist()
    {
        $.ajax({
            url : customUri + '/api/v1/vginfo',
            type : "GET",
            dataType : "json",
            contentType: 'application/json; charset=utf-8',
            success:function(result){
                //console.log(result[2]['id']);
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

    function vginfo_save()
    {
        let data = {
            vgNumber : $('#vg_number').val(),
            vgTitle : $('#vg_title').val(),
            vgStartDate : $('#vg_start_date').val(),
            team1Id : $('#team1id').val(),
            team2Id : $('#team2id').val(),
            team3Id : $('#team3id').val(),
            team4Id : $('#team4id').val(),
            team5Id : $('#team5id').val(),
            team6Id : $('#team6id').val(),
            team7Id : $('#team7id').val(),
            team8Id : $('#team8id').val()
        }

        $.ajax({
            type: 'POST',
            url: customUri + '/api/v1/vginfo',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(data){
            alert("저장햇슴");
            init_dropdown_vglist();
            $('#vdInfoDropdown').val(data);
            $('#vdInfoDropdown').trigger('change');
        }).fail(function(error)
        {
            alert (JSON.stringify(error));
        });
    }

    function vginfo_update()
    {
        let id = $('#id').val();
        let data = {
            vgNumber : $('#vg_number').val(),
            vgTitle : $('#vg_title').val(),
            vgStartDate : $('#vg_start_date').val(),
            team1Id : $('#team1id').val(),
            team2Id : $('#team2id').val(),
            team3Id : $('#team3id').val(),
            team4Id : $('#team4id').val(),
            team5Id : $('#team5id').val(),
            team6Id : $('#team6id').val(),
            team7Id : $('#team7id').val(),
            team8Id : $('#team8id').val()
        }

        $.ajax({
            type: 'PUT',
            url: customUri + '/api/v1/vginfo/' + id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(){
            alert("수정햇슴");
        }).fail(function(error)
        {
            alert (JSON.stringify(error));
        });
    }

    function init_dropdown_herolist()
    {
        $.ajax({
            url : 'https://feh.wiki/api/get_hero_list_outer.php',
            type : "GET",
            dataType : "jsonp",
            async : false,
            contentType: 'application/json; charset=utf-8',
            success:function(result){
                //console.log(result[2]['id']);
                $('.heroDropdown').html('');
                $('.heroDropdown').select2({
                    data:result,
                    placeholder : "select hero..",
                    width : 'element'});
            },error:function(result)
            {
                //console.log(result);
            }
        });
    }

    function update_herodropdown(dropdownid, value)
    {
        $('#'+dropdownid).val(value).trigger('change');
    }

    //TODO :: teamId heroList Dropdown 불러와서 드랍다운으로 나중에 교체할것

</script>