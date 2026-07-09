<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../../inc/userHeader.jsp"%>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
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

/* 💡 헤더/푸터 사이 본문 영역 - 푸터 완전히 밀어내고 하단 숨통 트이는 여백 80px 확보 */
.main-content {
    flex: 1;
    padding-top: 80px; 
    padding-bottom: 80px; /* ✨ [핵심] 푸터와 절대 붙지 않도록 여백 지정 */
    box-sizing: border-box;
}

/* container */
.detail-container{
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
    background:white;
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

.tab-content-meetup-detail{padding:25px;}

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

/* 💡 [명칭 변경] 헤더의 관리자 1:1문의 버튼 레이아웃을 해치지 않도록 클래스 이름 분리 */
.detail-inquiry-btn{
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

/* ===============================
   REVIEW TOOLBAR
================================= */

.rv-toolbar {
    display:flex;
    align-items:center;
}

.rv-toolbar .col-md-4 {
    margin-left:auto;
}

.rv-write-btn {
    width:auto;
    padding:0 20px;

	 height: 40px;
	 border: none;
	 border-radius: 10px;
	 background-color: var(--c4);
	 color: white;
	 font-weight: 700;
	 transition: 0.2s;
}

.rv-write-btn:hover {
    opacity: .9;
}


/* 정렬 */
.rv-sort-select {
    /* width: 100%; */
    height: 42px;
    border: 1px solid #dee2e6;
    border-radius: 10px;
    padding: 0 12px;
    background: #fff;
    color: #666;
    font-weight: 500;
}

/* focus */
.rv-search-input:focus,
.rv-sort-select:focus {
    border-color: var(--c4);
    box-shadow: 0 0 0 3px rgba(104,126,255,.15);
}



.meeting-info {
      display: flex;
      gap: 15px;
      margin-bottom: 20px;
      background: #f8fafc;
      padding: 15px;
      border-radius: 12px;
    }
    
    .meeting-img {
      width: 120px;
      height: 90px;
      border-radius: 8px;
      background: linear-gradient(135deg, var(--c2), var(--c4)); 
    }
    
    .meeting-detail h2 {
      font-size: 16px;
      font-weight: 700;
      color: #1e293b;
      margin-bottom: 2px;
    }
    
    .meeting-detail p {
      color: #64748b;
      font-size: 12px;
      margin-bottom: 2px;
    }

.section-title {
      font-size: 15px;
      font-weight: 700;
      margin-bottom: 10px;
      color: #1e293b;
    }
    
    .stars {
      font-size: 28px;
      cursor: pointer;
      display: flex;
      gap: 6px;
      user-select: none;
    }
    .star, .edit-star {
      color: #e2e8f0;
      transition: color 0.15s ease-in-out;
    }
    .star.active, .edit-star.active {
      color: #FFD43B;
    }
    
    .upload-box {
      width: 100%;
      height: 100px;
      border: 2px dashed var(--c3); 
      border-radius: 12px;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-bottom: 10px;
      background: #f8fcff;
      color: #64748b;
      font-size: 13px;
    }
    
    .review-text {
      width: 100%;
      height: 120px;
      border: 1px solid #e2e8f0;
      border-radius: 12px;
      padding: 15px;
      resize: none;
      font-size: 14px;
      outline: none;
      color: #1e293b;
    }
    .review-text:focus {
      border-color: var(--c4);
    }

    .status-wrap {
      display: flex;
      gap: 15px;
    }
    .form-check-input:checked {
      background-color: var(--c4);
      border-color: var(--c4);
    }
    
    .btn-like-trigger {
      border: none;
      background: none;
      padding: 0;
      font-size: 14px;
      color: #ff4b4b;
      font-weight: 600;
      cursor: pointer;
      display: inline-flex;
      align-items: center;
      gap: 3px;
      transition: transform 0.1s ease;
    }
    .btn-like-trigger:active {
      transform: scale(1.2);
    }
</style>

<div class="main-content">
	<div class="container detail-container">
	
		<div class="detail-wrap">
		
			<div class="left-panel">
			
			    <div class="image-box">
			        <div class="main-image">
			            <img src="https://images.unsplash.com/photo-1501555088652-021faa106b9b?auto=format&fit=crop&w=900&q=80" style="width:100%;height:100%;object-fit:cover;">
			        </div>
			        <div class="thumb-list">
			            <div class="thumb"><img src="https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=150&q=80" style="width:100%;height:100%;object-fit:cover;"></div>
			            <div class="thumb"><img src="https://images.unsplash.com/photo-1533240332313-0db49b459ad6?auto=format&fit=crop&w=150&q=80" style="width:100%;height:100%;object-fit:cover;"></div>
			            <div class="thumb"><img src="https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=150&q=80" style="width:100%;height:100%;object-fit:cover;"></div>
			            <div class="thumb"><img src="https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=150&q=80" style="width:100%;height:100%;object-fit:cover;"></div>
			        </div>
			    </div>
			
			    <div class="content-box">
			        <div class="content-top">
			            <div class="detail-badge">모집중</div>
			             <a	href="${pageContext.request.contextPath}/report/user/write.do?targetType=MEETUP&amp;targetId=${detail.meetupId}"
							class="report-btn"> 모임 신고</a>
			        </div>
			        <h1 class="title">${detail.title}</h1>
			    </div>
			
			    <div class="tabs">
			        <div class="tab-header">
			            <div class="tab-btn active" onclick="showTab(0)">상세정보</div>
			            <!-- <div class="tab-btn" onclick="showTab(1)">신청자</div> -->
			            <div class="tab-btn" onclick="showTab(1)">후기</div>
			            <div class="tab-btn" onclick="showTab(2)">Q&amp;A</div>
			        </div>
			
			        <div class="tab-content-meetup-detail">
			        
			            <div class="tab-panel active">
			                <h3>모임 안내</h3>
			                <div class="description">${detail.content}</div>
			            </div>
			
<!-- 			            <div class="tab-panel">
			                <p>홍길동 외 7명</p>
			            </div> -->
			            
			
			            <div class="tab-panel">

						<div class="rv-toolbar row g-3 my-2 align-items-center">
						
						    <div class="col-md-3">
						        <button type="button"
						                class="rv-write-btn"
						                data-bs-toggle="modal"
						                data-bs-target="#myModal">
						            ✍️ 후기 작성하기
						        </button>
						    </div>
						
						
						    <div class="col-md-4">
						        <form action="${pageContext.request.contextPath}/review/test" method="GET">
						
						            <input type="hidden" name="meetupId" value="${meetupId}">
						
						            <select class="rv-sort-select"
						                    name="sortType"
						                    onchange="this.form.submit()">
						
						                <option value="latest">⏱️ 최신순 정렬</option>
						                <option value="popular">⭐ 인기순 (좋아요 많은순)</option>
						
						            </select>
						
						        </form>
						    </div>
						
						</div>
			            
			                <div class="rating-box">
			                    <div class="rating-score">
			                        <h1>4.8</h1>
			                        <div class="stars">★★★★★</div>
			                        <p>총 후기 12개</p>
			                    </div>
			                    <div>
			                        <div class="bar">
			                            <span>5점</span>
			                            <div class="bar-line">
			                                <div class="bar-fill" style="width:80%"></div>
			                            </div>
			                            <span>10</span>
			                        </div>
			                        <div class="bar">
			                            <span>4점</span>
			                            <div class="bar-line">
			                                <div class="bar-fill" style="width:20%"></div>
			                            </div>
			                            <span>2</span>
			                        </div>
			                        <div class="bar">
			                            <span>3점</span>
			                            <div class="bar-line">
			                                <div class="bar-fill" style="width:0%"></div>
			                            </div>
			                            <span>0</span>
			                        </div>
			                    </div>
			                    
			                </div>
			                
		
			            </div>
			
			
			            <div class="tab-panel">
			                <h3>Q&A</h3>
			                <div class="comment">
			                    <div class="comment-name">익명</div>
			                    <div class="comment-date">2026.06.18</div>
			                    <p>초보인데 참여 가능할까요?</p>
			                </div>
			                <div class="comment">
			                    <div class="comment-name">작성자</div>
			                    <div class="comment-date">2026.06.18</div>
			                    <p>네 초보도 환영입니다!</p>
			                </div>
			            </div>
			            
			        </div>			        
			    </div>			
			</div>
			
			<div>
			    <div class="side-box">
			        <h3>모집 정보</h3>
			        <div class="info-row"><span>인원</span><span>${detail.participant}/${detail.maxParticipants}</span></div>
			        <div class="info-row"><span>지역</span><span>${detail.sidoName}</span></div>
					<c:choose>
					    <c:when test="${empty applyInfo}">
					        <form action="${pageContext.request.contextPath}/meetup/user/applyMeetup.do?meetupId=${detail.meetupId}" method="post">
					            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					            <button type="submit" class="btn btn-primary">신청하기</button>
					        </form>
					    </c:when>
					
					    <c:otherwise>
					        <form action="${pageContext.request.contextPath}/meetup/user/cancelApplyMeetup.do" method="post">
					            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					            <input type="hidden" name="meetupId" value="${detail.meetupId}">
					            <input type="hidden" name="applicationId" value="${applyInfo.applicationId}">
					            <button type="submit" class="btn btn-danger">신청취소</button>
					        </form>
					    </c:otherwise>
					</c:choose>		        
			    </div>
			
			    <div class="side-box">
			        <h3>작성자</h3>
			        <p>${detail.nickname}</p>			        
			        <button class="detail-inquiry-btn" onClick="gotoQna()">
			            💬 모임글 문의하기
			        </button>
			    </div>
			
			    <div class="side-box">
			        <h3>추천 모임</h3>
			        <div class="recommend-card">
			            <img src="https://images.unsplash.com/photo-1501555088652-021faa106b9b">
			            <div>
			                <div class="recommend-title">북한산 등산</div>
			                <div class="recommend-tag">서울</div>
			            </div>
			        </div>
			        <div class="recommend-card">
			            <img src="https://images.unsplash.com/photo-1533240332313-0db49b459ad6">
			            <div>
			                <div class="recommend-title">트레킹</div>
			                <div class="recommend-tag">경기</div>
			            </div>
			        </div>
			    </div>
			
			    <div class="sidebar-ad">
			        <div class="ad-tag">ADVERTISEMENT</div>
			        <h3>프리미엄<br>모임 홍보</h3>
			        <p>내 모임을 메인 화면에<br>노출해 보세요</p>
			        <a href="#">광고 신청</a>
			    </div>
			</div>
		
		</div>
	
	</div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered"> 
    <div class="modal-content" style="border-radius: 20px; overflow: hidden; border: none; box-shadow: var(--shadow);">
      <div class="modal-header" style="border-bottom: 1px solid #f1f5f9;">
        <h5 class="modal-title fw-bold" style="color: var(--c4);">후기 작성</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <form action="${pageContext.request.contextPath}/review/test" method="POST" enctype="multipart/form-data" onsubmit="return validateForm();">
        <div class="modal-body" style="padding: 25px;">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
            <input type="hidden" name="meetupId" value="${meetupId != null ? meetupId : 1}"> 
            <input type="hidden" name="memberId" value="10"> 
            <input type="hidden" name="rating" id="reviewRating" value="0">

            <div class="meeting-info">
              <div class="meeting-img"></div>
              <div class="meeting-detail">
                <h2>등산 좋아하는 사람 모여라!</h2>
                <p>📍 서울 | 📅 2026.06.01 ~ 06.15</p>
                <p><span class="badge bg-success" style="font-size: 10px;">👥 참가 완료</span></p>
              </div>
            </div>

            <div class="mb-4">
              <h3 class="section-title">별점 평가</h3>
              <div class="stars">
                <span class="star" data-value="1">★</span>
                <span class="star" data-value="2">★</span>
                <span class="star" data-value="3">★</span>
                <span class="star" data-value="4">★</span>
                <span class="star" data-value="5">★</span>
              </div>
            </div>

            <div class="mb-4">
              <h3 class="section-title">공개 여부 설정</h3>
              <div class="status-wrap">
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="isPublic" id="publicY" value="1" checked>
                  <label class="form-check-label fw-medium text-dark" for="publicY">🔓 전체 공개</label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="isPublic" id="publicN" value="0">
                  <label class="form-check-label fw-medium text-secondary" for="publicN">🔒 비공개</label>
                </div>
              </div>
            </div>

            <div class="mb-4">
              <h3 class="section-title">사진 첨부</h3>
              <label for="reviewFileInput" class="upload-box" style="cursor: pointer;">
                사진 업로드 영역 (클릭하여 추가)
              </label>
              <input type="file" id="reviewFileInput" name="reviewFiles" multiple style="display: none;">
            </div>

            <div class="mb-2">
              <h3 class="section-title">후기 내용</h3>
              <textarea name="content" class="review-text" placeholder="모임 경험을 자유롭게 작성해주세요." required></textarea>
            </div>
        </div>
        <div class="modal-footer" style="border-top: 1px solid #f1f5f9; background: #fafafa;">
          <button type="button" class="btn btn-light fw-bold" data-bs-dismiss="modal" style="border-radius: 8px; color:#64748b;">취소</button>
          <button type="submit" class="btn btn-primary fw-bold" style="background-color: var(--c4); border: none; border-radius: 8px;">후기 등록</button>
        </div>
      </form>
    </div>
  </div>
</div>


<script>
	const csrfHeader = "${_csrf.headerName}";
	const csrfToken = "${_csrf.token}";
	const applyInfo = "${applyInfo}";
	function showTab(i){
		const tabs=document.querySelectorAll(".tab-btn");
		const panels=document.querySelectorAll(".tab-panel");
		
		tabs.forEach(t=>t.classList.remove("active"));
		panels.forEach(p=>p.classList.remove("active"));
		
		tabs[i].classList.add("active");
		panels[i].classList.add("active");
	}
    
    window.onload = function(){
    	const result = "${result}";
    	if(result == "true"){
    		alert("처리 되었습니다.");
    	}else if(result == "false") {
    		alert("관리자에게 문의하세요");	
    	}	
    }
    
    function gotoQna(){
    	location.href ="${pageContext.request.contextPath}/questions/write?category=MEETUP"
    }
    
    	
	function cancelApplyMeetup() {
		if(applyInfo != null){
			const data = {
				applicationId:"${empty applyInfo? '':applyInfo.applicationId}"
		    };
		
		    fetch("${pageContext.request.contextPath}/meetup/user/cancelApplyMeetup.do", {
		        method: "POST",
		        headers: {
		            "Content-Type": "application/json",
		            [csrfHeader]: csrfToken
		        },
		        body: JSON.stringify(data)
		    })
		    .then(response => {
		    	console.log()
		        if (!response.ok) {
		            throw new Error("서버 오류");
		        }
		        return response.json();
		    })
		    .then(result => {
		        console.log(result);
		        alert("취소 완료 되었습니다.");
		    })
		    .catch(error => {
		        console.error(error);
		        alert("취소 실패");
		    });
		}
	    
	}
	
	// 1. 신규 등록 모달 별점 제어 스크립트
	document.querySelectorAll('.star').forEach(star => {
	    star.addEventListener('click', function() {
	        const score = this.getAttribute('data-value');
	        document.getElementById('reviewRating').value = score;
	        
	        document.querySelectorAll('.star').forEach(s => {
	            if (parseInt(s.getAttribute('data-value')) <= parseInt(score)) {
	                s.classList.add('active');
	            } else {
	                s.classList.remove('active');
	            }
	        });
	    });
	});

	// 2. 개별 수정 모달 전용 별점 제어 스크립트
	document.querySelectorAll('.edit-star').forEach(star => {
	    star.addEventListener('click', function() {
	        const score = this.getAttribute('data-value');
	        const parentStars = this.parentElement;
	        const reviewId = parentStars.getAttribute('data-review-id');
	        
	        document.getElementById('editRating-' + reviewId).value = score;
	        
	        parentStars.querySelectorAll('.edit-star').forEach(s => {
	            if (parseInt(s.getAttribute('data-value')) <= parseInt(score)) {
	                s.classList.add('active');
	            } else {
	                s.classList.remove('active');
	            }
	        });
	    });
	});

	// 3. 신규 등록 유효성 검사
	function validateForm() {
	    const rating = document.getElementById('reviewRating').value;
	    if (rating === "0" || rating === "") {
	        alert("모임 만족도 별점을 최소 1점 이상 선택해 주세요!");
	        return false;
	    }
	    return true;
	}

	// 4. 수정 처리 유효성 검사
	function validateEditForm(id) {
	    const rating = document.getElementById('editRating-' + id).value;
	    if (rating === "0" || rating === "") {
	        alert("수정할 별점을 최소 1점 이상 선택해 주세요!");
	        return false;
	    }
	    return true;
	}

	// 5. 좋아요 실시간 비동기(Fetch API) 스크립트
	function toggleLike(reviewId) {
	    const token = document.querySelector('input[name="_csrf"]')?.value;
	    const header = "X-CSRF-TOKEN";
	    
	    fetch('${pageContext.request.contextPath}/review/like', {
	        method: 'POST',
	        headers: {
	            'Content-Type': 'application/x-www-form-urlencoded',
	            [header]: token
	        },
	        body: 'id=' + reviewId
	    })
	    .then(response => {
	        if (!response.ok) throw new Error('네트워크 응답 에러');
	        return response.json();
	    })
	    .then(data => {
	        if(data.status === 'success') {
	            document.getElementById('like-count-' + reviewId).innerText = data.latestLikes;
	        } else {
	            alert('좋아요 처리 중 문제가 발생했습니다.');
	        }
	    })
	    .catch(error => {
	        console.error('Error:', error);
	        alert('서버와의 네트워크 연결에 실패했습니다.');
	    });
	}

</script>


<%@ include file="../../inc/userFooter.jsp"%>