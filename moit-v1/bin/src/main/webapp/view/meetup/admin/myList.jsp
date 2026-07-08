<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="../../inc/userHeader.jsp" %>

<style>
.page-wrap{
    width:1400px;
    margin:40px auto;
    display:flex;
    gap:30px;
}

.sidebar{
    width:220px;
    background:#fff;
    border-radius:16px;
    box-shadow:0 2px 10px rgba(0,0,0,0.05);
    overflow:hidden;
}

.sidebar .logo{
    padding:25px;
    font-size:32px;
    font-weight:bold;
    color:#2563eb;
    border-bottom:1px solid #eee;
}

.sidebar ul{
    list-style:none;
}

.sidebar li a{
    display:block;
    padding:18px 24px;
    color:#333;
    text-decoration:none;
    border-bottom:1px solid #f5f5f5;
}

.sidebar li.active a{
    background:#2563eb;
    color:#fff;
}

.content{
    flex:1;
}

.page-title{
    font-size:42px;
    font-weight:700;
    margin-bottom:10px;
}

.page-desc{
    color:#666;
    margin-bottom:30px;
}

.stat-wrap{
    display:flex;
    gap:20px;
    margin-bottom:30px;
}

.stat-box{
    flex:1;
    background:#fff;
    border-radius:18px;
    padding:30px;
    box-shadow:0 2px 12px rgba(0,0,0,0.04);
}

.stat-title{
    font-size:18px;
    color:#555;
    margin-bottom:15px;
}

.stat-value{
    font-size:52px;
    font-weight:bold;
    color:#2563eb;
}

.search-box{
    background:#fff;
    padding:25px;
    border-radius:18px;
    box-shadow:0 2px 12px rgba(0,0,0,0.04);
    margin-bottom:20px;
}

.search-row{
    display:flex;
    gap:12px;
}

.search-row select,
.search-row input{
    height:50px;
    border:1px solid #ddd;
    border-radius:10px;
    padding:0 15px;
}

.search-row input{
    flex:1;
}

.btn-search{
    width:100px;
    border:none;
    background:#2563eb;
    color:#fff;
    border-radius:10px;
    font-weight:bold;
}

.table-box{
    background:#fff;
    border-radius:18px;
    overflow:hidden;
    box-shadow:0 2px 12px rgba(0,0,0,0.04);
}

table{
    width:100%;
    border-collapse:collapse;
}

thead{
    background:#f8fafc;
}

th{
    padding:18px;
    border-bottom:1px solid #eee;
}

td{
    padding:18px;
    text-align:center;
    border-bottom:1px solid #f3f3f3;
}

.status{
    display:inline-block;
    padding:6px 14px;
    border-radius:30px;
    font-size:13px;
    font-weight:bold;
}

.pending{
    color:#f97316;
    border:1px solid #f97316;
}

.answered{
    color:#22c55e;
    border:1px solid #22c55e;
}

.pagination{
    display:flex;
    justify-content:center;
    gap:8px;
    padding:30px;
}

.pagination a{
    width:40px;
    height:40px;
    display:flex;
    align-items:center;
    justify-content:center;
    text-decoration:none;
    border:1px solid #ddd;
    border-radius:8px;
    color:#333;
}

.pagination .active{
    background:#2563eb;
    color:#fff;
    border-color:#2563eb;
}
</style>

<div class="page-wrap">

<aside class="sidebar">
    <div class="logo">MOIT</div>
    <ul>
        <li><a href="#">회원관리</a></li>
        <li><a href="#">관리자관리</a></li>
        <li><a href="#">지역관리</a></li>
        <li><a href="#">카테고리관리</a></li>
        <li><a href="#">공지사항</a></li>
        <li><a href="#">행사관리</a></li>
        <li><a href="#">광고관리</a></li>
        <li class="active"><a href="#">1:1 문의</a></li>
        <li><a href="#">시스템 설정</a></li>
    </ul>
</aside>

<section class="content">
    <h1 class="page-title">관리자 1:1 문의</h1>
    <div class="page-desc">
        사용자가 관리자에게 등록한 문의를 조회하고 답변합니다.
    </div>

    <div class="stat-wrap">
        <div class="stat-box">
            <div class="stat-title">전체 문의</div>
            <div class="stat-value">${allCnt}</div>
        </div>

        <div class="stat-box">
            <div class="stat-title">답변 대기</div>
            <div class="stat-value">${pendingCnt}</div>
        </div>

        <div class="stat-box">
            <div class="stat-title">답변 완료</div>
            <div class="stat-value">${answeredCnt}</div>
        </div>

        <div class="stat-box">
            <div class="stat-title">오늘 등록</div>
            <div class="stat-value">${todayCnt}</div>
        </div>
    </div>

    <div class="search-box">
        <form action="" method="get">
            <div class="search-row">
                <select name="type">
                    <option>제목</option>
                    <option>내용</option>
                </select>
                
                <input type="text" name="keyword" placeholder="검색어를 입력하세요">
                <select name="status">
                    <option>전체 문의</option>
                    <option>답변 대기</option>
                    <option>답변 완료</option>
                </select>
                
                <input type="date" name="startDate">
                <input type="date" name="endDate">
                <button class="btn-search">검색</button>
            </div>
        </form>
    </div>

    <div class="table-box">
        <table>
            <thead>
            <tr>
                <th>번호</th>
                <th>제목</th>
                <th>비공개</th>
                <th>작성자</th>
                <th>답변상태</th>
                <th>등록일</th>
                <th>답변일</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach items="${list}" var="q">
                <tr>
                    <td>${q.questionId}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/questions/${q.questionId}">
                            ${q.title}
                        </a>
                    </td>
                    <td>
                        <c:if test="${q.isPublic eq 'N'}">
                            🔒
                        </c:if>
                    </td>
                    <td>${q.memberName}</td>
                    <td>
                        <c:choose>
                            <c:when test="${q.status eq 'PENDING'}">
                                <span class="status pending">답변 대기</span>
                            </c:when>

                            <c:otherwise>
                                <span class="status answered">답변 완료</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td>${q.createdAt}</td>

                    <td>
                        <c:if test="${q.answer != null}">
                            ${q.answer.createdAt}
                        </c:if>
                    </td>

                </tr>

            </c:forEach>

            </tbody>

        </table>

        <div class="pagination">

            <c:if test="${page > 1}">
                <a href="?page=${page-1}">&lt;</a>
            </c:if>

            <c:forEach begin="1" end="${totalPage}" var="i">

                <a href="?page=${i}"
                   class="${page==i ? 'active' : ''}">
                    ${i}
                </a>

            </c:forEach>

            <c:if test="${page < totalPage}">
                <a href="?page=${page+1}">&gt;</a>
            </c:if>

        </div>

    </div>

</section>

</div>


<%@ include file="../../inc/userFooter.jsp" %>
