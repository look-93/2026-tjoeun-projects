<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../inc/userHeader.jsp" %>


<div class="signup-box">
  <h2>회원가입</h2>
  <form id="joinForm" action="${pageContext.request.contextPath}/users/join" method="post">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" >
    <div class="form-group-inline">
      <input type="text" id="loginId" name="loginId" placeholder="아이디" required>
      <!-- <button type="button" class="btn">중복확인</button> -->
    </div>
    <div id="idCheck" class="target"></div>
    <script>
    	window.addEventListener("load",function(){
    		
    		let loginId = document.getElementById("loginId");
    		let target = document.getElementById("idCheck");
    		
    		loginId.addEventListener("keyup",function(e){
    			
    			let value = e.target.value.trim();
    			
    			if(value==""){
    				target.textContent="아이디를 입력해주세요.";
    				target.className="target text-warning";
    				return;
    			}
    			fetch("${pageContext.request.contextPath}/users/checkLoginId?loginId="+encodeURIComponent(value))
    			.then(response=>response.json())
    			.then(data=>{
    				if(data.exists){
    					target.textContent="이미 사용중인 아이디입니다.";
    					target.className="target text-danger";
    				}
    				else{
    					target.textContent="사용 가능한 아이디입니다.";
    					target.className="target text-success";
    				}
    			})
    			.catch(error=>{
    				target.textContent="서버 오류가 발생했습니다.";
    				target.className="target text-warning"
    			});
    			
    		});
    		
    	});
    </script>
    
    <div class="form-group"><input type="password" id="password" name="password" placeholder="비밀번호" required></div>
    <div class="form-group"><input type="password" id="passwordCheck" placeholder="비밀번호 확인" required></div>
    <div id="passCheck"></div>
    
    <div class="form-group-inline">
      <input type="text" id="nickname" name="nickname" placeholder="닉네임" required>
      <!-- <button type="button" class="btn">중복확인</button> -->
    </div>
    <div id="nickCheck" class="target"></div>
    <script>
    	window.addEventListener("load",function(){
    		
    		let nickname = document.getElementById("nickname");
    		let target = document.getElementById("nickCheck");
    		
    		nickname.addEventListener("keyup",function(e){
    			
    			let value = e.target.value.trim();
    			
    			if(value==""){
    				target.textContent="닉네임을 입력해주세요.";
    				target.className="target text-warning";
    				return;
    			}
    			fetch("${pageContext.request.contextPath}/users/checkNickname?nickname="+encodeURIComponent(value))
    			.then(response=>response.json())
    			.then(data=>{
    				if(data.exists){
    					target.textContent="이미 사용중인 닉네임입니다.";
    					target.className="target text-danger";
    				}
    				else{
    					target.textContent="사용 가능한 닉네임입니다.";
    					target.className="target text-success";
    				}
    			})
    			.catch(error=>{
    				target.textContent="서버 오류가 발생했습니다.";
    				target.className="target text-warning"
    			});
    			
    		});
    		
    	});
    </script>
    
    <div class="form-group"><input type="email" id="email" name="email" placeholder="이메일" required></div>
    <div class="form-group">
      <input type="tel" id="mobile" name="mobile" placeholder="전화번호" required>
      <div class="hint">'-'없이 숫자만 입력해주세요</div>
    </div>

    <!-- 회원가입 유형 선택 -->
    <div class="form-options member-type">
      <label><input type="radio" name="memberTypeId" value="1" checked> 일반회원</label>
      <label><input type="radio" name="memberTypeId" value="2"> 제휴업체</label>
    </div>

    <!-- 약관 동의 -->
    <div class="form-options">
      <label><input type="checkbox" id="agree" required> 약관 및 개인정보처리방침 동의</label>
    </div>

    <button type="submit" class="btn btn-primary">회원가입</button>
  </form>
</div>


<script>
function signupComplete(){
  alert("회원가입이 완료되었습니다! 로그인 페이지로 이동합니다.");
  window.location.href = "login.html"; // 로그인 페이지로 이동
  return false;
}
</script>
<%@include file="../inc/userFooter.jsp" %>
