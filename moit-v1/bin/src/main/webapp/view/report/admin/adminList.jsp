<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MOIT 관리자 - 신고관리</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/report.css">

<style>
body {
	background: #f4f7fc;
}

.sidebar {
	min-height: 100vh;
	background: #fff;
	border-right: 1px solid #dee2e6;
}

.logo {
	font-size: 24px;
	font-weight: bold;
	color: #4a7dff;
	padding: 25px;
	text-align: center;
	border-bottom: 1px solid #eee;
}

.menu a {
	display: block;
	padding: 15px 20px;
	color: #333;
	text-decoration: none;
	font-weight: 500;
}

.menu a:hover {
	background: #edf3ff;
}

.menu .active {
	background: #4a7dff;
	color: white;
}

.topbar {
	background: white;
	border-radius: 15px;
	padding: 20px;
	margin-bottom: 20px;
}

.card-box {
	background: white;
	border-radius: 15px;
	padding: 20px;
	box-shadow: 0 2px 5px rgba(0, 0, 0, .05);
}

.tab-btn {
	background: white;
	border: none;
	padding: 10px 20px;
	border-radius: 10px;
	margin-right: 10px;
}

.tab-btn.active {
	background: #4a7dff;
	color: white;
}

.table-box {
	background: white;
	border-radius: 15px;
	padding: 20px;
}

.stat-number {
	font-size: 28px;
	font-weight: bold;
	color: #4a7dff;
}
</style>

</head>
<body>

<div class="container-fluid">

	<div class="row">

		<!-- 사이드바 -->
		<%@include file="../../inc/sidebar.jsp"%>

		<!-- 메인 -->
		<div class="col-md-10 p-4">

			<!-- 상단 -->
			<div class="topbar d-flex justify-content-between align-items-center">
				<h3>신고관리</h3>
				<div>관리자님</div>
			</div>

			<!-- 신고목록 -->
			<div class="admin-list-page">

				<section class="admin-list-card">

					<div class="admin-list-card-head">

						<div class="admin-list-card-title">
							관리자 신고목록
						</div>

						<div class="admin-list-filters">

							<a href="${pageContext.request.contextPath}/report/admin/adminList.do"
								class="admin-list-filter-btn ${empty param.targetType && empty param.status && empty param.deleteYn ? 'active' : ''}">
								전체
							</a>

							<a href="${pageContext.request.contextPath}/report/admin/adminList.do?targetType=MEETUP"
								class="admin-list-filter-btn ${param.targetType == 'MEETUP' ? 'active' : ''}">
								MEETUP
							</a>

							<a href="${pageContext.request.contextPath}/report/admin/adminList.do?targetType=REVIEW"
								class="admin-list-filter-btn ${param.targetType == 'REVIEW' ? 'active' : ''}">
								REVIEW
							</a>

							<a href="${pageContext.request.contextPath}/report/admin/adminList.do?status=PENDING"
								class="admin-list-filter-btn ${param.status == 'PENDING' ? 'active' : ''}">
								PENDING
							</a>

							<a href="${pageContext.request.contextPath}/report/admin/adminList.do?deleteYn=Y"
								class="admin-list-filter-btn ${param.deleteYn == 'Y' ? 'active' : ''}">
								DELETE
							</a>

						</div>

					</div>

					<div class="admin-list-table-wrap">

						<table class="admin-list-table">

							<thead>
								<tr>
									<th>신고번호</th>
									<th>대상</th>
									<th>대상ID</th>
									<th>신고자</th>
									<th>사유</th>
									<th>상태</th>
									<th>신고일</th>
									<th>상세</th>
								</tr>
							</thead>

							<tbody>
								<c:forEach var="dto" items="${list}">
									<tr>
										<td>${dto.reportId}</td>
										<td>${dto.targetType}</td>
										<td>${dto.targetId}</td>
										<td>${dto.memberId}</td>
										<td>${dto.reasonCode}</td>
										<td>
											<c:choose>
												<c:when test="${dto.status == 'PENDING'}">
													<span class="admin-list-status pending">${dto.status}</span>
												</c:when>
												<c:otherwise>
													<span class="admin-list-status approved">${dto.status}</span>
												</c:otherwise>
											</c:choose>
										</td>
										<td>${dto.createdAt}</td>
										<td>
											<a class="admin-list-link"
												href="${pageContext.request.contextPath}/report/admin/adminDetail.do?reportId=${dto.reportId}">
												보기
											</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>

							<tfoot>
								<tr>
									<td colspan="8">

										<ul class="admin-list-pagination">

											<c:if test="${paging.start > paging.bottomlist}">
												<li>
													<a class="admin-list-page-btn"
														href="?pstartno=${paging.start-1}&targetType=${param.targetType}&status=${param.status}&deleteYn=${param.deleteYn}">
														이전
													</a>
												</li>
											</c:if>

											<c:forEach var="i"
												begin="${paging.start}"
												end="${paging.end}">

												<li>
													<a href="?pstartno=${i}&targetType=${param.targetType}&status=${param.status}&deleteYn=${param.deleteYn}"
														class="admin-list-page-btn ${i == paging.current ? 'active' : ''}">
														${i}
													</a>
												</li>

											</c:forEach>

											<c:if test="${paging.end < paging.pagetotal}">
												<li>
													<a class="admin-list-page-btn"
														href="?pstartno=${paging.end+1}&targetType=${param.targetType}&status=${param.status}&deleteYn=${param.deleteYn}">
														다음
													</a>
												</li>
											</c:if>

										</ul>

									</td>
								</tr>
							</tfoot>

						</table>

					</div>

				</section>

			</div>

		</div>

	</div>

</div>

</body>
</html>