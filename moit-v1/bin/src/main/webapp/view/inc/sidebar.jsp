<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<!-- 사이드바 -->
	<div class="col-md-2 sidebar p-0">

		<div class="logo">MOIT</div>

		<div class="menu">
			<a href="/moit/admin/member/list.do">회원관리</a> <a
				href="/moit/admin/admin/list.do">관리자관리</a> <a
				href="${pageContext.request.contextPath}/meetup/admin/list.do"
				class="<c:if test='${menu eq \"meetup\"}'>active</c:if>"> >모임관리</a>
			<a href="/moit/admin/region/list.do">지역관리</a> <a
				href="/moit/admin/category/list.do">카테고리관리</a> <a
				href="/moit/admin/notice/list.do">공지사항</a> <a
				href="/moit/admin/event/list.do">행사관리</a> <a
				href="/moit/admin/advertisement/list.do">광고관리</a>
		</div>

	</div>
</body>
</html>