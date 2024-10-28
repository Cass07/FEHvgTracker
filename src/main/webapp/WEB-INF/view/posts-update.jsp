<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% request.setCharacterEncoding("UTF-8"); %>

<jsp:include page="layout/header.jsp">
    <jsp:param value="${header_title}" name="header_title"/>
</jsp:include>

<h1>게시글 수정</h1>

<div class = "col-md-12">
    <div class = "col-md-4">
        <form>
            <div class ="form-group">
                <label for = "id">글 번호</label>
                <input type = "number" class = "form-control" id = "id" value = ${posts.id} readonly>
            </div>
            <div class = "form-group">
                <label for = "title">제목</label>
                <input type = "text" class = "form-control" id = "title" value = "${posts.title}">
            </div>
            <div class = "form-group">
                <label for = "author">작성자</label>
                <input type = "text" class = "form-control" id = "author" value = "${posts.author}" readonly>
            </div>
            <div class = "form-group">
                <label for = "content">내용</label>
                <textarea class = "form-control" id = "content" >${posts.content}</textarea>
            </div>
        </form>

        <a href = "${customUri}/admin/board/posts/${posts.id}" role = "button" class = "btn btn-secondary">취소</a>
        <button type = "button" class = "btn btn-primary" id = "btn-update">수정 완료</button>
    </div>
</div>

<jsp:include page="layout/footer.jsp"/>