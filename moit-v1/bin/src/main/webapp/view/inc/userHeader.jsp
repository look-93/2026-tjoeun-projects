<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib  prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> 

<!DOCTYPE html>
<html lang="ko">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MOIT</title>
<style>
@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/static/pretendard.min.css');

:root{
    --c1:#B6FFFA;
    --c2:#98E4FF;
    --c3:#80B3FF;
    --c4:#687EFF;

    --bg:#f8fafc;
    --white:#fff;
    --text:#1e293b;
    --gray:#64748b;

    --shadow: 0 4px 20px -2px rgba(104, 126, 255, 0.06);
    --radius:20px;
}

*{
    margin:0;
    padding:0;
    box-sizing:border-box;
}


body {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    background: var(--bg);
    margin: 0; /* 기본 마진 제거 */
}

.container{
    width:1200px;
    max-width:95%;
    margin:auto;
}

/* ================= HEADER (로고+메뉴 좌측 밀집 버전) ================= */


header{
    background:white;
    height:80px;
    box-shadow:var(--shadow);
    position: sticky;
    top: 0;
    z-index: 100;
}

.header-inner{
    height:100%;
    display:flex;
    justify-content:space-between; /* 좌측 그룹과 우측 그룹을 양 끝으로 배치 */
    align-items:center;
}

/* 💡 [NEW] 로고와 메뉴를 한 공간에 묶어 왼쪽에 밀착시킵니다 */
.left-group {
    display: flex;
    align-items: center;
    gap: 45px; /* 로고와 '홈' 메뉴 사이의 간격 */
}

.logo {
   font-size: 24px;
   font-weight: bold;
   color: #4a7dff;
   padding: 25px;
   text-align: center;
   border-bottom: 1px solid #eee;
   text-decoration: none;
}

nav{
    display:flex;
    gap:30px; /* 홈과 모집찾기 사이의 간격 */
}

nav a{
    text-decoration:none;
    color:#475569;
    font-weight: 600;
    padding: 8px 0;
    position: relative;
    transition: color 0.2s ease;
}

nav a:hover, nav a.active {
    color: var(--c4);
}

nav a::after {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 0;
    height: 3px;
    background-color: var(--c4);
    border-radius: 2px;
    transition: width 0.2s ease;
}

nav a:hover::after, nav a.active::after {
    width: 100%;
}

/* 💡 [NEW] 우측 유저박스와 슬로건을 감싸는 그룹 */
.right-group {
    display: flex;
    align-items: center;
    gap: 30px; /* 슬로건과 알림 아이콘 사이의 간격 */
}

/* 예쁜 슬로건 텍스트 바 */
.header-slogan {
    font-size: 13px;
    font-weight: 700;
    color: var(--c4);
    background: rgba(104, 126, 255, 0.08);
    padding: 6px 16px;
    border-radius: 20px;
    letter-spacing: -0.3px;
    display: flex;
    align-items: center;
    gap: 6px;
    user-select: none;
}

.header-slogan::before {
    content: '✨';
}

.user-box{
    display:flex;
    align-items:center;
    gap:24px;
}

.alarm{
    font-size:22px;
    cursor: pointer;
    position: relative;
    transition: transform 0.2s;
}

.alarm:hover {
    transform: scale(1.1);
}

.alarm::after {
    content: '';
    position: absolute;
    top: -2px;
    right: -2px;
    width: 6px;
    height: 6px;
    background-color: #ef4444;
    border-radius: 50%;
}

.profile{
    display:flex;
    align-items:center;
    gap:12px;
    padding: 6px 16px 6px 6px;
    border-radius:30px;
    border: 1px solid #f1f5f9;
    background:#f8fafc;
    cursor: pointer;
    transition: all 0.2s ease;
}

.profile:hover {
    background: #f1f5f9;
    border-color: #e2e8f0;
}

.profile-img{
    width:38px;
    height:38px;
    border-radius:50%;
    background: linear-gradient(135deg, var(--c3) 0%, var(--c4) 100%);
    color:white;
    display:flex;
    align-items:center;
    justify-content:center;
    font-weight:700;
    box-shadow: 0 2px 6px rgba(104, 126, 255, 0.25);
}

.profile-info{
    display:flex;
    flex-direction:column;
    line-height: 1.4;
}

.profile-info strong{
    font-size:14px;
    color: #1e293b;
    font-weight: 700;
}

.profile-info span{
    font-size:11px;
    color:var(--gray);
    font-weight: 500;
}

/* ==================
<<<<<<< HEAD
HERO
================== */

.hero{
    margin-top:30px;
    background:linear-gradient(
    135deg,
    var(--c1),
    var(--c2)
    );

    border-radius:30px;

    padding:60px;

    display:flex;
    justify-content:space-between;
    align-items:center;

    overflow:hidden;
}

.hero h1{
    font-size:52px;
    line-height:1.3;
    margin-bottom:20px;
}

.hero p{
    color:var(--gray);
    margin-bottom:25px;
}

.hero-btn{
    display:flex;
    gap:15px;
}

.btn{
    border:none;
    padding:14px 28px;
    border-radius:50px;
    cursor:pointer;
    font-size:16px;
}

.btn-primary{
    background:var(--c4);
    color:white;
}

.btn-light{
    background:white;
}

.hero-image{
    font-size:180px;
}

/* ==================
CATEGORY
================== */

.category{
    margin-top:25px;

    display:grid;
    grid-template-columns:repeat(8,1fr);
    gap:15px;
}

.category-card{
    background:white;
    padding:20px;
    border-radius:20px;
    text-align:center;
    box-shadow:var(--shadow);
}

.category-card span{
    display:block;
    margin-top:10px;
}

/* ==================
AD
================== */

.ad-banner{
    margin-top:25px;
    background:white;

    /* border:2px dashed var(--c3); */

    border-radius:20px;

    /* padding:25px; */

    display:flex;
    justify-content:space-between;
    align-items:center;

    box-shadow:var(--shadow);
    
    height: 250px;
    padding: 0;
    overflow: hidden;
    
    
}

.ad-banner a {
    display: block;
    width: 100%;
}
.ad-banner img {
    display: block;
    width: 100%;
    height: auto;
    border-radius: 20px;
}
.ad-left h3{
    margin-bottom:10px;
}

.ad-btn{
    background:var(--c4);
    color:white;
    padding:10px 20px;
    border-radius:50px;
}

/* ==================
SECTION
================== */

.section{
    margin-top:40px;
}

.section-title{
    margin-bottom:20px;
    font-size:24px;
}

/* ==================
CARD
================== */

.card-wrap{
    display:grid;
    grid-template-columns:
    repeat(4,1fr);

    gap:20px;
}

.card{
    background:white;
    border-radius:20px;
    overflow:hidden;
    box-shadow:var(--shadow);
}

.card-img{
    height:180px;
    background:
    linear-gradient(
    135deg,
    var(--c2),
    var(--c3)
    );
}

.card-body{
    padding:20px;
}

.card-body h4{
    margin-bottom:10px;
}

.card-info{
    color:var(--gray);
    font-size:14px;
}

/* ==================
RECOMMEND
================== */

.recommend{
    display:grid;
    grid-template-columns:repeat(3,1fr);
    gap:20px;
}

.recommend-box{
    padding:30px;
    border-radius:20px;
    box-shadow:var(--shadow);
}

.recommend-box:nth-child(1){
    background:#eafcff;
}

.recommend-box:nth-child(2){
    background:#fffceb;
}

.recommend-box:nth-child(3){
    background:#f0fff5;
}

/* ==================
BOTTOM
================== */

.bottom-grid{
    display:grid;
    grid-template-columns:1fr 1fr;
    gap:20px;
}

.notice,
.review{
    background:white;
    padding:25px;
    border-radius:20px;
    box-shadow:var(--shadow);
}

.notice li,
.review li{
    list-style:none;
    padding:10px 0;
    border-bottom:1px solid #eee;
}


/* ==================
FOOTER
================== */
 

footer {
    margin-top: auto; /* 💡 flex 구조에서 자동으로 최하단에 배치됨 */ 
    background: white;
    padding: 30px;
    text-align: center;
    color: var(--gray);
    border-top: 1px solid #edf2f7; 
}

/* ==================
RESPONSIVE
================== */

@media(max-width:768px){

.hero{
    flex-direction:column;
    text-align:center;
}

.hero h1{
    font-size:32px;
}

.hero-image{
    font-size:100px;
}

.category{
    grid-template-columns:repeat(2,1fr);
}

.card-wrap{
    grid-template-columns:1fr;
}

.recommend{
    grid-template-columns:1fr;
}

.bottom-grid{
    grid-template-columns:1fr;
}

nav{
    display:none;
}

.search{
    display:none;
}

}

/* ==================
LOGIN
================== */
.auth-box{
    background:white;
    padding:50px;
    border-radius:var(--radius);
    box-shadow:var(--shadow);
    width:100%;
    max-width:600px;
    margin:60px auto;
}
.tab-menu{
    display:flex;
    margin-bottom:30px;
    border-bottom:1px solid #eee;
}
.tab-btn{
    flex:1;
    padding:14px;
    border:none;
    background:none;
    cursor:pointer;
    font-weight:600;
    color:var(--gray);
    font-size:16px;
}
.tab-btn.active{
    color:var(--c4);
    border-bottom:3px solid var(--c4);
}
.tab-content{display:none;}
.tab-content.active{display:block;}
.form-group{margin-bottom:25px;}
.form-group input{
    width:100%;
    padding:18px;
    border:1px solid #ddd;
    border-radius:10px;
    font-size:16px;
}
.form-options{margin:20px 0;display:flex;gap:15px;flex-wrap:wrap;}
.auth-links{
  margin-top:25px;
  text-align:center;
  font-size:14px;
  color:var(--gray);
}

.auth-links .link-row{
  margin:10px 0;
}

.auth-links a{
  color:var(--c3);
  font-weight:600;
  margin:0 5px;
}

.auth-links hr{
  border:none;
  border-top:1px solid #eee;
  margin:15px 0;
}

.btn.full{width:100%;margin-top:20px;}
.member-type{
  display:flex;
  gap:30px;
  margin-bottom:25px;
  font-size:15px;
}
.member-type input{
  margin-right:8px;
}

/* 회원가입 박스 */
.signup-box{max-width:600px;margin:60px auto;background:white;padding:40px;border-radius:var(--radius);box-shadow:var(--shadow);}
h2{text-align:center;color:var(--c4);margin-bottom:30px;}
.form-group{margin-bottom:20px;}
.form-group input{width:100%;padding:14px;border:1px solid #ddd;border-radius:10px;font-size:15px;}
.form-group-inline{display:flex;gap:10px;margin-bottom:20px;}
.form-group-inline input{flex:1;padding:14px;border:1px solid #ddd;border-radius:10px;}
.form-options{margin:20px 0;}
.member-type{display:flex;gap:30px;margin-top:10px;font-size:15px;}
.member-type input{margin-right:8px;}
.hint{color:#999;font-size:13px;margin-top:6px;}
footer{margin-top:50px;background:white;padding:30px;text-align:center;}

.target{
    margin-top:8px;
    font-size:13px;
    font-weight:600;
}

.text-success{
    color:#28a745;
}

.text-danger{
    color:#dc3545;
}

.text-warning{
    color:#ff9800;
}


/* ================= 헤더 깨짐 방지 반응형 코드 추가 ================= */
@media (max-width: 1024px) {
    /* 1. 화면이 좁아지면 자리를 많이 차지하는 슬로건을 숨깁니다 */
    .header-slogan {
        display: none !important;
    }
    
    /* 2. 로고와 메뉴, 유저박스 사이의 간격을 좁혀 여유 공간을 만듭니다 */
    .left-group {
        gap: 20px;
    }
    nav {
        gap: 15px;
    }
    .right-group {
        gap: 15px;
    }
    .user-box {
        gap: 12px;
    }
}

@media (max-width: 768px) {
    /* 3. 모바일 환경에서 글자가 절대 세로로 찢어지지 않도록 강제 고정 */
    nav a, .logo, .profile-info strong {
        white-space: nowrap !important;
        word-break: keep-all !important;
    }
    
    /* 4. 프로필의 등급(일반회원) 텍스트를 숨겨 공간을 확보합니다 */
    .profile-info span {
        display: none;
    }
    .profile {
        padding: 4px 10px 4px 4px;
    }
}
</style>

</head>

<body>

<header>
<div class="container header-inner">
    <!-- 좌측 로고 + 메뉴 -->
    <div class="left-group">
        <a href="${pageContext.request.contextPath}/" class="logo">
            MOIT
        </a>
        <nav>
            <a href="${pageContext.request.contextPath}/meetup/user/list.do"
               class="<c:if test='${menu eq "meetup"}'>active</c:if>">모집찾기</a>
			<a class="inquiry-btn" href="${pageContext.request.contextPath}/questions/adminWrite?category=ADMIN">
			  💬 관리자 1:1 문의
			</a>
        </nav>
    </div>

    <!-- 우측 그룹 -->
    <div class="right-group">
        <div class="header-slogan">
            <span>우리들의 취향 맞춤 소모임 플랫폼</span>
        </div> 

        <nav>
        <!-- 로그인 안한 상태 -->
        <sec:authorize access="isAnonymous()">            
            <a class="nav-link inquiry-btn" href="${pageContext.request.contextPath}/users/login">로그인</a> 
            <a class="nav-link inquiry-btn" href="${pageContext.request.contextPath}/users/join">회원가입</a>                             
        </sec:authorize>
        
        <!-- 로그인한 상태 -->
        <sec:authorize access="isAuthenticated()"> 
                <sec:authentication property="principal.dto" var="loginUser"/>
     			<a href="${pageContext.request.contextPath}/meetup/mypage/meetup.do">
	     			<div class="profile">
	                    <div class="profile-img">${loginUser.nickname.substring(0,1)}</div>
	                    <div class="profile-info">
	                        <strong>${loginUser.nickname}님</strong>
	                        <span>
	                            <c:choose>
	                                <c:when test="${loginUser.typeName eq 'ROLE_MEMBER'}">  
	                                    일반회원
	                                </c:when>
	                                <c:when test="${loginUser.typeName eq 'ROLE_PARTNER'}">
	                                    제휴업체
	                                </c:when>
	                                <c:when test="${loginUser.typeName eq 'ROLE_ADMIN'}">
	                                    관리자
	                                </c:when>
	                                <c:otherwise>
	                                    ${loginUser.typeName}
	                                </c:otherwise>
	                            </c:choose>
	                        </span>
	                    </div>
	                </div>
     			</a>
                
                
                <!-- 로그아웃 버튼 -->
                <form action="${pageContext.request.contextPath}/users/logout" method="post">
                   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                   <input type="submit" value="로그아웃" class="btn btn-danger">
                </form> 
        </sec:authorize>
        </nav>
    </div>

</div>
</header> 