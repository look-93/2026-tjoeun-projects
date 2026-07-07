<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>MOIT | 마이페이지</title>

    <style>
        :root {
            --c1: #B6FFFA;
            --c2: #98E4FF;
            --c3: #80B3FF;
            --c4: #687EFF;

            --bg: #f7faff;
            --shadow: 0 5px 20px rgba(0, 0, 0, .08);
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: sans-serif;
        }

        body {
            background: var(--bg);
        }

        .container {
            width: 1400px;
            max-width: 95%;
            margin: auto;
        }

        header {
            height: 80px;
            background: white;
            box-shadow: var(--shadow);
        }

        .header-inner {
            height: 100%;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            font-size: 30px;
            font-weight: bold;
            color: var(--c4);
        }

        /* PROFILE */
        .profile-card {
            margin-top: 30px;
            background: white;
            border-radius: 25px;
            padding: 30px;
            display: flex;
            align-items: center;
            gap: 25px;
            box-shadow: var(--shadow);
        }

        .profile-img {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            background: linear-gradient(135deg, var(--c2), var(--c4));
        }

        .badge {
            display: inline-block;
            padding: 8px 15px;
            border-radius: 30px;
            background: #eef4ff;
            color: var(--c4);
        }

        /* CONTENT */
        .mypage-wrap {
            margin-top: 25px;
            display: grid;
            grid-template-columns: 260px 1fr;
            gap: 25px;
        }

        /* SIDEBAR */
        .sidebar {
            background: white;
            border-radius: 20px;
            padding: 20px;
            box-shadow: var(--shadow);
            height: fit-content;
        }

        .menu {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .menu a {
            padding: 15px;
            border-radius: 12px;
            text-decoration: none;
            color: #333;
        }

        .menu a:hover,
        .menu .active {
            background: var(--c2);
        }

        /* MAIN */
        .main-content {
            display: flex;
            flex-direction: column;
            gap: 25px;
        }

        /* DASHBOARD */
        .dashboard {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 20px;
        }

        .stat-card {
            background: white;
            padding: 25px;
            border-radius: 20px;
            box-shadow: var(--shadow);
            text-align: center;
        }

        .stat-card h3 {
            font-size: 30px;
            color: var(--c4);
            margin-top: 10px;
        }

        /* USER INFO */
        .user-info {
            background: white;
            padding: 25px;
            border-radius: 20px;
            box-shadow: var(--shadow);
        }

        .user-info h2 {
            margin-bottom: 20px;
        }

        .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
        }

        .info-box {
            background: #f8fbff;
            padding: 15px;
            border-radius: 12px;
        }

        .info-title {
            font-size: 12px;
            color: #777;
            margin-bottom: 5px;
        }

        @media(max-width:768px) {

            .mypage-wrap {
                grid-template-columns: 1fr;
            }

            .dashboard {
                grid-template-columns: 1fr;
            }

            .info-grid {
                grid-template-columns: 1fr;
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
            <div>
                <h2>홍길동</h2>
                <p>hong@example.com</p>
                <span class="badge">일반 회원</span>
            </div>
        </div>

        <div class="mypage-wrap">

            <!-- SIDEBAR -->
            <aside class="sidebar">
                <div class="menu">
                    <a class="active" href="#">내 정보</a>
                    <a href="#">내 모집글</a>
                    <a href="#">신청한 모임</a>
                    <a href="#">관심 모임</a>
                    <a href="#">작성한 후기</a>
                    <a href="#">내 신고내역</a>
                    <a href="#">회원정보 수정</a>
                </div>
            </aside>

            <!-- MAIN -->
            <div class="main-content">

                <div class="dashboard">

                    <div class="stat-card">
                        📝
                        <h3>12</h3>
                        <p>내 모집글</p>
                    </div>

                    <div class="stat-card">
                        👥
                        <h3>8</h3>
                        <p>신청 모임</p>
                    </div>

                    <div class="stat-card">
                        ⭐
                        <h3>16</h3>
                        <p>작성 후기</p>
                    </div>

                    <div class="stat-card">
                        ❤️
                        <h3>6</h3>
                        <p>관심 모임</p>
                    </div>

                </div>

                <!-- USER INFO -->
                <div class="user-info">

                    <h2>사용자 정보</h2>

                    <div class="info-grid">

                        <div class="info-box">
                            <div class="info-title">닉네임</div>
                            <div>홍길동</div>
                        </div>

                        <div class="info-box">
                            <div class="info-title">이메일</div>
                            <div>hong@example.com</div>
                        </div>

                        <div class="info-box">
                            <div class="info-title">가입일</div>
                            <div>2026-01-10</div>
                        </div>

                        <div class="info-box">
                            <div class="info-title">관심 카테고리</div>
                            <div>러닝, 독서, 개발</div>
                        </div>

                    </div>

                </div>

            </div>
        </div>
    </div>
<%@include file="../inc/userFooter.jsp" %>