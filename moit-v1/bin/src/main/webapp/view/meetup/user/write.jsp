<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
:root{
    --c1:#B6FFFA;
    --c2:#98E4FF;
    --c3:#80B3FF;
    --c4:#687EFF;

    --bg:#f7faff;
    --white:#fff;
    --text:#222;
    --gray:#777;

    --shadow:0 5px 15px rgba(0,0,0,.07);
    --radius:20px;
}

body{
    background:var(--bg);
    font-family:sans-serif;
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

/* 💡 본문 영역 - fixed 헤더 밑으로 배치 및 푸터와의 하단 숨통 트이는 여백 확보 */
.main-content {
    flex: 1;
    padding-top: 80px;
    padding-bottom: 80px;
    box-sizing: border-box;
}

/* 💡 헤더 .container와 충돌하지 않도록 write 전용 컨테이너 생성 */
.write-container{
    width:1300px;
    max-width:95%;
    margin:auto;
}

/* ================= LAYOUT ================= */
.detail-wrap{
    margin-top:30px;
    display:grid;
    grid-template-columns:2fr 1fr;
    gap:25px;
}

.left-panel{
    display:flex;
    flex-direction:column;
    gap:25px;
}

/* IMAGE */
.image-box{
    background:white;
    padding:20px;
    border-radius:20px;
    box-shadow:var(--shadow);
}

.main-image{
    height:450px;
    border-radius:20px;
    overflow:hidden;
    background:#f5f5f5;
}

.thumb-list{
    display:flex;
    gap:10px;
    margin-top:15px;
}

.thumb{
    flex:1;
    height:80px;
    border-radius:10px;
    overflow:hidden;
}

/* CONTENT */
.content-box{
    background:white;
    padding:30px;
    border-radius:20px;
    box-shadow:var(--shadow);
    margin-bottom:30px;
}

.detail-badge{
    display:inline-block;
    padding:7px 15px;
    background:#dfffe4;
    color:#009933;
    border-radius:50px;
    margin-bottom:15px;
}

.title{
    font-size:32px;
    margin-bottom:20px;
}

.tags{
    display:flex;
    gap:10px;
    flex-wrap:wrap;
    margin-bottom:20px;
}

.tag{
    background:#eef7ff;
    color:var(--c4);
    padding:8px 12px;
    border-radius:50px;
}

/* SIDE */
.side-box{
    background:white;
    padding:25px;
    border-radius:20px;
    box-shadow:var(--shadow);
    margin-bottom:20px;
}

.info-row{
    display:flex;
    justify-content:space-between;
    margin-bottom:15px;
}

.btn{
    width:100%;
    border:none;
    padding:15px;
    border-radius:12px;
    cursor:pointer;
}

.btn-primary{
    background:var(--c4);
    color:white;
}

.btn-outline{
    margin-top:10px;
    background:white;
    border:2px solid var(--c4);
    color:var(--c4);
}

/* ================= TABS ================= */
.tabs{
    background:white;
    border-radius:20px;
    box-shadow:var(--shadow);
    overflow:hidden;
}

.tab-header{
    display:flex;
}

.tab-btn{
    flex:1;
    padding:20px;
    text-align:center;
    cursor:pointer;
    border-bottom:3px solid transparent;
}

.tab-btn.active{
    border-color:var(--c4);
    color:var(--c4);
    font-weight:bold;
}

.tab-content{padding:25px;}

.tab-panel{display:none;}
.tab-panel.active{display:block}

/* REVIEW */
.rating-box{
    display:flex;
    gap:40px;
    margin-bottom:20px;
}

.rating-score h1{
    font-size:60px;
    color:var(--c4);
}

.stars{color:gold}

.bar-line{
    width:200px;
    height:8px;
    background:#eee;
    border-radius:10px;
    overflow:hidden;
}

.bar-fill{
    height:100%;
    background:orange;
}

/* COMMENT */
.comment{
    padding:15px 0;
    border-bottom:1px solid #eee;
}

.comment-images{
    display:flex;
    gap:10px;
    margin-top:10px;
}

.comment-images img{
    width:100px;
    height:100px;
    object-fit:cover;
    border-radius:10px;
}

/* 추천모임 */
.recommend-card{
    display:flex;
    gap:10px;
    margin-bottom:12px;
}

.recommend-card img{
    width:60px;
    height:60px;
    border-radius:10px;
    object-fit:cover;
}

.recommend-title{
    font-size:12px;
    font-weight:bold;
}

.recommend-tag{
    font-size:10px;
    color:#666;
}

.report-btn{
    border:none;
    background:#ffeded;
    color:#e53935;
    padding:6px 12px;
    border-radius:8px;
    cursor:pointer;
    font-size:12px;
    font-weight:600;
}

.report-btn:hover{
    background:#ffd6d6;
}

.content-top{
    display:flex;
    justify-content:space-between;
    align-items:flex-start;
    margin-bottom:15px;
}

.comment-top{
    display:flex;
    justify-content:space-between;
    align-items:center;
    margin-bottom:5px;
}

/* 💡 [명칭 변경] 헤더의 .inquiry-btn과의 충돌 우려로 고유 이름 부여 */
.form-inquiry-btn{
    width:100%;
    margin-top:12px;
    border:none;
    background:var(--c2);
    color:#222;
    padding:12px;
    border-radius:10px;
    cursor:pointer;
    font-weight:600;
}

/* SIDEBAR AD */
.sidebar-ad{
    margin-top:25px;
    min-height:450px;

    background:linear-gradient(
        180deg,
        var(--c4),
        var(--c3)
    );

    border-radius:20px;
    padding:25px;

    display:flex;
    flex-direction:column;
    justify-content:center;
    align-items:center;

    text-align:center;
    color:white;

    box-shadow:var(--shadow);
}

.sidebar-ad .ad-tag{
    background:rgba(255,255,255,.2);
    padding:5px 12px;
    border-radius:20px;
    margin-bottom:15px;
    font-size:12px;
}

.sidebar-ad h3{
    font-size:24px;
    margin-bottom:15px;
    line-height:1.4;
}

.sidebar-ad p{
    margin-bottom:20px;
    line-height:1.6;
}

.sidebar-ad a{
    background:white;
    color:var(--c4);

    text-decoration:none;

    padding:12px 20px;
    border-radius:12px;

    font-weight:bold;
}

.form-input{
    width:100%;
    padding:14px;
    border:1px solid #ddd;
    border-radius:12px;
    margin-top:8px;
    font-size:14px;
    box-sizing:border-box;
}

.form-input:focus{
    outline:none;
    border-color:var(--c4);
    box-shadow:0 0 0 3px rgba(104,126,255,.15);
}

textarea.form-input{
    resize:vertical;
    min-height:200px;
}

.form-select {
    width: 100%;
    padding: 14px;
    border: 1px solid #ddd;
    border-radius: 12px;
    margin-top: 8px;
    font-size: 14px;
    box-sizing: border-box;
    background: white;
    cursor: pointer;
}

.form-select:focus {
    outline: none;
    border-color: var(--c4);
    box-shadow: 0 0 0 3px rgba(104,126,255,.15);
}
</style>

<%@ include file="../../inc/userHeader.jsp"%>
<div class="main-content">
    <div class="write-container"> 
    <form action="${empty meetup ? pageContext.request.contextPath.concat('/meetup/user/write.do') : pageContext.request.contextPath.concat('/meetup/mypage/update.do')}" method="post" enctype="multipart/form-data">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
        	<div class="detail-wrap">
    		<input type="hidden" name="meetupId" value="${!empty meetup ? meetup.meetupId:'0'}" />
                <div class="left-panel">
    
                    <!-- <div class="image-box">
                        <div class="main-image" style="display:flex;align-items:center;justify-content:center;">
                            <span style="color:#999;">대표 이미지를 등록해주세요</span>
                        </div>
    
                        <div class="thumb-list">
                            <input type="file" name="images" multiple style="width:100%;">
                        </div>
                    </div> -->
    
                    <div class="content-box">
                        <div class="content-top">
                            <div class="detail-badge">새 모임 등록</div>
                        </div>
    
                        <div style="margin-bottom:20px;">
                            <label>모임 제목</label>
                            <input type="text" name="title" class="form-input" placeholder="모임 제목을 입력하세요" 
                            value="${!empty meetup ? meetup.title : ''}">
                        </div>
    
                        <div style="margin-bottom:20px;">
                            <label>카테고리</label>
                            <select id="categoryId" name="categoryId" class="form-input">
                                <option value="">선택</option>
                                <c:forEach var="category" items="${childCategoryList}" varStatus="status">
                                <option value="${category.categoryId}">${category.categoryName}</option>
                                </c:forEach>
                            </select>
                        </div>
    
                        <div>
                            <label>모임 소개</label>
                            <textarea name="content" rows="10" class="form-input" placeholder="모임에 대한 설명을 작성해주세요">${!empty meetup ? meetup.content : ''}</textarea>
                        </div>
                    </div>
                </div>
    
                <div>
                    <div class="side-box">
                        <h3>모집 정보</h3>
    
                        <div style="margin-bottom:15px;">
                            <label>최소 인원</label>
                            <input type="number" name="minParticipants" class="form-input" min="1" max="100" value="${!empty meetup ? meetup.minParticipants : 0}">
                        </div>
    
                        <div style="margin-bottom:15px;">
                            <label>최대 인원</label>
                            <input type="number" name="maxParticipants" class="form-input" min="1" max="100" value="${!empty meetup ? meetup.maxParticipants : 0}">
                        </div>  
    
                        <div style="margin-bottom:15px;">
                            <label>지역</label>
                            <select id="sigunguId" name="sigunguId" class="form-select">
                                <option value="">지역 선택</option>
                                <c:forEach var="sigungu" items="${sigunguList}" varStatus="status">
                                <option value="${sigungu.sigunguId}">${sigungu.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div style="margin-bottom:15px;">
                            <label>모임 일시</label>
                            <input type="datetime-local" name="meetupAt" class="form-input" value="${!empty meetup ? meetup.meetupAt : ''}">
                        </div>

                        <div style="margin-bottom:15px;">
                            <label>상태</label>
                            <select id="status" name="status" class="form-select">
                                <option value="RECRUITING">모집중</option>
     							<option value="CLOSED">종료됨</option>
     							<!-- <option value="CANCELED">취소</option> -->
     							<!-- <option value="DELETED">삭제됨</option> -->
                            </select>
                        </div>
    
                        <button type="submit" class="btn btn-primary">
                            ${meetup != null ? '모임 수정하기' : '모임 등록하기'}
                        </button>
                    </div>
                    
                    <div class="side-box">
                        <h3>작성 가이드</h3>
                        <p style="line-height:1.8;color:#666;">
                            • 모임 목적을 명확히 작성해주세요.<br>
                            • 장소와 시간을 정확히 입력해주세요.<br>
                            • 참가 조건이 있다면 소개글에 작성해주세요.
                        </p>
                    </div>
                </div>
    
            </div>
    
        </form>
    
    </div>
</div>
<script>
	window.onload = function(){
		const sigunguId = document.getElementById("sigunguId");
		const categoryId = document.getElementById("categoryId");
		const status = document.getElementById("status");
		
		if(${!empty meetup}){
			status.value = "${meetup.status}"
		}	
		
		if(${!empty meetup}){
			sigunguId.value = "${meetup.sigunguId}"
		}	
		
		if(${!empty meetup}){
			categoryId.value = "${meetup.categoryId}"
		}		
	}

</script>

<%@ include file="../../inc/userFooter.jsp"%>