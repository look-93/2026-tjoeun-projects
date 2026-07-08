<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <title>MOIT | 후기 테스트 및 모달</title>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  
  <style>
    :root { 
      --c1: #B6FFFA; 
      --c2: #98E4FF; 
      --c3: #80B3FF; 
      --c4: #687EFF; 
      --bg: #f7faff; 
      --shadow: 0 5px 20px rgba(0, 0, 0, .05);
    }
    
    body {
      background: var(--bg); 
      font-family: 'Pretendard', '맑은 고딕', sans-serif;
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
</head>
<body>

<%@include file="../inc/userHeader.jsp" %>

<div class="container mt-5">
  <div class="card p-4 shadow-sm bg-white" style="border-radius: 20px; border: none;">
    <h3 class="fw-bold mb-2" style="color: var(--c4);">🎯 내 후기 목록 및 검증</h3>
    <p class="text-muted small">모임 번호: <span class="badge bg-primary">${meetupId}</span></p>
    
    <div class="row g-3 my-2 align-items-center">
  
  <div class="col-md-3">
    <button type="button" class="btn btn-primary fw-bold w-100 py-2" data-bs-toggle="modal" data-bs-target="#myModal" style="background-color: var(--c4); border: none; border-radius: 10px;">
      ✍️ 후기 작성하기
    </button>
  </div>
  
  <div class="col-md-5">
    <form action="${pageContext.request.contextPath}/review/searchContent" method="GET" class="m-0">
      <div class="input-group">
        <input type="text" name="Keyword" class="form-control" placeholder="후기 내용 검색..." value="${param.Keyword}" style="border-radius: 10px 0 0 10px; height: 40px;">
        <button class="btn btn-outline-secondary" type="submit" style="border-radius: 0 10px 10px 0;">🔍 검색</button>
      </div>
    </form>
  </div>
  
  <div class="col-md-4">
    <form action="${pageContext.request.contextPath}/review/test" method="GET" class="m-0">
      <input type="hidden" name="meetupId" value="${meetupId}">
      
      <select class="form-select text-secondary" name="sortType" onchange="this.form.submit()" style="border-radius: 10px; height: 40px; font-weight: 500;">
        <option value="latest" ${param.sortType != 'popular' ? 'selected' : ''}>⏱️ 최신순 정렬</option>
        <option value="popular" ${param.sortType == 'popular' ? 'selected' : ''}>⭐ 인기순 (좋아요 많은순)</option>
      </select>
    </form>
  </div>

</div>

    <table class="table table-hover mt-3 align-middle text-center">
      <thead class="table-light">
        <tr>
          <th style="width: 10%">ID</th>
          <th style="width: 40%">후기 내용</th>
          <th style="width: 10%">별점</th>
          <th style="width: 10%">좋아요</th>
          <th style="width: 10%">공개여부</th>
          <th style="width: 20%">제어</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty selectUserReview}">
            <c:forEach var="review" items="${selectUserReview}">
              <tr>
                <td>${review.id}</td>
                <td class="text-start"><c:out value="${review.content}" /></td>
                <td class="text-warning">★ ${review.rating}점</td>
                
                <td>
                  <button type="button" class="btn-like-trigger" onclick="toggleLike(${review.id})">
                    ❤️ <span id="like-count-${review.id}">0</span>
                  </button>
                </td>
                
                <td>
                  <c:choose>
                    <c:when test="${review.isPublic == '1' || review.isPublic == 'Y'}"><span class="badge bg-info">🔓 공개</span></c:when>
                    <c:otherwise><span class="badge bg-secondary">🔒 비공개</span></c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <button type="button" class="btn btn-sm btn-outline-warning me-1" style="border-radius: 6px;" data-bs-toggle="modal" data-bs-target="#editModal-${review.id}">
                    수정
                  </button>

                  <button type="button" class="btn btn-sm btn-outline-danger" style="border-radius: 6px;" data-bs-toggle="modal" data-bs-target="#deleteModal-${review.id}">
                    삭제
                  </button>

                  <div class="modal fade text-start" id="editModal-${review.id}" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                      <div class="modal-content" style="border-radius: 20px; overflow: hidden; border: none; box-shadow: var(--shadow);">
                        <div class="modal-header" style="border-bottom: 1px solid #f1f5f9;">
                          <h5 class="modal-title fw-bold text-warning">📝 후기 수정</h5>
                          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <form action="${pageContext.request.contextPath}/review/test" method="POST" enctype="multipart/form-data" onsubmit="return validateEditForm('${review.id}');">
                          <div class="modal-body" style="padding: 25px;">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
                            <input type="hidden" name="id" value="${review.id}">
                            <input type="hidden" name="meetupId" value="${review.meetupId}">
                            <input type="hidden" name="rating" id="editRating-${review.id}" value="${review.rating}">

                            <div class="mb-4">
                              <h3 class="section-title">별점 수정</h3>
                              <div class="stars" data-review-id="${review.id}">
                                <c:forEach var="i" begin="1" end="5">
                                  <span class="edit-star ${i <= review.rating ? 'active' : ''}" data-value="${i}">★</span>
                                </c:forEach>
                              </div>
                            </div>

                            <div class="mb-4">
                              <h3 class="section-title">공개 여부 변경</h3>
                              <div class="status-wrap">
                                <div class="form-check">
                                  <input class="form-check-input" type="radio" name="isPublic" id="editPublicY-${review.id}" value="1" ${review.isPublic == '1' || review.isPublic == 'Y' ? 'checked' : ''}>
                                  <label class="form-check-label fw-medium text-dark" for="editPublicY-${review.id}">🔓 전체 공개</label>
                                </div>
                                <div class="form-check">
                                  <input class="form-check-input" type="radio" name="isPublic" id="editPublicN-${review.id}" value="0" ${review.isPublic == '0' || review.isPublic == 'N' ? 'checked' : ''}>
                                  <label class="form-check-label fw-medium text-secondary" for="editPublicN-${review.id}">🔒 비공개</label>
                                </div>
                              </div>
                            </div>

                            <div class="mb-2">
                              <h3 class="section-title">후기 내용 수정</h3>
                              <textarea name="content" class="review-text" required>${review.content}</textarea>
                            </div>
                          </div>
                          <div class="modal-footer" style="border-top: 1px solid #f1f5f9; background: #fafafa;">
                            <button type="button" class="btn btn-light fw-bold" data-bs-dismiss="modal" style="border-radius: 8px;">취소</button>
                            <button type="submit" class="btn btn-warning fw-bold text-white" style="border-radius: 8px; border:none;">수정 완료</button>
                          </div>
                        </form>
                      </div>
                    </div>
                  </div>

                  <div class="modal fade text-start" id="deleteModal-${review.id}" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                      <div class="modal-content">
                        <div class="modal-header bg-danger text-white">
                          <h5 class="modal-title">⚠️ 삭제 확인</h5>
                          <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="닫기"></button>
                        </div>
                        <div class="modal-body">
                          정말로 <strong>${review.id}</strong>번 리뷰를 삭제하시겠습니까?
                        </div>
                        <div class="modal-footer">
                          <form action="${pageContext.request.contextPath}/review/delete" method="POST">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <input type="hidden" name="id" value="${review.id}">
                            <input type="hidden" name="meetupId" value="${review.meetupId}">
                            <button type="submit" class="btn btn-danger">삭제</button>
                          </form>
                          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                        </div>
                      </div>
                    </div>
                  </div>

                </td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr>
              <td colspan="6" class="text-center text-muted py-5">등록된 후기가 없습니다. 첫 후기를 작성해 보세요!</td>
            </tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>
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

<%@include file="../inc/userFooter.jsp" %>

<script>
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

</body>
</html>