<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<div class="dropdown">
    <button class="btn btn-success dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-expanded="false" style = "float : right;">
        메뉴
    </button>
    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
        <a class="dropdown-item" href="${customUri}/vg">메인 페이지</a>
        <a class="dropdown-item" href="${customUri}/vg/first">5시 초동 데이터</a>
        <a class="dropdown-item" href="${customUri}/vg/past">전체 데이터 조회</a>
        <div class="dropdown-divider"></div>
        <a class="dropdown-item" href="https://feh.wiki">파엠히 위키</a>
    </div>
</div>