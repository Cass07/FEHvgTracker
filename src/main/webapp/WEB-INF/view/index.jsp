<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% request.setCharacterEncoding("UTF-8"); %>

<jsp:include page="layout/header.jsp">
    <jsp:param value="${header_title}" name="header_title"/>
</jsp:include>

<style>
    .pagination{
        text-align : center;
        display : block;
    }
    .pagination>li{
        display : inline-block;
        list-style : none;
        height : 20px;
        font-size : 20px;
        vertical-align : middle;
        color : #1E1E1E;
    }
    .pagination>li.active>a{
        color : #338A76;
        font-weight : 600;
    }
    .pagination>li>a{
        color : #1E1E1E;
        width : 100%;
        height : 100%;
        padding : 0 2px 0 2px;
        display : block;
    }
</style>
<h1>로그 게시판</h1>
<div class = "col-md-12">
    <div class = "row">
        <div class = "col-md-6">
            <a href = "${customUri}/admin/board/posts/save" role = "button" class = "btn btn-primary">글 등록</a>
            <a href = "${customUri}/admin/manualcron" role = "button" class = "btn btn-primary">수동 데이터 수집</a>
            <a href = "${customUri}/admin/vginfo" role = "button" class = "btn btn-primary">투표대전 데이터 수정</a>
            <c:if test="${!empty userName}">
                Logged is as : <span id = "user">${userName.name}</span>
                <span><img src = "${userName.picture}" style = "width : 40px"></span>
                <a href = "https://feh.wiki/voting/logout" class = "btn btn-info active" role = button>로그아웃</a>
            </c:if>
            <c:if test="${empty userName}">
                <a href = "https://feh.wiki/voting/oauth2/authorization/google" class = "btn btn-success active" role = button>구글 로그인</a>

            </c:if>
        </div>
    </div>
    <br>
    <table class = "table table-horixontal table-bordered">
        <thead class = "thead-strong">
        <tr>
            <th>게시글번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>최종수정일</th>
        </tr>
        </thead>
        <tbody id = "tbody">
        <c:forEach var="post" items="${posts}">
            <tr>
                <td>${post.id}</td>
                <td><a href = "${customUri}/admin/board/posts/${post.id}">${post.title}</a></td>
                <td>${post.author}</td>
                <td>${post.modifiedDateString}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <ul class = "pagination">
        <c:if test="${!empty pageFirst}">
            <li id = "page-first">
                <a href = "/voting/admin/board/page/${pageFirst}"><i class="bi bi-chevron-double-left"></i></a>
            </li>
        </c:if>
        <c:if test="${!empty pagePrev}">
            <li id = "page-prev">
                <a href = "/voting/admin/board/page/${pagePrev}"><i class="bi bi-chevron-left"></i></a>
            </li>
        </c:if>
        <c:if test="${!empty pageCurrentPrevList}">
            <c:forEach var="page" items="${pageCurrentPrevList}">
                <li class = "page-number">
                    <a href = "/voting/admin/board/page/${page}">${page}</a>
                </li>
            </c:forEach>
        </c:if>
        <c:if test="${!empty pageCurrent}">
            <li class = "page-number active">
                <a href = "/voting/admin/board/page/${pageCurrent}">${pageCurrent}</a>
            </li>
        </c:if>
        <c:if test="${!empty pageCurrentNextList}">
            <c:forEach var="page" items="${pageCurrentNextList}">
                <li class = "page-number">
                    <a href = "/voting/admin/board/page/${page}">${page}</a>
                </li>
            </c:forEach>
        </c:if>
        <c:if test="${!empty pageNext}">
            <li id = "page-prev">
                <a href = "/voting/admin/board/page/${pageNext}"><i class="bi bi-chevron-right"></i></a>
            </li>
        </c:if>
        <c:if test="${!empty pageLast}">
            <li id = "page-prev">
                <a href = "/voting/admin/board/page/${pageLast}"><i class="bi bi-chevron-double-right"></i></a>
            </li>
        </c:if>
    </ul>

</div>

<jsp:include page="layout/footer.jsp"/>