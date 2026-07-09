<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>광고 테스트</title>
</head>
<body>

<h1>🔥 내가 수정한 test.jsp 맞음</h1>

<c:if test="${not empty ad}">

    <h2>${ad.title}</h2>

    <a href="${pageContext.request.contextPath}/advertisement/admin/click.do?adId=${ad.adId}">
        <img src="${pageContext.request.contextPath}${ad.imageUrl}"
        	 onerror="this.src='${pageContext.request.contextPath}/upload/no-image.png'"
             style="width:500px;">
    </a>

</c:if>

<c:if test="${empty ad}">
    <h2>노출 가능한 광고 없음</h2>
</c:if>


</body>
</html>