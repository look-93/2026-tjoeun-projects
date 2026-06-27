<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<aside class="sidebar">
	<a href="#">내 정보</a> 
	<a href="${pageContext.request.contextPath}/meetup/mypage/meetup.do" class="<c:if test='${menu eq \"meetup\"}'>active</c:if>">내 모집글</a> 
	<a href="${pageContext.request.contextPath}/meetup/mypage/apply.do" class="<c:if test='${menu eq \"meetupApply\"}'>active</c:if>">신청한 모임</a> 
	<!-- <a href="#">관심 모임</a>  -->
	<a href="${pageContext.request.contextPath}/review/mypageReview" class="<c:if test='${menu eq \"review\"}'>active</c:if>">작성한 후기</a> 
	<a href="${pageContext.request.contextPath}/report/user/myPageMyReportList.do" class="<c:if test='${menu eq \"myReport\"}'>active</c:if>">내 신고내역</a> 
	<a href="#">회원정보 수정</a>
</aside>

