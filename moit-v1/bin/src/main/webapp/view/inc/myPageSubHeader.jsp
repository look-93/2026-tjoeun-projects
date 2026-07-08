<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<div class="stats">
	<div class="stat-card">
		🎉
		<h3>${meetupStats.meetupCount}</h3>
		<p>작성한 모집글</p>
	</div>
	<div class="stat-card">
		⭐
		<h3>${meetupStats.reviewCount}</h3>
		<p>작성 후기</p>
	</div>
	<div class="stat-card">
		❤️
		<h3>${meetupStats.likeCount}</h3>
		<p>관심 모임</p>
	</div>
	<div class="stat-card">
		📝
		<h3>${meetupStats.applicationCount}</h3>
		<p>참여 기록</p>
	</div>
</div>