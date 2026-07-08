<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko"> 
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Moit 신고 버튼</title>
<!-- Latest compiled and minified CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- Latest compiled JavaScript -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
<link href="./css/board.css" rel="stylesheet">
</head>
<body>
	<div class="container my-5">
		<h3>신고 버튼 테스트</h3>

		<div class="text-end">
			<a	href="${pageContext.request.contextPath}/report/user/write.do?targetType=MEETUP&amp;targetId=1"
				class="btn btn-outline-primary"> 모임 신고 </a>			<!-- ${targetType} 		${targetId}-->
			<a	href="${pageContext.request.contextPath}/report/user/write.do?targetType=REVIEW&amp;targetId=3"
				class="btn btn-outline-danger"> 리뷰 신고 </a>				<!-- ${targetType} 		${targetId}-->
			<a	href="${pageContext.request.contextPath}/report/admin/adminList.do"
				class="btn btn-outline-success"> 출입금지 </a>				<!-- ${targetType} 		${targetId}-->
		</div>
	</div>
</body>
</html>