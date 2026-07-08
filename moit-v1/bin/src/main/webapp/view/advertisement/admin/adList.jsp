<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>MOIT 관리자 - 광고관리</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
	rel="stylesheet">

<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

<style>

body {
	background: #f4f7fc;
}

h3 {
	color: #4a7dff;
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

.badge-open {
	background: #dfffe4;
	color: #009933;
}

.badge-pending {
	background: #fff4cc;
	color: #aa8800;
}

.badge-closed {
	background: #ffe3e3;
	color: #d11a2a;
}

.status-select {
    border-radius: 8px;
    padding: 4px 8px;
    font-size: 13px;

    appearance: none;
    -webkit-appearance: none;
    -moz-appearance: none;
}

.status-open {
    border: 1px solid #28a745;
    color: #28a745;
}

.status-pending {
    border: 1px solid #ffc107;
    color: #aa8800;
}

.status-closed {
    border: 1px solid #dc3545;
    color: #dc3545;
}

</style>
<meta name="_csrf" content="${_csrf.token}">
<meta name="_csrf_header" content="${_csrf.headerName}">
</head>

<body>

<div class="container-fluid">

	<div class="row">

		<%@ include file="/view/inc/sidebar.jsp" %>

		<!-- 메인 -->
		<div class="col-md-10 p-4">

			<!-- 상단 -->
			<div class="topbar d-flex justify-content-between align-items-center">
				<h3>광고관리</h3>
				<c:choose>
				    <c:when test="${not empty sessionScope.loginUserName}">
				        <div>${sessionScope.loginUserName}님</div>
				    </c:when>
				    <c:otherwise>
				        <div>관리자님</div>
				    </c:otherwise>
				</c:choose>
			</div>

			<!-- 통계 -->
			<div class="row mb-4">

				<div class="col-md-3">
					<div class="card-box">
						<h6>전체 광고</h6>
						<div class="stat-number">${totalAdCnt}</div>
					</div>
				</div>

				<div class="col-md-3">
					<div class="card-box">
						<h6>진행중</h6>
						<div class="stat-number">${openCnt}</div>
					</div>
				</div>

				<div class="col-md-3">
					<div class="card-box">
						<h6>대기</h6>
						<div class="stat-number">${pendingCnt}</div>
					</div>
				</div>

				<div class="col-md-3">
					<div class="card-box">
						<h6>종료</h6>
						<div class="stat-number">${closedCnt}</div>
					</div>
				</div>

			</div>

			<!-- 검색 -->
			<form method="get" action="${pageContext.request.contextPath}/advertisement/admin/adList.do">

			<div class="table-box mb-4">
			
				<div class="row">
			
					<div class="col-md-5">
						<input type="text" name="searchText"
						       value="${dto.searchText}"
						       class="form-control"
						       placeholder="광고명 검색">
					</div>
			
					<div class="col-md-3">
						<select name="status" class="form-select">
							<option value="" ${dto.status == null ? 'selected' : ''}>전체</option>
							<option value="OPEN" ${dto.status == 'OPEN' ? 'selected' : ''}>OPEN</option>
							<option value="PENDING" ${dto.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
							<option value="CLOSED" ${dto.status == 'CLOSED' ? 'selected' : ''}>CLOSED</option>
						</select>
					</div>
			
					<div class="col-md-2">
						<button class="btn btn-primary">검색</button>
					</div>
			
				</div>
			
			</div>
			
			</form>

			<!-- 버튼 -->
			<a href="${pageContext.request.contextPath}/advertisement/admin/adWrite.do" 
				class="btn btn-primary my-3">
			    광고등록
			</a>
			

			<!-- 목록 -->
			<div class="table-box">

				<table class="table table-hover">

					<thead class="table-light">
						<tr>
							<th>번호</th>
							<th>이미지</th>
							<th>광고명</th>
							<th>유형</th>
							<th>상태</th>
							<th>기간</th>
							<th>노출수</th>
							<th>클릭수</th>
							<th>관리</th>
						</tr>
					</thead>

					<tbody>
						<c:set var="page" value="${empty dto.page ? 1 : dto.page}" />
						<c:forEach var="ad" items="${list}"  varStatus="status">
						
						<tr>
							<td> ${totalCnt - (((dto.page - 1) * dto.size) + status.index)} </td>
						
							<td>
							    <c:choose>
							        <c:when test="${not empty ad.imageUrl}">
							            <img src="${pageContext.request.contextPath}${ad.imageUrl}"
							            	 onerror="this.src='${pageContext.request.contextPath}/upload/no-image.png'"
							                 style="width:70px;
							                        height:45px;
							                        object-fit:cover;
							                        border-radius:8px;">
							        </c:when>
							        <c:otherwise>
							            <div style="
							                width:70px;
							                height:45px;
							                background:#eee;
							                border-radius:8px;
							                display:flex;
							                align-items:center;
							                justify-content:center;
							                font-size:12px;">
							                NO IMG
							            </div>
							        </c:otherwise>
							    </c:choose>
							</td>
						
							<td>${ad.title}</td>
							<td>${ad.adType}</td>
						
							<td>
								<select class="status-select 
								    ${ad.status == 'OPEN' ? 'status-open' :
								      ad.status == 'PENDING' ? 'status-pending' :
								      'status-closed'}"
								    onchange="changeStatus(this, ${ad.adId})">
								    
								    <option value="OPEN" ${ad.status == 'OPEN' ? 'selected' : ''}>OPEN</option>
								    <option value="PENDING" ${ad.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
								    <option value="CLOSED" ${ad.status == 'CLOSED' ? 'selected' : ''}>CLOSED</option>
								</select>
								
								<script>
									function changeStatus(select, adId) {
	
									    const status = select.value;
	
									    const token =
									        document.querySelector("meta[name='_csrf']").content;
	
									    const header =
									        document.querySelector("meta[name='_csrf_header']").content;
	
									    fetch("${pageContext.request.contextPath}/advertisement/admin/updateStatusAjax.do?testMode=true", {
									        method: "POST",
									        headers: {
									            "Content-Type": "application/json",
									            [header]: token
									        },
									        body: JSON.stringify({
									            adId: adId,
									            status: status
									        })
									    })
									    .then(res => res.json())
									    .then(data => {
	
									        if (data.result === "OK") {
	
									            alert("상태 변경 완료");
	
									            select.classList.remove(
									                "status-open",
									                "status-pending",
									                "status-closed"
									            );
	
									            if (status === "OPEN")
									                select.classList.add("status-open");
									            else if (status === "PENDING")
									                select.classList.add("status-pending");
									            else
									                select.classList.add("status-closed");
									            
									            location.reload();
	
									        } else {
									            alert("상태 변경 실패");
									        }
									    })
									    .catch(err => {
									        console.error(err);
									        alert("서버 오류");
									    });
									}
								</script>
							</td>
						
							<td>
							    ${fn:substring(ad.startDatetime, 0, 13)}
							    ~
							    ${fn:substring(ad.endDatetime, 0, 13)}
							</td>
							<td>${ad.impressions}</td>
							<td>${ad.clicks}</td>
						
							<td>
								<a href="${pageContext.request.contextPath}/advertisement/admin/adDetail.do?adId=${ad.adId}" 
									class="btn btn-sm btn-outline-primary">
								    상세
								</a>
								<form action="${pageContext.request.contextPath}/advertisement/admin/adDelete.do"
								      method="post"
								      style="display:inline;"
								      onsubmit="return confirm('삭제하시겠습니까?');">
								
								    <input type="hidden"
								           name="${_csrf.parameterName}"
								           value="${_csrf.token}">
								
								    <input type="hidden"
								           name="adId"
								           value="${ad.adId}">
								
								    <button type="submit"
								            class="btn btn-sm btn-outline-danger">
								        삭제
								    </button>
								
								</form>
							</td>
						</tr>
						
						</c:forEach>
						
					</tbody>

				</table>


				<nav class="mt-4">
					<ul class="pagination justify-content-center">
				
						<!-- 이전 -->
						<li class="page-item ${dto.page <= 1 ? 'disabled' : ''}">
							<a class="page-link"
							   href="?page=${dto.page - 1}&searchText=${dto.searchText}&status=${dto.status}">
								이전
							</a>
						</li>
				
						<!-- 번호 -->
						<c:if test="${totalPage > 0}">
							<c:forEach var="i" begin="1" end="${totalPage}">
								<li class="page-item ${dto.page == i ? 'active' : ''}">
									<a class="page-link"
									   href="?page=${i}&searchText=${dto.searchText}&status=${dto.status}">
										${i}
									</a>
								</li>
							</c:forEach>
						</c:if>
				
						<!-- 다음 -->
						<c:if test="${totalPage > 0}">
							<li class="page-item ${dto.page >= totalPage ? 'disabled' : ''}">
								<a class="page-link"
								   href="?page=${dto.page + 1}&searchText=${dto.searchText}&status=${dto.status}">
									다음
								</a>
							</li>
						</c:if>
					</ul>
				</nav>

			</div>

		</div>

	</div>

</div>

</body>
</html>