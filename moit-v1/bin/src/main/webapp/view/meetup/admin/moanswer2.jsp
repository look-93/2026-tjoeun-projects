<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../../inc/userHeader.jsp" %>

<style>
.answer-wrap{
    max-width:900px;
    margin:40px auto;
    padding:0 20px;
}

.page-title{
    font-size:38px;
    font-weight:700;
    margin-bottom:15px;
}

.breadcrumb{
    color:#667085;
    font-size:15px;
    margin-bottom:25px;
}

.page-desc{
    color:#667085;
    margin-bottom:30px;
}

.form-card{
    background:#fff;
    border:1px solid #e5e7eb;
    border-radius:14px;
    padding:28px;
}

.info-box{
    background:#f5f9ff;
    border:1px solid #dbe7ff;
    border-radius:12px;
    padding:24px;
    margin-bottom:35px;
}

.info-title{
    font-size:24px;
    font-weight:700;
    margin-bottom:25px;
}

.info-row{
    display:flex;
    align-items:center;
    margin-bottom:20px;
}

.info-row:last-child{
    margin-bottom:0;
}

.info-label{
    width:90px;
    font-weight:700;
    color:#111827;
}

.info-value{
    color:#374151;
    font-size:16px;
}

.answer-label{
    display:block;
    font-size:20px;
    font-weight:700;
    margin-bottom:15px;
}

.required{
    color:#ef4444;
}

.answer-textarea{
    width:100%;
    height:320px;
    border:1px solid #d1d5db;
    border-radius:10px;
    padding:20px;
    resize:none;
    font-size:16px;
    line-height:1.8;
    outline:none;
    box-sizing:border-box;
}

.answer-textarea:focus{
    border-color:#2563eb;
}

.btn-area{
    text-align:center;
    margin-top:30px;
}

.btn{
    min-width:110px;
    height:50px;
    border-radius:10px;
    font-size:16px;
    font-weight:600;
    cursor:pointer;
}

.btn-save{
    background:#2563eb;
    color:#fff;
    border:none;
    margin-right:12px;
}

.btn-cancel{
    background:#fff;
    color:#374151;
    border:1px solid #d1d5db;
}
</style>

<div class="answer-wrap">


<h1 class="page-title">모임글 답변 수정</h1>
<div class="breadcrumb">1:1 문의 &gt; 답변 수정</div>
<div class="page-desc">작성한 답변 내용을 수정 후 저장해 주세요.</div>

<div class="form-card">

    <div class="info-box">
        <div class="info-title">문의 정보</div>

        <div class="info-row">
            <div class="info-label">제목</div>
            <div class="info-value">${data.title}</div>
        </div>

        <div class="info-row">
            <div class="info-label">작성자</div>
            <div class="info-value">
                ${data.memberId}
                &nbsp; | &nbsp;
                ${data.createdAt}
            </div>
        </div>

    </div>

    <form action="${pageContext.request.contextPath}/questions/answer/edit"
          method="post">

        <input type="hidden"
               name="${_csrf.parameterName}"
               value="${_csrf.token}">

        <input type="hidden"
               name="answerId"
               value="${answer.answerId}">

        <input type="hidden"
               name="questionId"
               value="${data.questionId}">

        <label class="answer-label">
            답변 내용 <span class="required">*</span>
        </label>

        <textarea class="answer-textarea"
                  name="content"
                  required>${answer.content}</textarea>

        <div class="btn-area">
            <button type="submit" class="btn btn-save">
                저장
            </button>

            <button type="button"
                    class="btn btn-cancel"
                    onclick="history.back()">
                취소
            </button>
        </div>

    </form>

</div>


</div>

<%@ include file="../../inc/userFooter.jsp" %>
