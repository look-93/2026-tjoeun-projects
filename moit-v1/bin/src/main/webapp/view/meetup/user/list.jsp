<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../../inc/userHeader.jsp"%>
<style>

:root{
    --c1:#B6FFFA;
    --c2:#98E4FF;
    --c3:#80B3FF;
    --c4:#687EFF;

    --bg:#f7faff;
    --white:#fff;
    --text:#222;
    --gray:#777;

    --radius:20px;
    --shadow:0 5px 15px rgba(0,0,0,.07);
}


/* PAGE */

.page{
    margin-top:30px;
    display:grid;
    grid-template-columns:260px 1fr;
    gap:25px;
}

/* SIDEBAR */

.sidebar{
    background:white;
    border-radius:20px;
    padding:25px;
    box-shadow:var(--shadow);
    height:fit-content;
}

.sidebar h3{
    margin-bottom:20px;
}

.category{
    display:flex;
    flex-direction:column;
    gap:10px;
}

.category a{
    padding:12px;
    border-radius:12px;
    text-decoration:none;
    color:#333;
}

.category a:hover,
.category .active{
    background:var(--c2);
}

/* CONTENT */

.content{
    display:flex;
    flex-direction:column;
    gap:20px;
    min-height:770px;
}

.filter-box{
    background:white;
    border-radius:20px;
    padding:20px;
    box-shadow:var(--shadow);

    display:flex;
    gap:10px;
    flex-wrap:wrap;    
}

.filter-box input,
.filter-box select{
    padding:12px;
    border-radius:12px;
    border:1px solid #ddd;
}

.filter-box button{
    background:var(--c4);
    color:white;
    border:none;
    padding:12px 20px;
    border-radius:12px;
    
	cursor:pointer; 
}

/* CARD */

.card-grid{
    display:grid;
    grid-template-columns:
    repeat(auto-fill,minmax(280px,1fr));
    gap:20px;
}

.card{
    background:white;
    border-radius:20px;
    overflow:hidden;
    box-shadow:var(--shadow);
    transition:.3s;
}

.card:hover{
    transform:translateY(-5px);
}

.card-img{
    height:180px;
    background:linear-gradient(
    135deg,
    var(--c2),
    var(--c3)
    );
}

.card-body{
    padding:20px;
}

.badge{
    display:inline-block;
    background:#dfffe4;
    color:#009933;
    padding:5px 10px;
    border-radius:20px;
    font-size:12px;
    margin-bottom:10px;
}

.badge-close{
    background:#ffe5e5;
    color:red;
}

.card h4{
    margin-bottom:10px;
}

.card p{
    color:var(--gray);
    margin-bottom:5px;
    font-size:14px;
}

.card-footer{
    margin-top:15px;
    display:flex;
    justify-content:space-between;
}

.like{
    color:var(--c4);
}

/* PAGINATION */

.pagination {
    display: flex;
    justify-content: center;
    gap: 10px;
    margin-top: 40px;    /* 💡 카드 리스트와의 간격도 살짝 넓힘 (기존 20px) */
    margin-bottom: 40px; /* 💡 푸터와 부딪히지 않도록 아래쪽 여백 추가! */
}

.pagination a{
    width:40px;
    height:40px;
    background:white;
    display:flex;
    justify-content:center;
    align-items:center;
    border-radius:10px;
    text-decoration:none;
    color:#333;
}

.pagination .active{
    background:var(--c4);
    color:white;
}

/* MOBILE */

@media(max-width:768px){

.page{
    grid-template-columns:1fr;
}

nav{
    display:none;
}

.search-top{
    display:none;
}

}

/* SIDEBAR AD */

.sidebar-ad{
    margin-top:25px;
    height:320px;
    border-radius:20px;
    padding:25px;
	overflow:hidden; /* 중요 */
    padding:0;       /* 중요 */
    display:flex;
    flex-direction:column;
    justify-content:center;
    align-items:center;

    text-align:center;
    color:white;

    box-shadow:var(--shadow);
}

.sidebar-ad a{
    background:white;
    color:var(--c4);

    text-decoration:none;

    padding:12px 20px;
    border-radius:12px;

    font-weight:bold;
}

/* TOP AD */

.top-ad{
    border-radius:20px;
    box-shadow:var(--shadow);
    overflow:hidden; /* 중요 */
    padding:0;       /* 중요 */
    display:flex;
    align-items:center;
    
    /* 💡 배너의 높이를 고정하고 싶다면 아래와 같이 
          원하는 높이를 명시적으로 지정해주는 것이 좋습니다. */
    height: 120px; 
}
.top-ad a {
    display: block;
    width: 100%;
    height: 100%;
}
.top-ad img {
    width: 100%;
    height: 100%;
    object-fit: cover; /* 💡 이미지가 일그러지지 않고 비율을 유지하며 꽉 차게 채웁니다 */
}
a {
	text-decoration: none;
	color:inherit;
}

.write-btn{
    margin-left:auto;

    background:linear-gradient(
        135deg,
        var(--c4),
        var(--c3)
    );

    color:white;
    text-decoration:none;

    padding:12px 22px;
    border-radius:12px;

    font-weight:600;
    transition:.3s;

    display:flex;
    align-items:center;
}

.write-btn:hover{
    transform:translateY(-2px);
    box-shadow:0 8px 20px rgba(104,126,255,.25);
}

.btn-primary1{background:var(--c4);color:white;width:100%;}

</style>
<div class="main-content">
	<div class="container">

		<div class="page">

			<!-- LEFT -->

			<aside class="sidebar">

				<h3>카테고리</h3>

				<div class="category">
					<a href="list.do"
						class="${empty param.categoryId || param.categoryId eq '0' ? 'active' : ''}">전체</a>
					<c:forEach var="category" items="${categoryList}">
						<a href="?categoryId=${category.categoryId}"
							class="${param.categoryId eq category.categoryId ? 'active' : ''}">
							${category.categoryName} </a>
					</c:forEach>
				</div>

				<div class="sidebar-ad">

					<a href="${pageContext.request.contextPath}/advertisement/admin/click.do?adId=${ad.adId}">
				         <img src="${pageContext.request.contextPath}${ad.imageUrl}"
				        	 onerror="this.src='${pageContext.request.contextPath}/upload/no-image.png'">
				    </a>

				</div>

			</aside>

			<!-- RIGHT -->

			<section class="content">



				<div class="top-ad">

					<a href="${pageContext.request.contextPath}/advertisement/admin/click.do?adId=${ad.adId}">
				      <img src="${pageContext.request.contextPath}${ad.imageUrl}"
				        	 onerror="this.src='${pageContext.request.contextPath}/upload/no-image.png'">
	        	      <img src="${pageContext.request.contextPath}${ad.imageUrl}">
				    </a>

				</div>
				<form action="${pageContext.request.contextPath}/meetup/user/list.do" method="get">
					<div class="filter-box">
						<!-- 검색 -->
						<input type="hidden" name="categoryId"
							value="${empty param.categoryId ? 0 : param.categoryId}">
						<input type="text" name="searchText" class="form-control" placeholder="모임명 검색">
						<!-- 시도 -->
						<select id="sidoId" name="sidoId" class="form-select">
							<option value="0" selected>전체 지역</option>
							<c:forEach var="sido" items="${sidoList}" varStatus="status">
								<option value="${sido.sidoId}">${sido.name}</option>
							</c:forEach>
						</select> <select id="orderType" name="orderType" class="form-select">
							<option value="createAt" selected>최신순</option>
							<option value="like">인기순</option>
							<option value="meetupAt">마감임박순</option>
						</select>

						<div class="col-md-2">
							<button type="submit" class="btn btn-primary1">검색</button>
						</div>

						<!-- 여기 추가 -->
						<a href="${pageContext.request.contextPath}/meetup/user/write.do"
							class="write-btn"> ✏️ 모임 만들기 </a>

					</div>
				</form>


				<div class="card-grid">


					<!-- CARD -->
					<c:forEach var="meetup" items="${serchList}" varStatus="status">
							<div class="card">
								<!-- 수정 이미지 추가 -->
								<!-- <div class="card-img"></div> -->
								<div class="card-body">
									<a href="${pageContext.request.contextPath}/meetup/user/detail.do?meetupId=${meetup.meetupId}">
									<span class="badge"> 
										<c:choose>
											<c:when test="${meetup.status eq 'RECRUITING'}">
			                        			모집중
			                        		</c:when>
												<c:when test="${meetup.status eq 'CLOSED'}">
			                        			모집마감
			                        		</c:when>
										</c:choose>
									</span>

									<h4>${meetup.title}</h4>

									<p>${meetup.categoryName}</p>									
									<p>${meetup.sigunguName}</p>
									<p>👥 ${meetup.participant} /
										${meetup.maxParticipants}명</p>
									</a>
									<div class="card-footer">
										<span>${meetup.fomatMeetupAt}</span> 
										<span class="like">
										    <a href="#"
										       onclick="toggleLike(event, ${meetup.meetupId}, this)">
										        ${meetup.hasLike ? '♥' : '♡'}
										    </a>
										
										    <span class="like-count">${meetup.likeCnt}</span>
										</span>
										<!-- 수정 좋아요 -->
									</div>
								</div>
							</div>
					</c:forEach>
				</div>

				<div class="pagination">

					<!-- 이전 -->
					<c:if test="${paging.start > paging.bottomlist}">
						<a class="page-link" href="?pstartno=${paging.start-1}"> < </a>

					</c:if>

					<!-- 1,2,3,4,5,6 -->
					<c:forEach var="i" begin="${paging.start}" end="${paging.end}">
						<a href="?pstartno=${i}"
							class="${i == paging.current ? 'active' : ''}"> ${i} </a>
					</c:forEach>

					<!-- 다음 -->
					<c:if test="${paging.pagetotal > paging.end}">
						<a href="?pstartno=${paging.end+1}">›</a>
					</c:if>

				</div>
			</section>

		</div>

	</div>
</div>
<script>
	window.onload = function() {
	    const sidoId = document.getElementById("sidoId");
	    const orderType = document.getElementById("orderType");
	    if ('${param.sidoId}' !== '') {
	        sidoId.value = '${param.sidoId}';
	    }

	    if ('${param.orderType}' !== '') {
	        orderType.value = '${param.orderType}';
	    }
	}
	const csrfHeader = "${_csrf.headerName}";
	const csrfToken = "${_csrf.token}";

	function toggleLike(e, meetupId, el) {
	    e.preventDefault();

	    fetch(
	        "${pageContext.request.contextPath}/meetup/user/meetupLike.do?meetupId=" + meetupId,
	        {
	            method: "POST",
	            headers: {
	                [csrfHeader]: csrfToken
	            }
	        }
	    )
	    .then(res => res.json())
	    .then(data => {
			// console.log(data)
	        // 서버에서 true/false 반환한다고 가정
	        if (data.hasLike) {
	            el.innerText = "♥";
	        } else {
	            el.innerText = "♡";
	        }

	        el.parentElement.querySelector(".like-count").innerText =
	            data.likeCnt;
	    })
	    .catch(err => console.error(err));
	}
</script>
<%@ include file="../../inc/userFooter.jsp"%>