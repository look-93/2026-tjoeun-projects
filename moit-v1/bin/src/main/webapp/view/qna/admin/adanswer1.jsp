<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../../inc/userHeader.jsp" %>

<style>

.answer-wrap{
    max-width:1200px;
    margin:40px auto;
    padding:0 20px;
}

.page-title{
    font-size:34px;
    font-weight:700;
    margin-bottom:15px;
}

.breadcrumb{
    color:#64748b;
    font-size:15px;
    margin-bottom:35px;
}

.breadcrumb span{
    margin:0 8px;
}

.card{
    background:#fff;
    border:1px solid #e5e7eb;
    border-radius:16px;
    padding:35px;
    margin-bottom:25px;
}

.card-title{
    font-size:28px;
    font-weight:700;
    margin-bottom:25px;
}

.inquiry-info{
    background:#f8fbff;
    border:1px solid #dbeafe;
    border-radius:12px;
    overflow:hidden;
}

.info-row{
    display:flex;
    border-bottom:1px solid #e5e7eb;
}

.info-row:last-child{
    border-bottom:none;
}

.info-label{
    width:180px;
    background:#f8fbff;
    font-weight:600;
    padding:20px;
}

.info-value{
    flex:1;
    padding:20px;
    line-height:1.8;
}

.lock{
    margin-left:8px;
}

.form-group{
    margin-top:25px;
}

.form-group label{
    display:block;
    font-size:18px;
    font-weight:600;
    margin-bottom:12px;
}

.required{
    color:red;
}

textarea{
    width:100%;
    height:260px;
    padding:20px;
    border:1px solid #d1d5db;
    border-radius:10px;
    resize:none;
    font-size:15px;
    line-height:1.8;
    box-sizing:border-box;
}

.text-count{
    text-align:right;
    margin-top:10px;
    color:#64748b;
    font-size:14px;
}

.btn-area{
    margin-top:35px;
    text-align:right;
}

.btn{
    min-width:120px;
    height:48px;
    border-radius:10px;
    font-size:15px;
    font-weight:600;
    cursor:pointer;
}

.btn-submit{
    border:none;
    background:#2563eb;
    color:white;
}

.btn-cancel{
    margin-left:10px;
    background:white;
    border:1px solid #d1d5db;
    color:#374151;
}

</style>

<div class="answer-wrap">
    <h1 class="page-title">관리자 1:1 문의 답변 등록</h1>
    <div class="breadcrumb">
        1:1 문의
        <span>></span>
        문의 상세보기
        <span>></span>
        답변 등록
    </div>

    <!-- 문의 정보 -->
    <div class="card">
        <div class="card-title">문의 정보</div>
        <div class="inquiry-info">
            <div class="info-row">
                <div class="info-label">제목</div>
                <div class="info-value"> 이벤트 참여 방법이 궁금합니다. </div>
            </div>

            <div class="info-row">
                <div class="info-label">작성자</div>
                <div class="info-value"> user01 </div>
            </div>

            <div class="info-row">
                <div class="info-label">등록일</div>
                <div class="info-value"> 2026-06-01 </div>
            </div>

            <div class="info-row">
                <div class="info-label">문의 내용</div>
                <div class="info-value">이벤트 참여 방법이 궁금합니다.<br>자세한 안내 부탁드립니다.</div>
            </div>

            <div class="info-row">
                <div class="info-label">비공개 여부</div>
                <div class="info-value"> 비공개 <span class="lock">🔒</span> </div>
            </div>
        </div>
    </div>

    <!-- 답변 입력 -->
    <div class="card">
        <div class="card-title">답변 정보 입력</div>
        <div class="form-group">
            <label> 답변 내용 <span class="required">*</span> </label>
            <textarea placeholder="답변 내용을 입력하세요."></textarea>
            <div class="text-count"> 0 / 1000 </div>
        </div>

        <div class="btn-area">
            <button type="button" class="btn btn-submit" onclick="history.back()">
                등록하기
            </button>
            <button type="button" class="btn btn-cancel" onclick="history.back()">
                취소
            </button>
        </div>
    </div>

</div>

<%@ include file="../../inc/userFooter.jsp" %>