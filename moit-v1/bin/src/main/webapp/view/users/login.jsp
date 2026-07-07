<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../inc/userHeader.jsp" %>

<div class="container">
  <section class="auth-box">
    <div class="tab-menu">
      <button class="tab-btn active" data-tab="general">일반회원 로그인</button>
      <button class="tab-btn" data-tab="admin">관리자 로그인</button>
    </div>

    <!-- 일반회원 로그인 -->
    <div class="tab-content active" id="general">
    <form action="${pageContext.request.contextPath}/login" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" >   
    <input type="hidden" name="loginType" value="USER">
        <!-- 회원 유형 선택 (일반회원 / 제휴업체) -->
        <div class="form-options member-type">
        <label><input type="radio" name="memberTypeId" value="1" checked> 일반회원</label>
        <label><input type="radio" name="memberTypeId" value="2"> 제휴업체</label>
        </div>

        <div class="form-group"><input type="text" name="username" placeholder="아이디"></div>
        <div class="form-group"><input type="password" name="password" placeholder="비밀번호"></div>
        <div class="form-options"><label><input type="checkbox" name="rememberId"> 아이디 저장</label></div>
        <button type="submit" class="btn btn-primary full">로그인</button>

        <div class="auth-links">
        <div class="link-row">
            <a href="#">아이디 찾기</a> |  
            <a href="#">비밀번호 찾기</a>
        </div>
        <hr>
        <div class="link-row">
            <span>계정이 없으신가요?</span>
            <a href="signup-general.html">회원가입</a>
        </div>
        </div>
    </form>
    </div>


    <!-- 관리자 로그인 -->
    <div class="tab-content" id="admin">
      <form action="${pageContext.request.contextPath}/login" method="post">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" >
      <input type="hidden" name="loginType" value="ADMIN">
        <div class="form-group"><input type="text" name="username" placeholder="관리자 아이디"></div>
        <div class="form-group"><input type="password" name="password" placeholder="비밀번호"></div>
        <div class="form-options"><label><input type="checkbox" name="rememberId"> 아이디 저장</label></div>
        <button type="submit" class="btn btn-primary full">관리자 로그인</button>
        <div class="auth-links">
            <div class="link-row">
                <a href="#">아이디 찾기</a>  |  
                <a href="#">비밀번호 찾기</a>
            </div>
          <hr>
            <div class="link-row">
                <span>계정이 없으신가요?</span>
                <a href="signup-admin.html">회원가입</a>
            </div>
        </div>
      </form>
    </div>
  </section>
</div>

<script>
const tabBtns=document.querySelectorAll(".tab-btn");
const tabContents=document.querySelectorAll(".tab-content");
tabBtns.forEach(btn=>{
  btn.addEventListener("click",()=>{
    tabBtns.forEach(b=>b.classList.remove("active"));
    tabContents.forEach(c=>c.classList.remove("active"));
    btn.classList.add("active");
    document.getElementById(btn.dataset.tab).classList.add("active");
  });
});
</script>

<%@include file="../inc/userFooter.jsp" %>