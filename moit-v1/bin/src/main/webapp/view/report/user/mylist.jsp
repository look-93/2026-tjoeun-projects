<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../../inc/userHeader.jsp" />

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/report.css">

<div class="mylist-page">

	<section class="mylist-card">
		<div class="mylist-card-head">
			<div class="mylist-card-title">
				내 신고내역
			</div>
		</div>

		<div class="mylist-table-wrap">
			<table class="mylist-table">
				<thead>
					<tr>
						<th>번호</th>
						<th>대상</th>
						<th>대상ID</th>
						<th>신고사유</th>
						<th>상태</th>
						<th>신고일</th>
						<th>관리</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="dto" items="${list}" varStatus="status">
						<tr>
							<td>${status.count}</td>
							<td>${dto.targetType}</td>
							<td>${dto.targetId}</td>
							<td>${dto.reasonCode}</td>
							<td>
								<c:choose>
									<c:when test="${dto.status == 'PENDING'}">
										<span class="mylist-status pending">${dto.status}</span>
									</c:when>
									<c:otherwise>
										<span class="mylist-status approved">${dto.status}</span>
									</c:otherwise>
								</c:choose>
							</td>
							<td>${dto.createdAt}</td>
							<td>
								<a class="mylist-link" href="${pageContext.request.contextPath}/report/user/detail.do?reportId=${dto.reportId}">
									상세
								</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>

				<tfoot>
					<tr>
						<td colspan="7">
							<ul class="mylist-pagination">
								<!-- 이전 -->
								<c:if test="${paging.start > paging.bottomlist}">
									<li>
										<a href="?pstartno=${paging.start - 1}" class="mylist-page-btn">이전</a>
									</li>
								</c:if>

								<!-- 페이지 번호 -->
								<c:forEach var="i" begin="${paging.start}" end="${paging.end}">
									<li>
										<a href="?pstartno=${i}" class="mylist-page-btn <c:if test='${i == paging.current}'>active</c:if>">
											${i}
										</a>
									</li>
								</c:forEach>

								<!-- 다음 -->
								<c:if test="${paging.end < paging.pagetotal}">
									<li>
										<a href="?pstartno=${paging.end + 1}" class="mylist-page-btn">다음</a>
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

<jsp:include page="../../inc/userFooter.jsp" />