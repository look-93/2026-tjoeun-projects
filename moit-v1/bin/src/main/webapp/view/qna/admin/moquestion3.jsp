<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="../../inc/userHeader.jsp" %>

<style>
.inquiry-wrap{
    max-width:1000px;
    margin:40px auto;
    padding:0 20px;
}

.page-title{
    font-size:36px;
    font-weight:700;
    margin-bottom:12px;
}

.breadcrumb{
    color:#667085;
    margin-bottom:35px;
}

.page-desc{
    color:#667085;
    margin-bottom:30px;
    font-size:15px;
}

.form-card{
    background:#fff;
    border:1px solid #e5e7eb;
    border-radius:16px;
    padding:40px;
}

.form-group{
    margin-bottom:35px;
}

.form-label{
    display:block;
    font-size:18px;
    font-weight:700;
    margin-bottom:14px;
}

.required{
    color:#ef4444;
}

.form-input{
    width:100%;
    height:55px;
    border:1px solid #d0d5dd;
    border-radius:10px;
    padding:0 18px;
    font-size:15px;
}

.form-textarea{
    width:100%;
    height:260px;
    border:1px solid #d0d5dd;
    border-radius:10px;
    padding:18px;
    font-size:15px;
    resize:none;
    line-height:1.8;
}

.radio-group{
    display:flex;
    gap:30px;
    margin-bottom:12px;
}

.radio-item{
    display:flex;
    align-items:center;
    gap:8px;
    font-size:16px;
    cursor:pointer;
}

.radio-item input{
    width:auto;
}

.guide-text{
    color:#667085;
    font-size:14px;
}

.btn-area{
    margin-top:40px;
    text-align:center;
}

.btn{
    min-width:120px;
    height:46px;
    border-radius:10px;
    font-size:15px;
    cursor:pointer;
}

.btn-save{
    background:#2563eb;
    color:#fff;
    border:none;
}

.btn-cancel{
    background:#fff;
    border:1px solid #d0d5dd;
    margin-left:10px;
}
</style>

<div class="inquiry-wrap">
    <h1 class="page-title">모임 1:1 문의글 수정</h1>
    <div class="breadcrumb">1:1 문의 &gt; 문의 수정</div>
    <div class="page-desc">작성한 문의 내용을 수정 후 저장해 주세요.</div>
	    <form action="${pageContext.request.contextPath}/questions/edit" method="post"  onsubmit="return confirm('저장하시겠습니까?');">
	        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	        <!-- 수정할 게시글 PK -->
	        <input type="hidden" name="questionId" value="${data.questionId}">
	        <div class="form-card">
	
	            <div class="form-group">
	                <label class="form-label"> 제목 <span class="required">*</span> </label>
	                <input type="text" name="title" class="form-input" value="${data.title}">
	            </div>
	            
	            <div class="form-group">
	                <label class="form-label">문의 공개 여부</label>
	                <div class="radio-group">
	                    <label class="radio-item">
	                        <input type="radio" name="isPublic" value="Y"
	                               <c:if test="${data.isPublic eq 'Y'}">checked</c:if>> 공개 </label>
	
	                    <label class="radio-item">
	                        <input type="radio" name="isPublic" value="N"
	                               <c:if test="${data.isPublic eq 'N'}">checked</c:if>> 비공개 </label>
	                </div>
	                <div class="guide-text"> 비공개로 설정 시, 관리자만 확인할 수 있습니다. </div>
	            </div>
	
	            <div class="form-group">
	                <label class="form-label"> 문의 내용 <span class="required">*</span> </label>
	                <textarea name="content" class="form-textarea">${data.content}</textarea>
	            </div>
	
	            <div class="btn-area">
	                <button type="submit" class="btn btn-save"> 저장 </button>
	                <button type="button" class="btn btn-cancel" onclick="history.back()"> 취소
	                </button>
	            </div>
	        </div>
	    </form>
</div>

<%@ include file="../../inc/userFooter.jsp" %>
```
