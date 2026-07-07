<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- 사이드바 -->
	<div class="col-md-2 sidebar p-0">
		<div class="logo">MOIT</div>
		<div class="menu">
			<a href="/moit/admin/member/list.do">회원관리</a> 
			<a href="/moit/admin/admin/list.do">관리자관리</a> 
			<a href="${pageContext.request.contextPath}/meetup/admin/list.do" class="<c:if test='${menu eq \"meetup\"}'>active</c:if>">모집관리</a> 
			<a href="${pageContext.request.contextPath}/advertisement/admin/adList.do">광고관리</a>
			<a href="${pageContext.request.contextPath}/review/admin/list.do" class="<c:if test='${menu eq \"review\"}'>active</c:if>">후기관리</a>
			<a href="${pageContext.request.contextPath}/report/admin/adminList.do" class="<c:if test='${menu eq \"report\"}'>active</c:if>">신고관리</a>
		</div>
	</div>
</body>
</html>