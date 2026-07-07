<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="../../inc/userHeader.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/report.css">

<main class="write-page">

	<section class="write-layout">
		<form class="write-card write-form-card"
			  action="${pageContext.request.contextPath}/report/user/write.do"
			  method="post">

			<div class="write-card-title">
				<h2 class="write-heading">신고하기</h2>
			</div>

			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<input type="hidden" name="targetType" value="${dto.targetType}" />
			<input type="hidden" name="targetId" value="${dto.targetId}" />

			<div class="write-field">
				<label class="write-label">
					신고 사유
					<span class="write-required">(필수)</span>
				</label>

				<div class="write-reasons">
					<label class="write-reason">
						<input class="write-radio" type="radio" name="reasonCode" value="ABUSE" required />
						욕설/비방 (ABUSE)
					</label>

					<label class="write-reason">
						<input class="write-radio" type="radio" name="reasonCode" value="SPAM" />
						도배/스팸 (SPAM)
					</label>

					<label class="write-reason">
						<input class="write-radio" type="radio" name="reasonCode" value="FAKE_INFO" />
						허위 정보 (FAKE_INFO)
					</label>

					<label class="write-reason">
						<input class="write-radio" type="radio" name="reasonCode" value="AD" />
						광고성 게시물 (AD)
					</label>

					<label class="write-reason">
						<input class="write-radio" type="radio" name="reasonCode" value="ETC" />
						기타 (ETC)
					</label>
				</div>
			</div>

			<div class="write-field">
				<label class="write-label" for="reasonDetail">
					상세 내용
					<span class="write-optional">(선택)</span>
				</label>

				<textarea class="write-textarea"
						  id="reasonDetail"
						  name="reasonDetail"
						  maxlength="200"
						  placeholder="신고 내용을 자세히 입력해주세요."></textarea>

				<div class="write-counter">
					<span id="detailCount">0</span> / 200
				</div>
			</div>

			<div class="write-actions">
				<button type="button" class="write-btn" onclick="history.back()">취소</button>
				<button type="submit" class="write-btn write-btn-primary">신고 등록</button>
			</div>
		</form>
	</section>
</main>

<script>
	const reasonDetail = document.getElementById('reasonDetail');
	const detailCount = document.getElementById('detailCount');

	reasonDetail.addEventListener('input', function() {
		detailCount.textContent = reasonDetail.value.length;
	});
</script>

<%@ include file="../../inc/userFooter.jsp" %>