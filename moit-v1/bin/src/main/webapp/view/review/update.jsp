<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>MOIT | 후기 작성</title>

<style>
:root { -
	-c1: #B6FFFA; -
	-c2: #98E4FF; -
	-c3: #80B3FF; -
	-c4: #687EFF; -
	-bg: #f7faff; -
	-shadow: 0 5px 20px rgba(0, 0, 0, .08);
}

* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
	font-family: '맑은 고딕', sans-serif;
}

body {
	background: var(- -bg);
}

.container {
	width: 900px;
	max-width: 95%;
	margin: 40px auto;
}

.card {
	background: white;
	border-radius: 20px;
	padding: 35px;
	box-shadow: var(- -shadow);
}

.page-title {
	font-size: 32px;
	margin-bottom: 30px;
	color: #687EFF;
}

.meeting-info {
	display: flex;
	gap: 20px;
	margin-bottom: 30px;
}

.meeting-img {
	width: 220px;
	height: 140px;
	border-radius: 15px;
	background: linear-gradient(135deg, var(- -c2), var(- -c4));
}

.meeting-detail {
	flex: 1;
}

.meeting-detail h2 {
	margin-bottom: 10px;
}

.meeting-detail p {
	color: #666;
	margin-bottom: 8px;
}

.section-title {
	font-size: 22px;
	margin-bottom: 15px;
}

.rating-box {
	margin-bottom: 30px;
}

.stars {
	font-size: 40px;
	color: #FFD43B;
	cursor: pointer;
}

.upload-box {
	height: 180px;
	border: 2px dashed var(- -c3);
	border-radius: 15px;
	display: flex;
	justify-content: center;
	align-items: center;
	margin-bottom: 30px;
	background: #f8fcff;
}

.review-text {
	width: 100%;
	height: 220px;
	border: 1px solid #ddd;
	border-radius: 15px;
	padding: 20px;
	resize: none;
	font-size: 15px;
}

.btn-wrap {
	display: flex;
	justify-content: flex-end;
	gap: 10px;
	margin-top: 30px;
}

.btn {
	border: none;
	padding: 14px 25px;
	border-radius: 10px;
	cursor: pointer;
	font-size: 15px;
}

.btn-cancel {
	background: #e9e9e9;
}

.btn-save {
	background: #687EFF;
	color: white;
}

@media ( max-width :768px) {
	.meeting-info {
		flex-direction: column;
	}
	.meeting-img {
		width: 100%;
	}
}
</style>
</head>

<body>

	<div class="container">

		<form action="${pageContext.request.contextPath}/review/insert"
			method="POST" class="card">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" /> <input type="hidden" name="meetupId"
				value="1"> <input type="hidden" name="memberId" value="10">
			<input type="hidden" name="isPublic" value="true">

			<h1 class="page-title">후기 작성</h1>

			<div class="meeting-info">
				<div class="meeting-img"></div>
				<div class="meeting-detail">
					<h2>등산 좋아하는 사람 모여라!</h2>
					<p>📍 서울</p>
					<p>📅 2026.06.01 ~ 2026.06.15</p>
					<p>👥 참가 완료</p>
				</div>
			</div>

			<div class="rating-box">
				<h3 class="section-title">별점 평가</h3>
				<div class="stars">★★★★★</div>
			</div>

			<div>
				<h3 class="section-title">사진 첨부</h3>
				<div class="upload-box">사진 업로드 영역</div>
				<input type="file" multiple>
			</div>

			<br>

			<div>
				<h3 class="section-title">후기 내용</h3>
				<textarea name="content" class="review-text"
					placeholder="모임 경험을 자유롭게 작성해주세요." required></textarea>
			</div>

			<div class="btn-wrap">
				<button type="button" class="btn btn-cancel"
					onclick="history.back();">취소</button>
				<button type="submit" class="btn btn-save">후기 등록</button>
			</div>

		</form>

	</div>

</body>
</html>