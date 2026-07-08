<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MOIT 관리자 페이지</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>

<style>

body{
    background:#f4f7fc;
}

.sidebar{
    min-height:100vh;
    background:#fff;
    border-right:1px solid #dee2e6;
}

.logo{
    font-size:24px;
    font-weight:bold;
    color:#4a7dff;
    padding:25px;
    text-align:center;
    border-bottom:1px solid #eee;
}

.menu a{
    display:block;
    padding:15px 20px;
    color:#333;
    text-decoration:none;
    font-weight:500;
}

.menu a:hover{
    background:#edf3ff;
}

.menu .active{
    background:#4a7dff;
    color:white;
}

.topbar{
    background:white;
    border-radius:15px;
    padding:20px;
    margin-bottom:20px;
}

.card-box{
    background:white;
    border-radius:15px;
    padding:20px;
    box-shadow:0 2px 5px rgba(0,0,0,.05);
}

.tab-btn{
    background:white;
    border:none;
    padding:10px 20px;
    border-radius:10px;
    margin-right:10px;
}

.tab-btn.active{
    background:#4a7dff;
    color:white;
}

.table-box{
    background:white;
    border-radius:15px;
    padding:20px;
}

</style>

</head>
<body>

<div class="container-fluid">

    <div class="row">

        <!-- 사이드바 -->
        <div class="col-md-2 sidebar p-0">

            <div class="logo">
                MOIT
            </div>

            <div class="menu">
                <a href="#" class="active">회원관리</a>
                <a href="#">관리자관리</a>
                <a href="#">지역관리</a>
                <a href="#">카테고리관리</a>
                <a href="#">공지사항</a>
                <a href="#">행사관리</a>
                <a href="#">광고관리</a>
            </div>

        </div>

        <!-- 메인 -->
        <div class="col-md-10 p-4">

            <!-- 상단 -->
            <div class="topbar d-flex justify-content-between align-items-center">
                <h3>회원관리</h3>

                <div>
                    관리자님
                </div>
            </div>

            <!-- 통계 -->
            <div class="row mb-4">

                <div class="col-md-3">
                    <div class="card-box">
                        <h6>전체 회원</h6>
                        <h2>1,245</h2>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="card-box">
                        <h6>활동 회원</h6>
                        <h2>1,020</h2>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="card-box">
                        <h6>경고 회원</h6>
                        <h2>78</h2>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="card-box">
                        <h6>정지 회원</h6>
                        <h2>147</h2>
                    </div>
                </div>

            </div>

            <!-- 탭 -->
            <div class="mb-4">
                <button class="tab-btn active">회원목록</button>
                <button class="tab-btn">회원등록</button>
                <button class="tab-btn">상태변경</button>
                <button class="tab-btn">비밀번호초기화</button>
            </div>

            <!-- 검색 -->
            <div class="table-box mb-4">

                <div class="row">

                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="회원 검색">
                    </div>

                    <div class="col-md-3">
                        <select class="form-select">
                            <option>전체 상태</option>
                            <option>활동</option>
                            <option>경고</option>
                            <option>정지</option>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <button class="btn btn-primary">
                            검색
                        </button>
                    </div>

                </div>

            </div>

            <!-- 버튼 -->
            <div class="mb-3">
                <button class="btn btn-primary">등록</button>
                <button class="btn btn-warning">수정</button>
                <button class="btn btn-danger">삭제</button>
            </div>

            <!-- 테이블 -->
            <div class="table-box">

                <table class="table table-hover">

                    <thead class="table-light">
                        <tr>
                            <th>번호</th>
                            <th>아이디</th>
                            <th>이름</th>
                            <th>상태</th>
                            <th>가입일</th>
                        </tr>
                    </thead>

                    <tbody>

                        <tr>
                            <td>1</td>
                            <td>user01</td>
                            <td>홍길동</td>
                            <td>활동</td>
                            <td>2026-06-10</td>
                        </tr>

                        <tr>
                            <td>2</td>
                            <td>user02</td>
                            <td>김철수</td>
                            <td>경고</td>
                            <td>2026-06-09</td>
                        </tr>

                        <tr>
                            <td>3</td>
                            <td>user03</td>
                            <td>이영희</td>
                            <td>정지</td>
                            <td>2026-06-08</td>
                        </tr>

                    </tbody>

                </table>

                <nav>
                    <ul class="pagination justify-content-center">
                        <li class="page-item"><a class="page-link" href="#">1</a></li>
                        <li class="page-item"><a class="page-link" href="#">2</a></li>
                        <li class="page-item"><a class="page-link" href="#">3</a></li>
                    </ul>
                </nav>

            </div>

        </div>

    </div>

</div>

</body>
</html>