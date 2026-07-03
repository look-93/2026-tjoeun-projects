<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../../inc/userHeader.jsp" %>

<style>

.inquiry-wrap{
    padding:40px 0;
}

.page-header{
    display:flex;
    justify-content:space-between;
    align-items:flex-start;
    margin-bottom:30px;
}

.page-header h1{
    font-size:34px;
    margin-bottom:10px;
}

.page-header p{
    color:#64748b;
}

.write-btn{
    background:#2563ff;
    color:white;
    text-decoration:none;
    padding:14px 24px;
    border-radius:12px;
    font-weight:600;
}

.status-tab{
    display:flex;
    gap:40px;
    margin-bottom:25px;
}

.status-tab a{
    text-decoration:none;
    color:#64748b;
    padding-bottom:10px;
}

.status-tab .active{
    color:#2563ff;
    border-bottom:3px solid #2563ff;
}

.content-layout{
    display:flex;
    gap:30px;
}

.list-section{
    flex:1;
    background:white;
    border-radius:20px;
    padding:25px;
    box-shadow:0 4px 15px rgba(0,0,0,0.04);
}

.search-box{
    display:flex;
    gap:15px;
    margin-bottom:25px;
}

.search-box select,
.search-box input{
    height:46px;
    border:1px solid #e5e7eb;
    border-radius:10px;
    padding:0 15px;
}

.search-box input{
    flex:1;
}

.search-box button{
    width:90px;
    border:none;
    border-radius:10px;
    background:#2563ff;
    color:white;
    cursor:pointer;
}

.inquiry-table{
    width:100%;
    border-collapse:collapse;
}

.inquiry-table th{
    background:#f8fafc;
    padding:18px;
    text-align:center;
}

.inquiry-table td{
    padding:18px;
    text-align:center;
    border-bottom:1px solid #f1f5f9;
}

.inquiry-table tbody tr{
    cursor:pointer;
}

.inquiry-table tbody tr:hover{
    background:#f8fafc;
}

.badge{
    padding:5px 12px;
    border-radius:20px;
    font-size:13px;
    font-weight:600;
}

.waiting{
    color:#f97316;
    border:1px solid #f97316;
}

.complete{
    color:#22c55e;
    border:1px solid #22c55e;
}

.paging{
    display:flex;
    justify-content:center;
    gap:8px;
    margin-top:25px;
}

.paging a{
    width:38px;
    height:38px;
    display:flex;
    align-items:center;
    justify-content:center;
    border:1px solid #e2e8f0;
    border-radius:10px;
    text-decoration:none;
    color:#475569;
}

.paging .active{
    background:#2563ff;
    color:white;
}

.guide-box{
    width:260px;
    background:white;
    border-radius:20px;
    padding:25px;
    box-shadow:0 4px 15px rgba(0,0,0,0.04);
}

.guide-box h3{
    margin-bottom:20px;
}

.guide-item{
    display:flex;
    gap:15px;
    margin-bottom:25px;
}

.icon{
    font-size:24px;
}

.guide-item strong{
    display:block;
    margin-bottom:5px;
}

.guide-item p{
    font-size:13px;
    color:#64748b;
}

</style>

<main class="container inquiry-wrap">

    <div class="page-header">
        <div>
            <h1>모임 문의</h1>
            <p>내가 작성한 문의 내역을 확인할 수 있습니다.</p>
        </div>

        <a href="${pageContext.request.contextPath}/view/meetup/admin/moquestion.jsp"
           class="write-btn">
            ✎ 문의 등록
        </a>
    </div>

    <div class="status-tab">
        <a href="#" class="active">전체</a>
    </div>

    <div class="content-layout">
        <div class="list-section">
            <form class="search-box">
                <select name="type">
                    <option value="title">제목</option>
                    <option value="content">내용</option>
                </select>

                <input type="text"
                       name="keyword"
                       placeholder="검색어를 입력하세요">
                <button type="submit">검색</button>
            </form>

            <table class="inquiry-table">

                <thead>
                    <tr>
                        <th>번호</th>
                        <th>문의구분</th>
                        <th>제목</th>
                        <th>답변상태</th>
                        <th>등록일</th>
                        <th>답변일</th>
                        <th></th>
                    </tr>
                </thead>
                
                <tbody>
                    <c:choose>
                        <c:when test="${empty list}">
                            <tr>
                                <td colspan="7"> 등록된 문의가 없습니다. </td>
                            </tr>
                        </c:when>

                        <c:otherwise>
                            <c:forEach var="q" items="${list}">
                                <tr onclick="location.href='${pageContext.request.contextPath}/questions/${q.questionId}'">
                                    <td>${q.questionId}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${q.category eq 'MEETUP'}"> 모임 문의 </c:when>
                                            <c:otherwise> 관리자 문의 </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>${q.title}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${q.status eq 'PENDING'}">
                                                <span class="badge waiting"> 답변 대기 </span>
                                            </c:when>

                                            <c:otherwise>
                                                <span class="badge complete"> 답변 완료 </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>${q.createdAt}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${q.status eq 'ANSWERED'}"> 완료 </c:when>
                                            <c:otherwise> - </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td>›</td>
                                </tr>

                            </c:forEach>

                        </c:otherwise>

                    </c:choose>

                </tbody>

            </table>

            <div class="paging">
                <c:if test="${page > 1}">
                    <a href="?page=${page-1}">‹</a>
                </c:if>

                <c:forEach begin="1" end="${totalPage}" var="i">
                    <a href="?page=${i}"
                       class="${page eq i ? 'active' : ''}">
                        ${i}
                    </a>
                </c:forEach>

                <c:if test="${page < totalPage}">
                    <a href="?page=${page+1}">›</a>
                </c:if>
            </div>

        </div>

        <aside class="guide-box">
            <h3>이용 가이드</h3>

            <div class="guide-item">
                <div class="icon">📎</div>
                <div>
                    <strong>문의 등록</strong>
                    <p>궁금한 내용을 등록해 주세요.</p>
                </div>
            </div>

            <div class="guide-item">
                <div class="icon">💬</div>
                <div>
                    <strong>답변 확인</strong>
                    <p>작성자의 답변을 확인할 수 있습니다.</p>
                </div>
            </div>

            <div class="guide-item">
                <div class="icon">🗓</div>
                <div>
                    <strong>문의 수정/삭제</strong>
                    <p>답변 전까지 수정 및 삭제가 가능합니다.</p>
                </div>
            </div>

        </aside>

    </div>

</main>

<%@ include file="../../inc/userFooter.jsp" %>