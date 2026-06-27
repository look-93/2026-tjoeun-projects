<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>MOIT | 마이페이지 - 작성한 후기</title>

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

        /* REVIEW COUNT BAR */
        .count-bar {
            background: white;
            padding: 20px 25px;
            border-radius: 20px;
            box-shadow: var(--shadow);
            display: flex;
            align-items: center;
            gap: 12px;
        }

        /* REVIEW TABLE AREA */
        .review-section {
            background: white;
            padding: 30px;
            border-radius: 20px;
            box-shadow: var(--shadow);
        }

        .section-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
        }

        .section-header h2 {
            font-size: 22px;
            color: #333;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .sort-select {
            padding: 10px 16px;
            border-radius: 12px;
            border: 1px solid #e2e8f0;
            background-color: #fff;
            font-size: 14px;
            color: #4a5568;
            outline: none;
            cursor: pointer;
            transition: all 0.2s;
        }
        
        .sort-select:hover {
            border-color: var(--c3);
        }

        .review-table {
            width: 100%;
            border-collapse: collapse;
            text-align: left;
        }

        .review-table th, 
        .review-table td {
            padding: 16px 20px;
            border-bottom: 1px solid #f0f4f8;
            vertical-align: middle;
        }

        .review-table th {
            background-color: #f8fbff;
            color: #666;
            font-weight: 600;
            font-size: 14px;
        }

        .review-table td {
            font-size: 15px;
            color: #333;
        }

        .btn-action {
            padding: 6px 12px;
            border-radius: 8px;
            border: 1px solid #e2e8f0;
            background: #fff;
            font-size: 13px;
            cursor: pointer;
            text-decoration: none;
            margin-right: 4px;
        }

        .btn-edit { color: var(--c4); border-color: #cdd5ff; }
        .btn-edit:hover { background: #f0f3ff; }
        .btn-delete { color: #ff6868; border-color: #ffcdd3; }
        .btn-delete:hover { background: #fff0f1; }

        .badge-status {
            display: inline-block;
            padding: 4px 10px;
            border-radius: 6px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-public { background: #e6fffa; color: #047857; }
        .status-private { background: #f7fafc; color: #4a5568; }

        .star-rating {
            color: #ffb800;
            font-weight: bold;
        }

        @media(max-width:768px) {
            .mypage-wrap { grid-template-columns: 1fr; }
            .section-header { flex-direction: column; align-items: flex-start; gap: 15px; }
            .review-table th, .review-table td { padding: 10px; font-size: 13px; }
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

        <div class="profile-card">
            <div class="profile-img"></div>
            <div>
                <h2>홍길동</h2>
                <p>hong@example.com</p>
                <span class="badge">일반 회원</span>
            </div>
        </div>

        <div class="mypage-wrap">

            <aside class="sidebar">
                <div class="menu">
                    <a href="#">내 정보</a>
                    <a href="#">내 모집글</a>
                    <a href="#">신청한 모임</a>
                    <a href="#">관심 모임</a>
                    <a class="active" href="#">작성한 후기</a>
                    <a href="#">내 신고내역</a>
                    <a href="#">회원정보 수정</a>
                </div>
            </aside>

            <div class="main-content">

                <div class="count-bar">
                    <span style="font-size: 22px;">⭐</span>
                    <span style="font-size: 16px; font-weight: 600; color: #4a5568;">내가 작성한 총 후기 :</span>
                    <span style="font-size: 24px; font-weight: bold; color: var(--c4);">${selectUserReview.size()}</span>
                    <span style="font-size: 16px; font-weight: 600; color: #4a5568;">개</span>
                </div>

                <div class="review-section">
                    <div class="section-header">
                        <h2>내 후기 목록</h2>
                        
                        <select name="status" class="sort-select" id="sortSelect" onchange="changeSort(this.value)">
                            <option value="latest" ${param.sort == 'latest' || empty param.sort ? 'selected' : ''}>최신순</option>
                            <option value="popular" ${param.sort == 'popular' ? 'selected' : ''}>인기순 (좋아요 많은순)</option>
                           
                        </select>
                    </div>

                    <table class="review-table">
                        <thead>
                            <tr>
                                <th style="width: 80px;">ID</th>
                                <th>후기 내용</th>
                                <th style="width: 120px;">별점</th>
                                <th style="width: 120px;">공개여부</th>
                                <th style="width: 150px; text-align: center;">제어</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="review" items="${selectUserReview}">
                                <tr>
                                    <td>${review.id}</td>
                                    <td>${review.content}</td>
                                    <td><span class="star-rating">★ ${review.rating}점</span></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${review.isPublic == 'Y' || review.isPublic == '1'}">
                                                <span class="badge-status status-public">공개</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge-status status-private">비공개</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td style="text-align: center;">
                                        <button class="btn-action btn-edit" onclick="location.href='${pageContext.request.contextPath}/review/update?id=${review.id}'">수정</button>
                                        <button class="btn-action btn-delete" onclick="if(confirm('정말 삭제하시겠습니까?')) { location.href='${pageContext.request.contextPath}/review/delete?id=${review.id}'; }">삭제</button>
                                    </td>
                                </tr>
                            </c:forEach>
                            
                            <c:if test="${empty selectUserReview}">
                                <tr>
                                    <td colspan="5" style="text-align: center; color: #999; padding: 40px 0;">작성한 후기가 존재하지 않습니다.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>

    <script>
        function changeSort(sortValue) {
            const currentUrl = new URL(window.location.href);
            currentUrl.searchParams.set('sort', sortValue);
            window.location.href = currentUrl.toString();
        }
    </script>
</body>

</html>