<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="../../inc/userHeader.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/report.css">

<div class="update-page">

	<section class="update-card">
		<div class="update-card-head">
			<div class="update-card-title">
				신고 수정
			</div>
		</div>

		<div class="update-inner">
			<form action="${pageContext.request.contextPath}/report/user/update.do" method="post">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

				<input type="hidden" name="reportId" value="${dto.reportId}">
				<input type="hidden" name="targetType" value="${dto.targetType}">
				<input type="hidden" name="targetId" value="${dto.targetId}">
				<input type="hidden" name="memberId" value="${dto.memberId}">

				<!--
				<div class="update-target-row">
					<span class="update-chip">report_id: ${dto.reportId}</span>
					<span class="update-chip">target_type: ${dto.targetType}</span>
					<span class="update-chip">target_id: ${dto.targetId}</span>
				</div>
				-->

				<label class="update-form-label">
					신고 사유 <span class="update-required">(필수)</span>
				</label>

				<div class="update-reason-grid">
					<label class="update-reason-item">
						<input type="radio" name="reasonCode" value="ABUSE" ${dto.reasonCode == 'ABUSE' ? 'checked' : ''}>
						욕설/비방 (ABUSE)
					</label>

					<label class="update-reason-item">
						<input type="radio" name="reasonCode" value="SPAM" ${dto.reasonCode == 'SPAM' ? 'checked' : ''}>
						도배/스팸 (SPAM)
					</label>

					<label class="update-reason-item">
						<input type="radio" name="reasonCode" value="FAKE_INFO" ${dto.reasonCode == 'FAKE_INFO' ? 'checked' : ''}>
						허위 정보 (FAKE_INFO)
					</label>

					<label class="update-reason-item">
						<input type="radio" name="reasonCode" value="AD" ${dto.reasonCode == 'AD' ? 'checked' : ''}>
						광고성 게시물 (AD)
					</label>

					<label class="update-reason-item">
						<input type="radio" name="reasonCode" value="ETC" ${dto.reasonCode == 'ETC' ? 'checked' : ''}>
						기타 (ETC)
					</label>
				</div>

				<label class="update-form-label">
					상세 내용 <span class="update-optional">(선택)</span>
				</label>

				<textarea class="update-textarea"
						  id="reasonDetail"
						  name="reasonDetail"
						  maxlength="200"
						  placeholder="신고 내용을 자세히 입력해주세요.">${dto.reasonDetail}</textarea>

				<div class="update-counter">
					<span id="detailCount">0</span> / 200
				</div>

				<div class="update-actions">
					<button type="button" class="update-btn update-btn-white" onclick="history.back()">취소</button>
					<button type="submit" class="update-btn update-btn-blue">수정 완료</button>
				</div>
			</form>
		</div>
	</section>

</div>

<script>
	const reasonDetail = document.getElementById('reasonDetail');
	const detailCount = document.getElementById('detailCount');

	function updateDetailCount() {
		detailCount.textContent = reasonDetail.value.length;
	}

	updateDetailCount();

	reasonDetail.addEventListener('input', updateDetailCount);
</script>

<%@ include file="../../inc/userFooter.jsp" %>