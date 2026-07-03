<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %> 
<!DOCTYPE html>
<div class="profile-card">
	<div class="myProfile-img"></div>
	<sec:authorize access="isAuthenticated()"> 
    <sec:authentication property="principal.dto" var="loginUser"/>
		<div class="myProfile-info">
			<h2>${loginUser.nickname}님</h2>
			<p></p>
			<br> 
			<span class="badge">
				<c:choose>
		            <c:when test="${loginUser.typeName eq 'ROLE_MEMBER'}">  
		                일반회원
		            </c:when>
		            <c:when test="${loginUser.typeName eq 'ROLE_PARTNER'}">
		                제휴업체
		            </c:when>
		            <c:when test="${loginUser.typeName eq 'ROLE_ADMIN'}">
		                관리자
		            </c:when>
		            <c:otherwise>
		                ${loginUser.typeName}
		            </c:otherwise>
		        </c:choose>
			</span>
		</div>  		
    </sec:authorize>
</div>


