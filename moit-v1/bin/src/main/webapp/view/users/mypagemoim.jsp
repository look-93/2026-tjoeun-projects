<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>MOIT 마이페이지 - 신청한 모임</title>

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

*{
    margin:0;
    padding:0;
    box-sizing:border-box;
    font-family:"맑은 고딕",sans-serif;
}

body{
    background:var(--bg);
}

.container{
    width:1200px;
    max-width:95%;
    margin:auto;
}

/* HEADER */

header{
    height:80px;
    background:white;
    box-shadow:var(--shadow);
}

.header-inner{
    height:100%;
    display:flex;
    justify-content:space-between;
    align-items:center;
}

.logo{
    font-size:30px;
    font-weight:bold;
    color:var(--c4);
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

.profile-img{
    width:100px;
    height:100px;
    border-radius:50%;
    background:linear-gradient(135deg,var(--c2),var(--c4));
}

.profile-info h2{
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

table{
    width:100%;
    border-collapse:collapse;
}

th{
    background:#f8fbff;
}

th,td{
    padding:15px;
    border-bottom:1px solid #eee;
    text-align:center;
}

/* STATUS */

.status{
    padding:6px 12px;
    border-radius:20px;
    font-size:13px;
}

.status-progress{
    background:#e8f7ff;
    color:#0077cc;
}

.status-end{
    background:#ffe8e8;
    color:#d10000;
}

/* BUTTON */

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

/* MOBILE */

@media(max-width:768px){

    .content{
        grid-template-columns:1fr;
    }

    .stats{
        grid-template-columns:1fr 1fr;
    }

    table{
        font-size:12px;
    }

}

</style>
</head>
<body>

<header>

    <div class="container header-inner">

        <div class="logo">MOIT</div>

        <div>🔔 👤</div>

    </div>

</header>

<div class="container">

    <!-- PROFILE -->

    <div class="profile-card">

        <div class="profile-img"></div>

        <div class="profile-info">

            <h2>홍길동</h2>
            <p>hong@example.com</p>
            <br>

            <span class="badge">일반회원</span>

        </div>

    </div>

    <!-- CONTENT -->

    <div class="content">

        <!-- SIDEBAR (행사 메뉴 제거됨) -->

        <aside class="sidebar">

            <a href="#">내 정보</a>
            <a href="#">내 모집글</a>
            <a href="#" class="active">신청한 모임</a>
            <a href="#">관심 모임</a>
            <a href="#">작성한 후기</a>
            <a href="#">내 신고내역</a>
            <a href="#">회원정보 수정</a>

        </aside>

        <!-- MAIN -->

        <div class="main">

            <!-- STATS -->

            <div class="stats">

                <div class="stat-card">
                    🎉
                    <h3>6</h3>
                    <p>신청 모임</p>
                </div>

                <div class="stat-card">
                    ⭐
                    <h3>3</h3>
                    <p>작성 후기</p>
                </div>

                <div class="stat-card">
                    ❤️
                    <h3>8</h3>
                    <p>관심 모임</p>
                </div>

                <div class="stat-card">
                    📝
                    <h3>14</h3>
                    <p>참여 기록</p>
                </div>

            </div>

            <!-- 모임 목록 -->

            <div class="section">

                <h2>신청한 모임</h2>

                <table>

                    <thead>
                    <tr>
                        <th>모임명</th>
                        <th>모임일</th>
                        <th>상태</th>
                        <th>후기</th>
                    </tr>
                    </thead>

                    <tbody>

                    <tr>
                        <td>코딩 스터디</td>
                        <td>2026.06.10</td>

                        <td><span class="status status-end">종료</span></td>

                        <td><button class="review-btn">후기 작성</button></td>
                    </tr>

                    <tr>
                        <td>운동 모임</td>
                        <td>2026.05.15</td>

                        <td><span class="status status-end">종료</span></td>

                        <td><button class="review-complete">작성완료</button></td>
                    </tr>

                    <tr>
                        <td>독서 모임</td>
                        <td>2026.07.20</td>

                        <td><span class="status status-progress">진행중</span></td>

                        <td>-</td>
                    </tr>

                    </tbody>

                </table>

            </div>

        </div>

    </div>

</div>
<%@include file="../inc/userFooter.jsp" %>