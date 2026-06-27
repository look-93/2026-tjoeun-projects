<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../inc/header.jsp"%>

<div class="container my-5">
	<h3>회원가입</h3>
	<form action="${pageContext.request.contextPath}/users/join"
		method="post" onsubmit="return checkForm()">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
		<div class="my-3">
			<label for="nickname" class="form-label">닉네임</label> <input
				type="text" class="form-control" id="nickname" name="nickname" />
		</div>
		<div class="my-3  alert  alert-warning  tnickname">닉네임 중복검사는
			필수입니다.</div>

		<div class="my-3">
			<label for="bpass" class="form-label">비밀번호</label> <input
				type="password" class="form-control" id="bpass" name="bpass" />
		</div>
		<div class="my-3">
			<label for="email" class="form-label">이메일</label> <input type="email"
				class="form-control" id="email" name="email" />
		</div>
		<div class="my-3  alert  alert-warning  target">아이디 중복검사는 필수입니다.
		</div>

		<div class="my-3">
			<label for="mobile" class="form-label">휴대폰</label> <input type="text"
				class="form-control" id="mobile" name="mobile" />
		</div>
		<div class="text-end">
			<button type="reset" class="btn btn-outline-primary">취소</button>
			<button type="submit" class="btn btn-primary">가입하기</button>
		</div>
	</form>

</div>
<script>
	window.addEventListener("load", function(){
		let nickname = document.querySelector("#nickname");
		let tnickname = document.querySelector(".tnickname");

		let email = document.querySelector("#email");
		let target = document.querySelector(".target");
		
	    nickname.addEventListener("keyup", function(e){
	        let value = e.target.value.trim();

	        if(value !== ""){
	            fetch("${pageContext.request.contextPath}/duplicateNickname?nickname=" + encodeURIComponent(value))
	            .then(response => response.json())
	            .then(data => {
	                if(data.exists){
	                    tnickname.textContent = "이미 사용중인 닉네임입니다.";
	                    tnickname.className = "my-3 alert alert-danger tnickname";
	                }else{
	                    tnickname.textContent = "사용가능한 닉네임입니다.";
	                    tnickname.className = "my-3 alert alert-success tnickname";
	                }
	            })
	            .catch(err => {
	                tnickname.textContent = "서버오류입니다.";
	                tnickname.className = "my-3 alert alert-info tnickname";
	            });
	        }else{
	            tnickname.textContent = "닉네임 중복검사는 필수입니다.";
	            tnickname.className = "my-3 alert alert-warning tnickname";
	        }
	    });
	    
	    email.addEventListener("keyup", function(e){
	        let value = e.target.value.trim();

	        if(value !== ""){
	            fetch("${pageContext.request.contextPath}/duplicateEmail?email=" + encodeURIComponent(value))
	            .then(response => response.json())
	            .then(data => {
	                if(data.exists){
	                	target.textContent = "이미 사용중인 이메일입니다.";
	                	target.className = "my-3 alert alert-danger target";
	                }else{
	                	target.textContent = "사용가능한 이메일입니다.";
	                	target.className = "my-3 alert alert-success target";
	                }
	            })
	            .catch(err => {
	            	target.textContent = "서버오류입니다.";
	            	target.className = "my-3 alert alert-info target";
	            });
	        }else{
	        	target.textContent = "이메일 중복검사는 필수입니다.";
	        	target.className = "my-3 alert alert-warning target";
	        }
	    });	    
	});
	
	

</script>


<script>
function checkForm(){
  let nickname = document.getElementById("nickname");
  let bpass = document.getElementById("bpass");
  let email = document.getElementById("email");
  let mobile = document.getElementById("mobile");

  if(nickname.value.trim()==""){ alert("닉네임을 입력하세요"); nickname.focus(); return false; }
  if(bpass.value.trim()==""){ alert("비밀번호를 입력하세요"); bpass.focus(); return false; }
  if(email.value.trim()==""){ alert("이메일을 입력하세요"); email.focus(); return false; }
  if(mobile.value.trim()==""){ alert("휴대폰 번호를 입력하세요"); mobile.focus(); return false; }
  return true;
}
</script>

<%@include file="../inc/footer.jsp"%>
