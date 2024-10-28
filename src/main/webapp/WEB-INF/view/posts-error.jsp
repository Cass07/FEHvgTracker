<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% request.setCharacterEncoding("UTF-8"); %>

<jsp:include page="layout/header.jsp">
    <jsp:param value="${header_title}" name="header_title"/>
</jsp:include>

<div class = "col-md-12">
    <h3>오류!</h3>
    <p class="lead">${errorMessage}</p>
    <a href = "${customUri}/vg/" role = button class = "btn btn-success">돌아가기</a>
</div>

<jsp:include page="layout/footer.jsp"/>