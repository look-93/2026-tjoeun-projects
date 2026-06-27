<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>MOIT 관리자 - 광고 등록</title>

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

/* ===== content ===== */
body {
    background:#f4f7fc;
}

.page-title{
    font-size:28px;
    font-weight:700;
    margin-bottom:20px;
}

.card-box{
    background:#fff;
    border-radius:15px;
    padding:30px;
    box-shadow:0 2px 10px rgba(0,0,0,0.05);
}

.section-title{
    font-size:18px;
    font-weight:700;
    margin:25px 0 15px;
    color:#333;
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

/* autocomplete */
.suggest-box{
    list-style:none;
    margin:0;
    padding:0;
    border:1px solid #ddd;
    border-radius:10px;
    background:white;
    position:absolute;
    width:100%;
    max-height:200px;
    overflow-y:auto;
    display:none;
    z-index:999;
}

.suggest-box li{
    padding:10px;
    cursor:pointer;
}

.suggest-box li:hover{
    background:#f0f4ff;
}

</style>

</head>

<body>

<div class="container-fluid">
    <div class="row">

        <!-- SIDEBAR -->
        <%@ include file="/view/inc/sidebar.jsp" %>

        <!-- CONTENT -->
        <div class="col-md-10 p-4">

            <h1 class="page-title">광고 등록</h1>

            <form class="card-box" action="${ctx}/advertisement/admin/adWriteAction.do" 
            		method="post" enctype="multipart/form-data">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
			
                <!-- ===== 기본 정보 ===== -->
                <div class="section-title">기본 정보</div>

                <div class="mb-3">
                    <label class="form-label fw-bold">광고 제목</label>
                    <input type="text" class="form-control" name="title">
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">광고 내용</label>
                    <textarea class="form-control" name="content" rows="4"></textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">광고 이미지</label>

                    <div class="preview-box">
				    	<img id="previewImg"
					    	 src="${ctx}${dto.imageUrl}"
					         style="max-width:100%;
					                max-height:100%;">
					</div>

                    <input type="file" id="imageFile" class="form-control" name="imageFile" accept="image/*">
                </div>

                <div class="mb-3">
                    <label class="form-label fw-bold">랜딩 URL</label>
                    <input type="url" class="form-control" name="landingUrl">
                </div>

                <hr>

                <!-- ===== 광고 기간 ===== -->
                <div class="section-title">광고 기간</div>

                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label fw-bold">시작일시</label>
                        <input type="datetime-local" class="form-control" name="startDatetime">
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-bold">종료일시</label>
                        <input type="datetime-local" class="form-control" name="endDatetime">
                    </div>
                </div>


                <hr>

                <!-- ===== 운영 정보 ===== -->
                <div class="section-title">운영 정보</div>

                <div class="mb-3 position-relative">
                    <label class="form-label fw-bold">광고주 (회원 닉네임 검색)</label>

                    <input type="text"
                           id="memberInput"
                           class="form-control"
                           autocomplete="off"
                           placeholder="닉네임 입력">

                    <input type="hidden" name="advertiserId" id="advertiserId">

                    <ul id="suggestBox" class="suggest-box"></ul>
	<script>
	window.addEventListener("load", function() {
	    let memberInput = document.getElementById("memberInput");
	    let target = document.querySelector("#suggestBox");
	    let hiddenId = document.getElementById("advertiserId");

	    memberInput.addEventListener("keyup", function(e) {
	        let value = e.target.value.trim();

	        if (value !== "") {
	            fetch("${pageContext.request.contextPath}/advertisers/search?keyword=" + encodeURIComponent(value))
	                .then(response => response.json())
	                .then(data => {
	                    target.innerHTML = "";

	                    if (data.length > 0) {
	                        data.forEach(item => {
	                            let li = document.createElement("li");
	                            li.textContent = item.nickname;
	                            li.className = "list-group-item list-group-item-action";

	                            li.addEventListener("click", function() {
	                                memberInput.value = item.nickname;
	                                hiddenId.value = item.memberId;
	                                target.style.display = "none"; // 선택 후 닫기
	                            });

	                            target.appendChild(li);
	                        });
	                        target.style.display = "block"; // 결과 있을 때 보이기
	                    } else {
	                        let li = document.createElement("li");
	                        li.textContent = "검색 결과가 없습니다.";
	                        li.className = "list-group-item disabled";
	                        target.appendChild(li);
	                        target.style.display = "block"; // 메시지도 보이기
	                    }
	                })
	                .catch(err => {
	                    target.innerHTML = "<li class='list-group-item list-group-item-danger'>서버 오류입니다.</li>";
	                    target.style.display = "block";
	                });
	        } else {
	            target.innerHTML = "";
	            target.style.display = "none"; // 입력 없으면 닫기
	        }
	    });
	});

	</script>           
	          
                    
                </div>

                <!-- 버튼 -->
                <div class="d-flex justify-content-end gap-2 mt-4">

                    <button type="button" class="btn btn-secondary">취소</button>

                    <button type="submit" class="btn btn-primary">
                        광고 등록
                    </button>

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