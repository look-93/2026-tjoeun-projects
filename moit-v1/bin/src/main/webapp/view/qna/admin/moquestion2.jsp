<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../../inc/userHeader.jsp" %>

<c:if test="${not empty msg}">
<script>
    alert("${msg}");
</script>
</c:if>

<style>
.inquiry-wrap{
    max-width:1100px;
    margin:40px auto;
    padding:0 20px;
}

.page-title{
    font-size:34px;
    font-weight:700;
    margin-bottom:12px;
}

.breadcrumb{
    color:#667085;
    margin-bottom:30px;
}

.top-btn-area{
    display:flex;
    justify-content:flex-end;
    gap:12px;
    margin-bottom:20px;
}

.btn{
    padding:12px 28px;
    border-radius:10px;
    font-size:15px;
    font-weight:600;
    cursor:pointer;
}

.btn-answer{
    background:#2563eb;
    color:#fff;
    border:none;
}

.btn-edit{
    background:#fff;
    border:1px solid #d0d5dd;
}

.btn-delete{
    background:#fff;
    border:1px solid #ef4444;
    color:#ef4444;
}

.card{
    background:#fff;
    border:1px solid #e5e7eb;
    border-radius:16px;
    padding:28px;
    margin-bottom:24px;
}

.card-title{
    font-size:28px;
    font-weight:700;
    margin-bottom:24px;
}

.info-table{
    width:100%;
    border-collapse:collapse;
}

.info-table th{
    width:140px;
    text-align:left;
    padding:18px 12px;
    border-bottom:1px solid #e5e7eb;
}

.info-table td{
    padding:18px 12px;
    border-bottom:1px solid #e5e7eb;
}

.status{
    display:inline-block;
    padding:6px 12px;
    border:1px solid #fb923c;
    color:#f97316;
    border-radius:8px;
    font-size:13px;
    font-weight:600;
}

.content-box{
    line-height:2;
    min-height:180px;
}

.answer-header{
    display:flex;
    justify-content:space-between;
    margin-bottom:20px;
}

.answer-btns{
    display:flex;
    gap:10px;
}

.btn-answer-edit{
    border:1px solid #2563eb;
    background:#fff;
    color:#2563eb;
}

.btn-answer-delete{
    border:1px solid #ef4444;
    background:#fff;
    color:#ef4444;
}

.answer-content{
    line-height:2.2;
    min-height:220px;
}

.answer-date{
    margin-top:30px;
    font-weight:600;
}

.list-btn-area{
    text-align:center;
    margin-top:30px;
}

.btn-list{
    border:1px solid #d0d5dd;
    background:#fff;
}
</style>

<div class="inquiry-wrap">

    <h1 class="page-title">모임 1:1 문의 상세보기</h1>
    <div class="breadcrumb">1:1 문의 > 문의 상세보기</div>

    <div class="top-btn-area">
        <button class="btn btn-answer"
        		onclick="location.href='${pageContext.request.contextPath}/questions/answer/write/${data.questionId}'">답변등록</button>
        <button class="btn btn-edit"
   				onclick="location.href='${pageContext.request.contextPath}/questions/edit/${data.questionId}'"> 수정 </button>
		<button class="btn btn-delete"
        		onclick="deleteQuestion(${data.questionId})"> 삭제 </button>
    </div>

    <div class="card">
        <div class="card-title">문의 정보</div>
        <table class="info-table">
            <tr>
                <th>제목</th>
                <td>${data.title}</td>
            </tr>
            <tr>
                <th>작성자</th>
                <td>${data.memberId}</td>
            </tr>
            <tr>
                <th>등록일</th>
                <td>${data.createdAt}</td>
            </tr>
            <tr>
                <th>답변상태</th>
                <td>
                	<c:choose>
                		<c:when test="${data.status eq 'ANSWERED'}">
	                   		<span class="status">답변 완료</span>
	                    </c:when>
	                    <c:otherwise>
	                    	<span class="status">답변 대기</span>
	                    </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <th>공개여부</th>
                <td>
				    <c:choose>
				        <c:when test="${data.isPublic eq 'N'}">
				            비공개 상태입니다. 🔒
				        </c:when>
				        <c:otherwise>
				            공개 상태입니다.
				        </c:otherwise>
				    </c:choose>
				</td>
            </tr>
        </table>
    </div>

    <div class="card">
        <div class="card-title">문의 내용</div>
        <div class="content-box">
        	${data.content}
        </div>
    </div>
    
	<c:if test="${not empty data.answer}">
	    <div class="card">
	        <div class="answer-header">
	        <div class="card-title">답변 내용</div>
	        
	            <div class="answer-btns">
	                <button class="btn btn-answer-edit"
						onclick="location.href='${pageContext.request.contextPath}/questions/answer/edit/${data.questionId}'"> 답변수정 </button>
	                <button class="btn btn-answer-delete"
						onclick="deleteAnswer(${data.answer.answerId}, ${data.questionId})"> 답변삭제 </button>
	            </div>
	        </div>
	        <div class="answer-content"> ${data.answer.content} </div>
	        <div class="answer-date">답변일 ${data.answer.createdAt}</div>
	    </div>
	    <div class="list-btn-area" onclick="location.href='${pageContext.request.contextPath}/questions'">
	        <button class="btn btn-list">목록으로</button>
	    </div>
	</c:if>
	<script>
		function deleteQuestion(questionId){
		    if(confirm("정말 삭제하시겠습니까?")){
		        location.href = "${pageContext.request.contextPath}/questions/delete/" + questionId; }
		}
		function deleteAnswer(answerId, questionId){
		    if(confirm("답변을 삭제하시겠습니까?")){
		        location.href = "${pageContext.request.contextPath}/questions/answer/delete/" + answerId + "/" + questionId; }
		}
	</script>
</div>

<%@ include file="../../inc/userFooter.jsp" %>