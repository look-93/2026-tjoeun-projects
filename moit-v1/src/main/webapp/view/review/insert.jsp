<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
:root { 
	--c1: #B6FFFA; 
	--c2: #98E4FF; 
	--c3: #80B3FF; 
	--c4: #687EFF; 
	--bg: #f7faff; 
	--shadow: 0 5px 20px rgba(0, 0, 0, .05);
}

* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
	font-family: 'Pretendard', '맑은 고딕', sans-serif;
}

body {
	background: var(--bg); 
}

.container {
	width: 1200px;
	max-width: 95%;
	margin: 40px auto;
}

.card {
	background: white;
	border-radius: 20px;
	padding: 40px;
	box-shadow: var(--shadow); 
	width: 100%;
}

.page-title {
	font-size: 28px;
	font-weight: 700;
	margin-bottom: 30px;
	color: var(--c4);
}

.meeting-info {
	display: flex;
	gap: 25px;
	margin-bottom: 35px;
	background: #f8fafc;
	padding: 20px;
	border-radius: 15px;
}

.meeting-img {
	width: 220px;
	height: 140px;
	border-radius: 12px;
	background: linear-gradient(135deg, var(--c2), var(--c4)); 
}

.meeting-detail {
	flex: 1;
	display: flex;
	flex-direction: column;
	justify-content: center;
	gap: 8px;
}

.meeting-detail h2 {
	font-size: 22px;
	font-weight: 700;
	color: #1e293b;
	margin-bottom: 4px;
}

.meeting-detail p {
	color: #64748b;
	font-size: 14px;
	display: flex;
	align-items: center;
	gap: 6px;
}

.section-title {
	font-size: 18px;
	font-weight: 700;
	margin-bottom: 15px;
	color: #1e293b;
}

.rating-box {
	margin-bottom: 35px;
}

.stars {
	font-size: 36px;
	cursor: pointer;
	display: flex;
	gap: 6px;
	user-select: none;
}

/* 💡 [수정] 기본 상태는 불이 모두 꺼진 회색 별로 통일합니다 */
.star {
	color: #e2e8f0;
	transition: color 0.15s ease-in-out;
}

/* active 클래스가 동적으로 붙어야 노란색 불이 들어옵니다 */
.star.active {
	color: #FFD43B;
}

.upload-box {
	width: 100%;
	height: 140px;
	border: 2px dashed var(--c3); 
	border-radius: 15px;
	display: flex;
	justify-content: center;
	align-items: center;
	margin-bottom: 15px;
	background: #f8fcff;
	color: #64748b;
	font-size: 15px;
}

.review-text {
	width: 100%;
	height: 200px;
	border: 1px solid #e2e8f0;
	border-radius: 15px;
	padding: 20px;
	resize: none;
	font-size: 15px;
	outline: none;
	color: #1e293b;
	line-height: 1.6;
}

.review-text:focus {
	border-color: var(--c4);
}

.btn-wrap {
	display: flex;
	justify-content: flex-end;
	gap: 12px;
	margin-top: 30px;
}

.btn {
	border: none;
	padding: 14px 28px;
	border-radius: 10px;
	cursor: pointer;
	font-size: 15px;
	font-weight: 600;
	transition: background 0.2s;
}

.btn-cancel {
	background: #f1f5f9;
	color: #64748b;
}

.btn-cancel:hover {
	background: #e2e8f0;
}

.btn-save {
	background: var(--c4);
	color: white;
}

.btn-save:hover {
	background: #4f46e5;
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

<%@include file="../inc/userHeader.jsp" %>
<body>

	<div class="container">

		<form action="${pageContext.request.contextPath}/review/insert" method="POST" class="card" onsubmit="return validateForm();">
    
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
			
			<input type="hidden" name="meetupId" value="1"> 
			<input type="hidden" name="memberId" value="10">
			<input type="hidden" name="isPublic" value="true">
			
			<input type="hidden" name="rating" id="reviewRating" value="0">

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
				<div class="stars">
					<span class="star" data-value="1">★</span>
					<span class="star" data-value="2">★</span>
					<span class="star" data-value="3">★</span>
					<span class="star" data-value="4">★</span>
					<span class="star" data-value="5">★</span>
				</div>
			</div>

			<div>
				<h3 class="section-title">사진 첨부</h3>
				
				<label for="reviewFileInput" class="upload-box" style="cursor: pointer; display: flex;">
					사진 업로드 영역 (클릭하여 추가)
				</label>
				
				<input type="file" id="reviewFileInput" name="reviewFiles" multiple style="display: none;">
			</div>

			<br>

			<div>
				<h3 class="section-title">후기 내용</h3>
				<textarea name="content" class="review-text" placeholder="모임 경험을 자유롭게 작성해주세요." required></textarea>
			</div>

			<div class="btn-wrap">
				<button type="button" class="btn btn-cancel" onclick="history.back();">취소</button>
				<button type="submit" class="btn btn-save">후기 등록</button>
			</div>

		</form>

	</div>

<script>
// 1. 별점 클릭 인터랙션
document.querySelectorAll('.star').forEach(star => {
    star.addEventListener('click', function() {
        const score = this.getAttribute('data-value');
        
        document.getElementById('reviewRating').value = score;
        
        document.querySelectorAll('.star').forEach(s => {
            if (parseInt(s.getAttribute('data-value')) <= parseInt(score)) {
                s.classList.add('active');
            } else {
                s.classList.remove('active');
            }
        });
        
        console.log("선택된 별점: " + score + "점");
    });
});

// 2. 💡 [추가] 사용자가 별점을 0점인 상태로 등록 버튼을 눌렀을 때 막아주는 체크 함수
function validateForm() {
    const rating = document.getElementById('reviewRating').value;
    if (rating === "0" || rating === "") {
        alert("모임 만족도 별점을 최소 1점 이상 선택해 주세요!");
        return false; // 서브밋 차단
    }
    return true; // 서브밋 허용
}
</script>

<%@include file="../inc/userFooter.jsp" %>