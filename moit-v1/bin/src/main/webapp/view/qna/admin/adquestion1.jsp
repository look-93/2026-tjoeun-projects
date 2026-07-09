<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../inc/userHeader.jsp" %>

<style>
.inquiry-wrap{max-width:1100px;margin:30px auto;padding:0 20px;}
.page-card{background:#fff;border:1px solid #e5e7eb;border-radius:12px;padding:40px;}
.page-title{font-size:32px;font-weight:700;margin-bottom:10px;}
.breadcrumb{color:#6b7280;margin-bottom:30px;}
.form-box{background:#fff;border:1px solid #eef0f4;border-radius:12px;padding:35px;}
.form-box h3{margin-bottom:30px;}
.form-group{margin-bottom:25px;}
.form-group label{display:block;font-weight:600;margin-bottom:10px;}
.form-group input,.form-group textarea{
width:100%;border:1px solid #d9dfea;border-radius:8px;padding:14px;
}
.form-group textarea{height:300px;resize:none;}
.btn-area{text-align:right;margin-top:30px;}
.btn-primary,.btn-cancel{
padding:12px 28px;border-radius:8px;border:1px solid #d9dfea;cursor:pointer;
}
.btn-primary{background:#1d4ed8;color:#fff;border:none;}
.checkbox-label{ display:flex !important; align-items:center; gap:8px; margin-bottom:0 !important; }
.checkbox-label input{ width:auto !important; }
.required{ color:#ef4444; }
</style>


<div class="inquiry-wrap">
    <div class="page-card">
        <h1 class="page-title">관리자 1:1 문의 등록</h1>
        <div class="breadcrumb">1:1 문의 > 문의 등록</div>

		<form action="${pageContext.request.contextPath}/questions/write"
              method="post"
              onsubmit="return ck()">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

            <!-- 임시값 -->
            <input type="hidden" name="memberId" value="1">
            <input type="hidden" name="parentId" value="0">
      
        
            <!-- 관리자 문의 -->
            <input type="hidden" name="category" value="ADMIN">
            <input type="hidden" name="deleteYn" value="N">
            
	        <div class="form-box">
	            <h3>문의 정보 입력</h3>
	
	            <div class="form-group">
	                <label> 제목 <span class="required">*</span> </label>
	                <input type="text" name="title" id="title" placeholder="제목을 입력하세요">
	            </div>
	
	            <div class="form-group">
	                <label> 문의 내용 <span class="required">*</span> </label>
	                <textarea name="content" id="content" placeholder="문의 내용을 입력하세요."></textarea>
	            </div>
	
	            <div class="form-group">
	                <label class="checkbox-label"> <input type="checkbox" id="privateChk">비공개 문의</label>
	                <input type="hidden" name="isPublic" id="isPublic" value="Y">
	            </div>
	
	            <div class="btn-area">
	                <button class="btn-primary"
	        		onclick="location.href='${pageContext.request.contextPath}/view/qna/admin/adquestion2.jsp'">등록하기</button>
	                <button class="btn-cancel" onclick="history.back()">취소</button>
	            </div>
	        </div>
        
        </form>
    </div>
</div>
<script>
	document.getElementById("privateChk")
	    .addEventListener("change", function() {
	
	    document.getElementById("isPublic").value = this.checked ? "N" : "Y";
	});
	function ck() {
	    let title = document.getElementById("title").value.trim();
	    let content = document.getElementById("content").value.trim();
	
	    if(title === ""){ alert("제목을 입력해주세요."); document.getElementById("title").focus(); return false; }
	    if(content === ""){ alert("문의 내용을 입력해주세요."); document.getElementById("content").focus(); return false; }
	    return confirm("문의를 등록하시겠습니까?");
	}
</script>

<%@ include file="../../inc/userFooter.jsp" %>
