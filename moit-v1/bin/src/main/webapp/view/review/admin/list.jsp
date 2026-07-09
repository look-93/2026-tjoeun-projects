<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MOIT 관리자 - 후기관리</title>

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

</head>
<body>

	<div class="container-fluid">

		<div class="row">

			<%@include file="../../inc/sidebar.jsp"%>

			<!-- 메인 -->
			<div class="col-md-10 p-4">

				<!-- 상단 -->
				<div
					class="topbar d-flex justify-content-between align-items-center">
					<h3>행사관리</h3>

					<div>관리자님</div>
				</div>

				<!-- 통계 -->
				<div class="row mb-4">

					<div class="col-md-3">
						<div class="card-box">
							<h6>전체 행사</h6>
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
							<h6>종료행사</h6>
							<div class="stat-number">7</div>
						</div>
					</div>

				</div>

				<!-- 탭 -->
				<div class="mb-4">
					<button class="tab-btn active">행사목록</button>
					<button class="tab-btn">행사등록</button>
					<button class="tab-btn">신청자목록</button>
				</div>

				<!-- 검색 -->
				<div class="table-box mb-4">

					<div class="row">

						<div class="col-md-4">
							<input type="text" class="form-control" placeholder="행사명 검색">
						</div>

						<div class="col-md-3">
							<select class="form-select">
								<option>전체</option>
								<option>모집중</option>
								<option>진행예정</option>
								<option>종료</option>
							</select>
						</div>

						<div class="col-md-2">
							<button class="btn btn-primary">검색</button>
						</div>

					</div>

				</div>

				<!-- 버튼 -->
				<div class="mb-3">
					<button class="btn btn-primary">행사등록</button>
					<button class="btn btn-warning">수정</button>
					<button class="btn btn-danger">삭제</button>
				</div>

				<!-- 행사목록 -->
				<div class="table-box">

					<table class="table table-hover">

						<thead class="table-light">
							<tr>
								<th>번호</th>
								<th>행사명</th>
								<th>행사일</th>
								<th>신청기간</th>
								<th>모집인원</th>
								<th>신청현황</th>
								<th>관리</th>
							</tr>
						</thead>

						<tbody>

							<tr>
								<td>1</td>
								<td>환경정화 캠페인</td>
								<td>2026-07-01</td>
								<td>06-01 ~ 06-25</td>
								<td>50명</td>
								<td>32명 신청</td>
								<td>
									<button class="btn btn-sm btn-outline-primary"
										data-bs-toggle="modal" data-bs-target="#applicantModal">
										신청자목록</button>
								</td>
							</tr>

							<tr>
								<td>2</td>
								<td>플로깅 행사</td>
								<td>2026-07-15</td>
								<td>06-10 ~ 07-10</td>
								<td>30명</td>
								<td>15명 신청</td>
								<td>
									<button class="btn btn-sm btn-outline-primary">신청자목록</button>
								</td>
							</tr>

						</tbody>

					</table>

					<nav>
						<ul class="pagination justify-content-center">
							<li class="page-item"><a class="page-link" href="#">1</a></li>
							<li class="page-item"><a class="page-link" href="#">2</a></li>
							<li class="page-item"><a class="page-link" href="#">3</a></li>
						</ul>
					</nav>

				</div>

			</div>

		</div>

	</div>

	<!-- 신청자목록 모달 -->
	<div class="modal fade" id="applicantModal">

		<div class="modal-dialog modal-xl">

			<div class="modal-content">

				<div class="modal-header">
					<h5>행사 신청자 목록</h5>
					<button class="btn-close" data-bs-dismiss="modal"></button>
				</div>

				<div class="modal-body">

					<table class="table">

						<thead>
							<tr>
								<th>회원명</th>
								<th>신청일</th>
								<th>첨부파일</th>
								<th>상태</th>
								<th>처리</th>
							</tr>
						</thead>

						<tbody>

							<tr>
								<td>홍길동</td>
								<td>2026-06-12</td>
								<td>
									<button class="btn btn-sm btn-secondary">다운로드</button>
								</td>
								<td>대기</td>
								<td>
									<button class="btn btn-success btn-sm">승인</button>
									<button class="btn btn-danger btn-sm">거절</button>
								</td>
							</tr>

							<tr>
								<td>김철수</td>
								<td>2026-06-11</td>
								<td>
									<button class="btn btn-sm btn-secondary">다운로드</button>
								</td>
								<td>승인</td>
								<td>-</td>
							</tr>

						</tbody>

					</table>

				</div>

			</div>

		</div>

	</div>

</body>
</html>