<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MOIT 관리자 회원가입</title>
<link href="https://fonts.googleapis.com/css2?family=Pretendard:wght@400;500;600;700&display=swap" rel="stylesheet">
<style>
:root{
    --c1:#B6FFFA;
    --c2:#98E4FF;
    --c3:#80B3FF;
    --c4:#687EFF;

    --white:#fff;
    --text:#222;
    --gray:#666;
    --bg:#f7faff;

    --shadow:0 5px 20px rgba(0,0,0,.07);
    --radius:20px;
}

*{margin:0;padding:0;box-sizing:border-box;font-family:'Pretendard',sans-serif;}
body{background:var(--bg);}
a{text-decoration:none;color:inherit;}
.container{width:1200px;max-width:95%;margin:auto;}

header{background:white;height:80px;box-shadow:var(--shadow);}
.header-inner{height:100%;display:flex;justify-content:space-between;align-items:center;}
.logo{font-size:32px;font-weight:700;color:var(--c4);}
nav{display:flex;gap:35px;}
nav a{font-weight:600;}
.header-right{display:flex;gap:10px;align-items:center;}
.search{width:250px;padding:12px;border-radius:50px;border:1px solid #ddd;}

/* 회원가입 박스 */
.signup-box{max-width:600px;margin:60px auto;background:white;padding:40px;border-radius:var(--radius);box-shadow:var(--shadow);}
h2{text-align:center;color:var(--c4);margin-bottom:30px;}
.form-group{margin-bottom:20px;}
.form-group input{width:100%;padding:14px;border:1px solid #ddd;border-radius:10px;font-size:15px;}
.form-group-inline{display:flex;gap:10px;margin-bottom:20px;}
.form-group-inline input{flex:1;padding:14px;border:1px solid #ddd;border-radius:10px;}
.btn{border:none;padding:14px 28px;border-radius:50px;cursor:pointer;font-size:16px;}
.btn-primary{background:var(--c4);color:white;width:100%;margin-top:20px;}
.form-options{margin:20px 0;}
.hint{color:#999;font-size:13px;margin-top:6px;}
footer{margin-top:50px;background:white;padding:30px;text-align:center;}
</style>
</head>
<body>

<header>
  <div class="container header-inner">
    <div class="logo">MOIT</div>
    <nav>
      <a href="#">홈</a>
      <a href="#">모임찾기</a>
      <a href="#">행사</a>
      <a href="#">커뮤니티</a>
      <a href="#">공지사항</a>
    </nav>
    <div class="header-right">
      <input class="search" placeholder="모임을 검색하세요"> 🔔 👤
    </div>
  </div>
</header>

<div class="signup-box">
  <h2>관리자 회원가입</h2>
  <form onsubmit="return signupComplete()">
    <div class="form-group-inline">
      <input type="text" placeholder="아이디" required>
      <button type="button" class="btn">중복확인</button>
    </div>
    <div class="form-group"><input type="password" placeholder="비밀번호" required></div>
    <div class="form-group"><input type="password" placeholder="비밀번호 확인" required></div>
    <div class="form-group-inline">
      <input type="text" placeholder="닉네임" required>
      <button type="button" class="btn">중복확인</button>
    </div>
    <div class="form-group"><input type="email" placeholder="이메일" required></div>
    <div class="form-group">
      <input type="tel" placeholder="전화번호" required>
      <div class="hint">'-'없이 숫자만 입력해주세요</div>
    </div>

    <!-- 약관 동의 -->
    <div class="form-options">
      <label><input type="checkbox" required> 약관 및 개인정보처리방침 동의</label>
    </div>

    <button type="submit" class="btn btn-primary">회원가입</button>
  </form>
</div>

<footer>© 2026 MOIT</footer>

<script>
function signupComplete(){
  alert("관리자 회원가입이 완료되었습니다! 로그인 페이지로 이동합니다.");
  window.location.href = "login.html"; // 로그인 페이지로 이동
  return false;
}
</script>

</body>
</html>
