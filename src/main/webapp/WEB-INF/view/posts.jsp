<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% request.setCharacterEncoding("UTF-8"); %>

<jsp:include page="layout/header.jsp">
    <jsp:param value="${header_title}" name="header_title"/>
</jsp:include>

<h1>게시글 열람</h1>

<div class = "col-md-12">

    <table class = "table table-horixontal table-bordered">

        <thead>
        <tr>
            <th>${posts.title}</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>작성자 : ${posts.author}</td>
        </tr>
        <tr>
            <td>사진 : <img src = "${posts.picture}"></td>
        </tr>
        <tr>
            <td>작성 시간 : ${posts.createdDate}</td>
        </tr>
        <tr>
            <td>수정 시간 : ${posts.modifiedDate}</td>
        </tr>
        <tr>
            <td>${posts.content}</td>
        </tr>
        </tbody>
    </table>

    <div style = "display : none" id = "id">${posts.id}</div>

    <a href = "${customUri}/admin/board/" role = "button" class = "btn btn-secondary">돌아가기</a>
    <a href = "${customUri}/admin/board/posts/update/${posts.id}" role = "button" class = "btn btn-primary">글 수정</a>
    <button type = "button" id = "btn-delete" class = "btn btn-danger">글 삭제</button>


</div>


<jsp:include page="layout/footer.jsp"/>
