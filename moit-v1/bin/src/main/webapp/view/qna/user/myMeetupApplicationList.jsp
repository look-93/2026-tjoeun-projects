<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../../inc/userHeader.jsp" %>
<style>

:root{
    --c1:#B6FFFA;
    --c2:#98E4FF;
    --c3:#80B3FF;
    --c4:#687EFF;

    --bg:#f7faff;
    --white:#ffffff;
    --shadow:0 5px 20px rgba(0,0,0,.08);
}

/* 💡 헤더 들썩임/이동을 완전히 방지하기 위한 브라우저 기본값 유지 */
html {
    height: 100%;
}

body {
    min-height: 100%;
    margin: 0;
    padding: 0;
    overflow-y: auto; /* 브라우저 기본 스크롤 정책을 따라 헤더 위치 보존 */
}

/* 💡 메인 컨텐츠 영역 자체를 제어하여 외부 레이아웃(헤더/푸터)에 영향 차단 */
.main-content {
    width: 100%;
    display: block;
    overflow: hidden; /* 내부 요소가 튀어나가 전체 스크롤을 만드는 것 방지 */
    min-height: 1050px;       
}

/* 마이페이지 전체 감싸는 영역 */
.mypage-wrap {
    /* 💡 컨텐츠가 적을 때 억지로 화면을 늘려 스크롤을 유발하지 않도록 최적화 */
    min-height: auto; 
    height: 100%;
    max-width: 1200px; 
    margin: 0 auto;
    box-sizing: border-box;
    padding-top: 30px;
    padding-bottom: 50px; /* 푸터와의 안정적인 거리 확보 */
}
/* PROFILE */
.profile-card{
    margin-top:30px;
    background:white;
    border-radius:20px;
    padding:30px;
    box-shadow:var(--shadow);

    display:flex;
    align-items:center;
    gap:20px;
}

.myProfile-img{
    width:100px;
    height:100px;
    border-radius:50%;
    background:linear-gradient(135deg,var(--c2),var(--c4));
}

.myProfile-info h2{
    margin-bottom:10px;
}

.badge{
    display:inline-block;
    padding:8px 14px;
    border-radius:30px;
    background:#eef4ff;
    color:var(--c4);
}

/* CONTENT */
.content{
    margin-top:25px;
    display:grid;
    grid-template-columns:250px 1fr;
    gap:25px;
}

/* SIDEBAR */
.sidebar{
    background:white;
    border-radius:20px;
    padding:20px;
    box-shadow:var(--shadow);
    height:fit-content;
}

.sidebar a{
    display:block;
    padding:14px;
    margin-bottom:8px;
    text-decoration:none;
    color:#333;
    border-radius:10px;
}

.sidebar a.active{
    background:var(--c2);
}

/* MAIN */
.main{
    display:flex;
    flex-direction:column;
    gap:25px;
    min-width: 0;
}

/* STATS */
.stats{
    display:grid;
    grid-template-columns:repeat(4,1fr);
    gap:20px;
}

.stat-card{
    background:white;
    border-radius:20px;
    padding:25px;
    box-shadow:var(--shadow);
    text-align:center;
}

.stat-card h3{
    color:var(--c4);
    margin-top:10px;
    font-size:28px;
}

/* SECTION */
.section{
    background:white;
    padding:25px;
    border-radius:20px;
    box-shadow:var(--shadow);
}

.section h2{
    margin-bottom:20px;
}

/* TABLE */
table {
    width: 100%;
    border-collapse: collapse;
    table-layout: fixed; 
}

th {
    background: #f8fbff;
}

th, td {
    padding: 15px 8px; 
    border-bottom: 1px solid #eee;
    text-align: center;
    vertical-align: middle;
    font-size: 14px;
}

table td:first-child {
    text-align: left;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

/* STATUS */
.status {
    display: inline-block;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 13px;
    white-space: nowrap; 
}

.status-progress { background: #e8f7ff; color: #0077cc; }
.status-end { background: #ffe8e8; color: #d10000; }

/* BUTTON STYLES */
.action-btn-wrap {
    display: flex;
    justify-content: center;
    gap: 6px;
    flex-wrap: nowrap;
}

.action-btn-wrap button {
    border: none;
    padding: 8px 14px;
    border-radius: 8px;
    font-size: 13px;
    font-weight: 600;
    cursor: pointer;
    white-space: nowrap;
    transition: all 0.2s ease;
}

.applicant-btn { background: #e6f9f3; color: #0aa370; }
.applicant-btn:hover { background: #0aa370; color: white; }
.edit-btn { background: #eef2ff; color: #4f46e5; }
.edit-btn:hover { background: #4f46e5; color: white; }
.delete-btn { background: #fff5f5; color: #e53e3e; }
.delete-btn:hover { background: #e53e3e; color: white; }


/* TABLE RESPONSIVE WRAPPER */
.table-responsive {
    width: 100%;
    max-width: 100%;    
    overflow-x: auto;   
    display: block;     
    -webkit-overflow-scrolling: touch; 
    min-width: 0;       
}

.table-responsive table {
    min-width: 800px;  
    width: 100%;
}


/* MODAL STYLES */
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    opacity: 0;
    visibility: hidden;
    transition: all 0.3s ease;
}

.modal-overlay.active {
    opacity: 1;
    visibility: visible;
}

.modal-window {
    background: white;
    width: 750px;
    max-width: 90%;
    border-radius: 16px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.15);
    overflow: hidden;
    transform: translateY(-20px);
    transition: transform 0.3s ease;
}

.modal-overlay.active .modal-window {
    transform: translateY(0);
}

.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 24px;
    border-bottom: 1px solid #f1f5f9;
}

.modal-header h3 {
    font-size: 18px;
    color: #1e293b;
    font-weight: 600;
}

.modal-close {
    background: none;
    border: none;
    font-size: 24px;
    color: #94a3b8;
    cursor: pointer;
    transition: color 0.2s;
    line-height: 1;
}

.modal-close:hover {
    color: #64748b;
}

.modal-body {
    padding: 24px;
    max-height: 450px;
    overflow-y: auto;
}

.modal-table {
    width: 100%;
    border-collapse: collapse;
}

.modal-table th {
    background: #ffffff;
    color: #1e293b;
    font-weight: 600;
    border-bottom: 2px solid #f1f5f9;
    padding: 12px 8px;
}

.modal-table td {
    padding: 14px 8px;
    border-bottom: 1px solid #f1f5f9;
    color: #334155;
    font-size: 14px;
    vertical-align: middle;
}

.download-btn {
    background: #64748b;
    color: white;
    border: none;
    padding: 6px 12px;
    border-radius: 6px;
    font-size: 13px;
    cursor: pointer;
    transition: background 0.2s;
}

.download-btn:hover { background: #475569; }
.btn-approve {
    background: #10b981;
    color: white;
    border: none;
    padding: 6px 12px;
    border-radius: 6px;
    font-size: 13px;
    font-weight: 600;
    cursor: pointer;
    margin-right: 4px;
}
.btn-approve:hover { background: #059669; }

.btn-reject {
    background: #ef4444;
    color: white;
    border: none;
    padding: 6px 12px;
    border-radius: 6px;
    font-size: 13px;
    font-weight: 600;
    cursor: pointer;
}
.btn-reject:hover { background: #dc2626; }

.state-text-wait { color: #64748b; font-weight: 500; }
.state-text-pass { color: #10b981; font-weight: 600; }


/* ================= 💡 RESPONSIVE (하나로 통합 정리) ================= */
@media(max-width: 1200px) {
    th, td {
        font-size: 13px;
        padding: 12px 6px;
    }
    .action-btn-wrap button {
        padding: 6px 10px;
    }
}

@media(max-width: 768px){
    .content { 
        grid-template-columns: 1fr; 
        width: 100%;
        overflow: hidden; 
    }
    .stats { grid-template-columns: 1fr 1fr; }
    table { font-size:12px; }
    .action-btn-wrap { flex-wrap: wrap; }
}

/* ================= PAGINATION STYLES (예쁘게 변경) ================= */
.pagination {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 6px;           /* 페이지 번호 사이의 간격 */
    margin-top: 35px;   /* 테이블과의 거리 확보 */
    margin-bottom: 10px;
    list-style: none;
    padding: 0;
}

.page-item {
    display: inline-block;
}

.page-link {
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 36px;
    height: 36px;
    padding: 0 6px;
    font-size: 14px;
    font-weight: 500;
    color: #64748b;     /* 기본 글자색 (차분한 그레이) */
    text-decoration: none;
    background-color: var(--white);
    border: 1px solid #e2e8f0;
    border-radius: 8px; /* 딱딱한 각진 모양 대신 부드러운 라운드 */
    transition: all 0.2s ease-in-out;
}

/* 마우스 호버(올렸을 때) 효과 */
.page-link:hover {
    color: var(--c4);
    background-color: #f0f5ff;
    border-color: var(--c3);
}

/* 현재 활성화된 페이지 번호 스타일 (Active) */
.page-item.active .page-link {
    color: var(--white) !important;
    background-color: var(--c4) !important; /* 메인 포인트 컬러 */
    border-color: var(--c4) !important;
    font-weight: 700;
    box-shadow: 0 4px 10px rgba(104, 126, 255, 0.25); /* 은은한 네온 효과 */
}

/* 이전(<) / 다음(>) 버튼 전용 스타일 */
.page-item:first-child .page-link,
.page-item:last-child .page-link {
    background-color: #f8fafc;
    font-weight: bold;
    color: #94a3b8;
}

.page-item:first-child .page-link:hover,
.page-item:last-child .page-link:hover {
    color: var(--c4);
    background-color: #f1f5f9;
}

/* BUTTON */
th:nth-child(1),
td:nth-child(1){
    text-align:left;
    padding-left:20px;
}

.review-btn{
    border:none;
    background:var(--c4);
    color:white;
    padding:8px 15px;
    border-radius:8px;
    cursor:pointer;
}

.review-complete{
    border:none;
    background:#ddd;
    color:#666;
    padding:8px 15px;
    border-radius:8px;
}
</style>

<div class="main-content">
	<div class="container mypage-wrap">

		<%@ include file="../../inc/myPageHeader.jsp" %>

		<div class="content">			
			<%@ include file="../../inc/myPageSidebar.jsp" %>

			<div class="main">

			<%@ include file="../../inc/myPageSubHeader.jsp" %>

				<div class="section">
					<h2>내 신청글</h2>
					<div class="table-responsive">
						<table>
							<colgroup>
							    <col style="width:8%">
							    <col style="width:30%">
							    <col style="width:20%">
							    <col style="width:15%">
							    <col style="width:15%">
							</colgroup>
							<thead>
								<tr>
									<th>번호</th>
									<th>모집명</th>
									<th>모집일</th>
									<th>모집 상태</th>
									<th>후기</th>
						        </tr>
						    </thead>
							<tbody>
							    <c:forEach var="apply" items="${applyList}" varStatus="status">
							        <tr>
							        	<td>${paging.listtotal - paging.pstartno - status.index}</td>
				                        <td>${apply.title }</td>
				                        <td>${apply.fomatMeetupAt}</td>
				
				                        <td><span class="status status-end">종료</span></td>
				
				                        <td><button class="review-btn">후기 작성</button></td>
							        </tr>
							    </c:forEach>
							    
							    <c:if test="${empty applyList}">
							        <tr>
							            <td colspan="5" style="padding: 40px 0; color: #94a3b8;">신청한 모집글이 존재하지 않습니다.</td>
							        </tr>
							    </c:if>
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
	</div>
</div>

<script>

console.log("${applyList}");
</script>
<%@ include file="../../inc/userFooter.jsp" %>