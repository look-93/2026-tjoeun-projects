<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>MOIT | 후기 목록 테스트</title>
<style>
body {
	font-family: '맑은 고딕', sans-serif;
	background: #f7faff;
	margin: 40px;
}

.container {
	width: 1000px;
	margin: 0 auto;
	background: white;
	padding: 30px;
	border-radius: 20px;
	box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
}

h2 {
	color: #687EFF;
	margin-bottom: 20px;
}

.btn-write {
	display: inline-block;
	background: #687EFF;
	color: white;
	padding: 10px 20px;
	text-decoration: none;
	border-radius: 8px;
	margin-bottom: 20px;
	font-size: 14px;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 10px;
}

th, td {
	padding: 12px;
	border-bottom: 1px solid #ddd;
	text-align: left;
}

th {
	background-color: #f2f6ff;
	color: #687EFF;
}

.btn-action {
	border: none;
	padding: 6px 12px;
	border-radius: 5px;
	cursor: pointer;
	font-size: 12px;
	margin-right: 5px;
}

.btn-hide {
	background: #ffeaa7;
	color: #d63031;
}

.btn-delete {
	background: #ff7675;
	color: white;
}

.status-badge {
	display: inline-block;
	padding: 3px 8px;
	border-radius: 4px;
	font-size: 12px;
	font-weight: bold;
}

.status-y {
	background: #e3faf2;
	color: #20c997;
}

.status-n {
	background: #ffe3e3;
	color: #ff6b6b;
}
</style>
</head>
<body>

	<div class="container">
		<h2>🎯 모임 후기 목록 (내 기능 검증 화면)</h2>

		<a href="${pageContext.request.contextPath}/review/insert"
			class="btn-write">✍️ 새 후기 작성하러 가기</a>

		<table>
			<thead>
				<tr>
					<th width="8%">리뷰ID</th>
					<th width="10%">모임ID</th>
					<th width="10%">작성자ID</th>
					<th>후기 내용</th>
					<th width="10%">공개여부</th>
					<th width="20%">기능 제어</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when disguise="empty reviewList" test="${not empty reviewList}">
						<c:forEach var="review" items="${reviewList}">
							<tr>
								<td>${review.id}</td>
								<td>${review.meetupId}</td>
								<td>${review.memberId}</td>
								<td>c:out value="${review.content}" /></td>
								<td><c:choose>
										<c:when test="${review.isPublic == 1}">
											<span class="status-badge status-y">공개 (Y)</span>
										</c:when>
										<c:otherwise>
											<span class="status-badge status-n">비공개 (N)</span>
										</c:otherwise>
									</c:choose></td>
								<td>
									<form action="${pageContext.request.contextPath}/review/hide"
										method="POST" style="display: inline;">
										<input type="hidden" name="${_csrf.parameterName}"
											value="${_csrf.token}" /> <input type="hidden" name="id"
											value="${review.id}"> <input type="hidden"
											name="meetupId" value="${review.meetupId}">
										<button type="submit" class="btn-action btn-hide">비공개</button>
									</form>

									<form action="${pageContext.request.contextPath}/review/delete"
										method="POST" style="display: inline;">
										<input type="hidden" name="${_csrf.parameterName}"
											value="${_csrf.token}" /> <input type="hidden" name="id"
											value="${review.id}"> <input type="hidden"
											name="meetupId" value="${review.meetupId}">
										<button type="submit" class="btn-action btn-delete">삭제</button>
									</form>
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="6"
								style="text-align: center; color: #999; padding: 40px;">등록된
								후기가 없거나 조건에 맞는 글이 없습니다.</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
	</div>

</body>
</html>