<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn"
    uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>광고 수정</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
/* ===== sidebar ===== */
.sidebar {
    min-height: 100vh;
    background: #fff;
    border-right: 1px solid #dee2e6;
}

.logo {
    font-size: 24px;
    font-weight: bold;
    color: #4a7dff;
    padding: 25px;
    text-align: center;
    border-bottom: 1px solid #eee;
}

.menu a {
    display:block;
    padding:15px 20px;
    text-decoration:none;
    color:#333;
}

.menu a:hover {
    background:#edf3ff;
}

.menu .active {
    background:#4a7dff;
    color:white;
}
.page-title{
	margin-top:50px;
    font-size:28px;
    font-weight:700;
    margin-bottom:20px;
    color: #4a7dff;
}
.card-box {
    background: #fff;
    padding: 30px;
    border-radius: 15px;
}
.preview-box{
    width:100%;
    height:200px;
    background:#eee;
    border-radius:10px;
    margin-bottom: 10px;
    overflow:hidden;

    display:flex;
    justify-content:center;
    align-items:center;
}
#previewImg{
    width:100%;
    height:100%;
    object-fit:cover;
}
</style>

</head>

<body>

<div class="container-fluid">
<div class="row">

    <%@ include file="/view/inc/sidebar.jsp" %>

    <div class="col-md-10 p-4">

        <h1 class="page-title">광고 수정</h1>

        <form class="card-box"
              action="${ctx}/advertisement/admin/adEdit.do"
              method="post"
              enctype="multipart/form-data">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="hidden" name="adId" value="${dto.adId}"/>

            <div class="mb-3">
                <label>제목</label>
                <input type="text" name="title" class="form-control" value="${dto.title}">
            </div>
       
            <div class="mb-3">
                <label>내용</label>
                <textarea name="content" class="form-control">${dto.content}</textarea>
            </div>

            <div class="mb-3">
                <label>기존 이미지</label>
                <div class="preview-box">
			    <img id="previewImg"
			    	 src="${ctx}${dto.imageUrl}"
			    	 onerror="this.src='${pageContext.request.contextPath}/upload/no-image.png'"
			         style="max-width:100%;
			                max-height:100%;">
				</div>

                <input type="file" id="imageFile" name="imageFile" class="form-control" accept="image/*">
                <input type="hidden" name="imageUrl" value="${dto.imageUrl}">
            </div>

            <div class="mb-3">
                <label>랜딩 URL</label>
                <input type="text" name="landingUrl" class="form-control"
                       value="${dto.landingUrl}">
            </div>

            <div class="row">
                <div class="col">
                    <label>시작일</label>
                    <input type="datetime-local" name="startDatetime"
                           class="form-control"
                           value="${fn:replace(fn:substring(dto.startDatetime,0,16),' ','T')}">
                </div>

                <div class="col">
                    <label>종료일</label>
                    <input type="datetime-local" name="endDatetime"
                           class="form-control"
                           value="${fn:replace(fn:substring(dto.endDatetime,0,16),' ','T')}">
                </div>
            </div>
            
            <div class="mb-3">
			    <label>광고 상태</label>
			
			    <select name="status" class="form-select">
			        <option value="PENDING"
			            ${dto.status == 'PENDING' ? 'selected' : ''}>
			            PENDING
			        </option>
			
			        <option value="OPEN"
			            ${dto.status == 'OPEN' ? 'selected' : ''}>
			            OPEN
			        </option>
			
			        <option value="CLOSED"
			            ${dto.status == 'CLOSED' ? 'selected' : ''}>
			            CLOSE
			        </option>
			    </select>
			</div>
            
            <input type="hidden"
			       name="advertiserId"
			       value="${dto.advertiserId}">

            <div class="mt-4 d-flex justify-content-end gap-2">
                <a href="${ctx}/advertisement/admin/adList.do"
                   class="btn btn-secondary">취소</a>

                <button class="btn btn-primary">수정</button>
            </div>

        </form>

    </div>

</div>
</div>
<script>

	const imageInput = document.getElementById("imageFile");
	const previewImg = document.getElementById("previewImg");
	
	imageInput.addEventListener("change", function() {
	
	    const file = this.files[0];
	
	    if (!file) return;
	
	    const reader = new FileReader();
	
	    reader.onload = function(e) {
	        previewImg.src = e.target.result;
	        previewImg.style.display = "block";
	    };
	
	    reader.readAsDataURL(file);
	});

</script>

</body>
</html>