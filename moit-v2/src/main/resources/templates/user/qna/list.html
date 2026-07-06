<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MOIT 관리자 - 모집관리</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

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


<div class="container-fluid">

	<div class="row">

		<!-- 사이드바 -->
		<%@include file="../../inc/sidebar.jsp"%>

		<!-- 메인 -->
		<div class="col-md-10 p-4">

			<!-- 상단 -->
			<div
				class="topbar d-flex justify-content-between align-items-center">
				<h3>모집관리</h3>

				<div>관리자님</div>
			</div>

			<!-- 통계 -->
			<div class="row mb-4">

				<div class="col-md-3">
					<div class="card-box">
						<h6>전체 모집글</h6>
						<div class="stat-number">25</div>
					</div>
				</div>

				<div class="col-md-3">
					<div class="card-box">
						<h6>모집중</h6>
						<div class="stat-number">8</div>
					</div>
				</div>

				<div class="col-md-3">
					<div class="card-box">
						<h6>진행예정</h6>
						<div class="stat-number">10</div>
					</div>
				</div>

				<div class="col-md-3">
					<div class="card-box">
						<h6>모집종료</h6>
						<div class="stat-number">7</div>
					</div>
				</div>

			</div>

			<!-- 탭 -->
			<div class="mb-4">
				<button class="tab-btn active">모집목록</button>
				<!--                 <button class="tab-btn">행사등록</button>
               <button class="tab-btn">신청자목록</button> -->
			</div>

			
			<!-- 검색 -->
			<form action="${pageContext.request.contextPath}/meetup/admin/list.do" method="get" >
				<div class="table-box mb-4">

					<div class="row">

						<div class="col-md-4">
							<input type="text" name="searchText" class="form-control" placeholder="모집명 검색">
						</div>

						<div class="col-md-2">
							<select id="status" name="status" class="form-select">
								<option value="">상태</option>
								<option value="RECRUITING">모집중</option>
								<option value="CLOSED">모집마감</option>
								<option value="CANCELED">취소</option>
							</select>
						</div>
						<div class="col-md-2">
							<select id="searchType" name="searchType" class="form-select">
								<option value="name">작성자이름</option>
								<option value="title">모집명</option>
							</select>
						</div>
<!-- 						<div class="col-md-2">
							<select class="form-select">
								<option>이름</option>
								<option>모집명</option>
								<option>모집자명</option>
							</select>
						</div> -->

						<div class="col-md-2">
							<button type="submit" class="btn btn-primary">검색</button>
						</div>

					</div>

				</div>
			</form>

			<!-- 행사목록 -->
			<div class="table-box">

				<table class="table table-hover">
				    <colgroup>
				        <col style="width:5%">
				        <col style="width:10%">
				        <col style="width:30%">
				        <col style="width:15%">
				        <col style="width:10%">
				        <col style="width:10%">
				        <col style="width:10%">
				        <col style="width:10%">
				    </colgroup>
					<thead class="table-light">
						<tr>
							<th>번호</th>
							<th>모집자</th>
							<th>모집명</th>
							<th>모집일</th>
							<th>최소모집인원</th>
							<th>최대모집인원</th>
							<th>신청현황</th>
							<th>관리</th>
						</tr>
					</thead>
					
					<tbody>

						<c:forEach var="serchList" items="${serchList}" varStatus="status">
							<tr>
								<td>${paging.listtotal - paging.pstartno - status.index}</td>
								<td>${serchList.nickname}</td> <!-- 모집자명으로 수정! -->
								<td>${serchList.title}</td>
								<td>${serchList.meetupAt}</td>
								<td>${serchList.minParticipants }</td>
								<td>${serchList.maxParticipants  }</td>
								<td>${serchList.totalParticipants }</td>									
								<td>
									<form action="${pageContext.request.contextPath}/meetup/admin/delete.do?meetupId=${serchList.meetupId}" method="post">
										<input  type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" /> <!-- 보안 -->
										<button class="btn btn-danger btn-sm">삭제</button>
									</form>
								</td>
							</tr>


						</c:forEach>

					</tbody>

				</table>
				
				<ul class="pagination justify-content-center">
					<!-- 이전 -->
					<c:if test="${paging.start > paging.bottomlist}">
						<li class="page-item"><a class="page-link" href="?pstartno= ${paging.start-1}"> < </a>
						</li>
					</c:if>

					<!-- 1,2,3,4,5,6 -->
					<c:forEach var="i" begin="${paging.start}" end="${paging.end}">
						<li class="page-item <c:if test="${i==paging.current}"> active </c:if>">
							<a href="?pstartno=${i}" class="page-link">${i}</a>
						</li>
					</c:forEach>

					<!-- 다음 -->
					<c:if test="${paging.pagetotal > paging.end}">
						<li class="page-item"><a class="page-link" href="?pstartno= ${paging.end+1}"> > </a>
						</li>
					</c:if>
				</ul>					

			</div>

		</div>

	</div>

</div>


<script>
window.onload = function() {
    const status = document.getElementById("status");
    status.value = '${param.status}';
}

</script>
</html>

