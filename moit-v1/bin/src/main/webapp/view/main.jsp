<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="./inc/userHeader.jsp" %>

<div class="container">

    <section class="hero">

        <div>

            <h1>
                당신의 관심사가<br>
                새로운 모임이 되는 곳
            </h1>

            <p>
                대학생 · 일반인을 위한 모임 플랫폼
            </p>

            <div class="hero-btn">
                <button class="btn btn-primary">모임 찾기</button>
            </div>

        </div>

        <div class="hero-image">
            👥
        </div>

    </section>

    <section class="category">

        <div class="category-card">🏃<span>운동</span></div>
        <div class="category-card">🎨<span>문화</span></div>
        <div class="category-card">📚<span>스터디</span></div>
        <div class="category-card">🤝<span>봉사</span></div>
        <div class="category-card">🎮<span>게임</span></div>
        <div class="category-card">✈️<span>여행</span></div>
        <div class="category-card">☕<span>카페</span></div>
        <div class="category-card">🎵<span>음악</span></div>

    </section>

    <section class="ad-banner">

        <!-- <div class="ad-left"> -->
            <a href="${pageContext.request.contextPath}/advertisement/admin/click.do?adId=${ad.adId}">
		        <img src="${pageContext.request.contextPath}${ad.imageUrl}"
		        	 onerror="this.src='${pageContext.request.contextPath}/upload/no-image.png'">
		    </a>
        <!-- </div>
 -->
        <!-- <a href="#" class="ad-btn">자세히 보기</a> -->

    </section>

    <section class="section">

        <h2 class="section-title">🔥 인기 모임</h2>

        <div class="card-wrap">

            <div class="card">
                <!-- <div class="card-img"></div> -->
                <div class="card-body">
                    <h4>러닝 크루 모집</h4>
                    <div class="card-info">정원 8/10 · 서울</div>
                </div>
            </div>

            <div class="card">
                <!-- <div class="card-img"></div> -->
                <div class="card-body">
                    <h4>독서 스터디</h4>
                    <div class="card-info">정원 6/8 · 인천</div>
                </div>
            </div>

            <div class="card">
               <!--  <div class="card-img"></div> -->
                <div class="card-body">
                    <h4>보드게임 모임</h4>
                    <div class="card-info">정원 7/10 · 경기</div>
                </div>
            </div>

            <div class="card">
                <!-- <div class="card-img"></div> -->
                <div class="card-body">
                    <h4>영화 같이 볼 사람</h4>
                    <div class="card-info">정원 5/10 · 서울</div>
                </div>
            </div>

        </div>

    </section>

</div>
<!-- <script>
console.log("${pageContext.request.contextPath}${ad.imageUrl}" + "ddddd" )

</script> -->
<%@include file="./inc/userFooter.jsp" %>