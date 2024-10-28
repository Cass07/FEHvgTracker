<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <script type="text/javascript" async="" src="https://www.google-analytics.com/analytics.js"></script>
    <script type="text/javascript" async="" src="https://www.googletagmanager.com/gtag/js?id=G-SJYFPWL98F&amp;l=dataLayer&amp;cx=c"></script>
    <script async="" src="https://www.googletagmanager.com/gtag/js?id=UA-143004793-2"></script>

    <c:if test="${!empty header_title}">
    <title>${header_title}</title>
    </c:if>
    <c:if test="${empty header_title}">
        <title>타이틀 없음.</title>
    </c:if>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <link rel="shortcut icon" href="https://feh.wiki/images/favicon.png" type="image/x-icon">
    <link rel="icon" href="https://feh.wiki/images/favicon.png" type="image/x-icon">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <link rel = "stylesheet" href = "${customUri}/js/app/assets/select2.min.css">
</head>
<body>