<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% request.setCharacterEncoding("UTF-8"); %>

<jsp:include page="layout/header.jsp">
    <jsp:param value="${header_title}" name="header_title"/>
</jsp:include>

<h1>게시글 등록</h1>

<div class = "col-md-12">
    <div class = "col-md-4">
        <form>
            <div class = "form-group">
                <label for = "title">제목</label>
                <input type = "text" class = "form-control" id = "title" placeholder = "제목을 입력하세요">
            </div>
            <div class = "form-group">
                <label for = "author">작성자</label>
                <input type = "text" class = "form-control" id = "author" value = "${user.email}">
            </div>
            <div class = "form-group">
                <label for = "content">내용</label>
                <textarea class = "form-control" id = "content" placeholder = "내용을 입력하세요"></textarea>
            </div>
        </form>

        <a href = "${customUri}/admin/board/" role = "button" class = "btn btn-secondary">취소</a>
        <button type = "button" class = "btn btn-primary" id = "btn-save">등록</button>
    </div>
</div>

<jsp:include page="layout/footer.jsp"/>