# 🚀 MOIT (모잇)

## 📌 프로젝트 소개

**MOIT(모잇)**는 스터디, 프로젝트, 운동, 취미 활동 등 **공통의 관심사와 목표를 가진 사람들이 모임을 만들고 참여할 수 있는 목적형 커뮤니티 플랫폼**입니다.

1차 프로젝트에서는 기본적인 소모임 플랫폼을 구축하였으며, 2차 프로젝트에서는 **Spring Boot 기반으로 리팩토링하고 다양한 Open API와 AI 기능을 도입하여 서비스 품질과 사용자 경험을 고도화**하였습니다.

---

# 🎯 프로젝트 목표

* 목적 기반 소모임 커뮤니티 서비스 구축
* 안전하고 신뢰할 수 있는 모임 환경 제공
* AI 및 Open API를 활용한 사용자 편의성 향상
* 유지보수성과 확장성을 고려한 Spring Boot 기반 리팩토링

---

# 📅 프로젝트 개요

| 항목    | 내용                        |
| ----- | ------------------------- |
| 프로젝트명 | MOIT (모잇)                 |
| 1차 개발 | 2026.06.16 ~ 2026.06.22   |
| 2차 개발 | 2026.07.02 ~ 2026.07.14 Spring Boot 리팩토링 및 기능 고도화 |
| 개발 형태 | 팀 프로젝트                    |

---

# 🔄 리팩토링 및 기술 변경

### Framework

* Spring Framework → **Spring Boot**

### Database

* MySQL → **Oracle**

### View

* JSP → **Thymeleaf**

### Security

* Spring Security 적용
* OAuth2 기반 소셜 로그인 추가
* BCrypt 비밀번호 암호화 적용

---

# ✨ 주요 기능

## 👤 회원

### 1차

* 회원가입
* 로그인

### 2차 고도화

* OAuth2 기반 소셜 로그인
* 관심사 태그 등록
* BCrypt 비밀번호 암호화
* HIBP(Have I Been Pwned) API를 활용한 비밀번호 유출 여부 검사

---

## 🤝 모임

### 1차

* 모임 등록
* 모임 조회
* 수정 / 삭제
* 모임 신청

### 2차 고도화

* OpenAI GPT API를 활용한

  * 모임 제목 자동 추천
  * 카테고리 자동 추천
  * 소개글 자동 작성
* 참가자 신뢰도 AI 평가
* 기상청 단기예보 API를 활용한 모임 날씨 알림
* VWorld 주소 검색 API 기반 주소 검색
* 네이버 MAP API를 활용한 지도 시각화

---

## 📝 후기

### 1차

* 후기 작성
* 조회
* 수정
* 삭제
* 좋아요

### 2차 고도화

* OpenAI GPT API 기반 욕설 및 비방 필터링
* OpenAI GPT API 기반 개설자 후기 분석 서비스

---

## 📨 문의

### 1차 신규 기능
* 문의 작성, 답변
* 조회
* 수정
* 삭제

### 2차 신규 기능

* OpenAI GPT API 기반 문의 비속어 필터링
* 답변 등록 시 비동기 이벤트 기반 알림 발송

---

## 🚨 신고

### 1차

* 모집글 신고
* 후기 신고
* 관리자 신고 처리

### 2차 고도화

* OpenAI GPT API 기반 신고 사유 문장 생성
* 중복 신고 방지
* SMTP 기반 신고 처리 결과 메일 자동 발송

---

## 📢 광고

### 1차

* 광고 등록
* 수정
* 삭제
* 상태 관리

### 2차 고도화

* OpenAI GPT API 기반 광고 제목 및 내용 자동 생성
* 광고 게시 상태 자동 관리 Scheduler
* SMTP 기반 광고 종료 예약 메일 발송

---

# 💡 프로젝트 특징

* Spring Boot 기반 리팩토링을 통한 유지보수성 향상
* Oracle 및 Thymeleaf 기반 서버 사이드 렌더링 적용
* OAuth2 및 Spring Security를 활용한 보안 강화
* OpenAI GPT API를 활용한 AI 추천 및 콘텐츠 생성
* 기상청, VWorld, 네이버 MAP 등 다양한 Open API 연동
* SMTP 및 비동기 이벤트를 활용한 사용자 알림 자동화
* Scheduler를 통한 광고 상태 자동 관리
* AI 기반 콘텐츠 필터링으로 안전한 커뮤니티 환경 제공

---

# 🛠 기술 스택

### Front-End

* HTML5
* CSS3
* JavaScript
* Thymeleaf

### Back-End

* Java
* Spring Boot
* Spring Security
* OAuth2
* MyBatis

### Database

* Oracle

### AI & Open API

* OpenAI GPT API
* Have I Been Pwned API
* 기상청 단기예보 API
* VWorld 주소 검색 API
* 네이버 MAP API
* SMTP Mail

### DevOps & Collaboration

* Git
* GitHub
* Notion

---

# 👥 Team

* GitHub Flow 기반 협업
* Notion을 활용한 일정 및 업무 관리
* 코드 리뷰를 통한 협업 진행

---

# 🎥 프로젝트 시연

* 회원가입 및 로그인

  * https://www.youtube.com/watch?v=jrTrxnriqys

* 모임 등록 및 신청

  * https://www.youtube.com/watch?v=WLSxFhWPIRs

* 문의

  * https://www.youtube.com/watch?v=eWmBrzBqTeU

* 후기

  * https://www.youtube.com/watch?v=vFFOV-ELUPY

* 신고

  * https://www.youtube.com/watch?v=BbsZr3dRHZ0

* 광고

  * https://www.youtube.com/watch?v=iv0MOgaqSUI

---

## 📢 MOIT

**Meet + It = MOIT**

같은 관심사와 목표를 가진 사람들이 연결되어 함께 성장할 수 있도록 지원하는 목적형 커뮤니티 플랫폼입니다.

1차 프로젝트에서 기본 기능을 구현한 후, 2차 프로젝트에서는 **Spring Boot 리팩토링과 AI(OpenAI GPT API), OAuth2, 기상청 API, VWorld API, 네이버 MAP API 등 다양한 Open API를 적용하여 서비스의 완성도와 사용자 경험을 크게 향상**시켰습니다.
