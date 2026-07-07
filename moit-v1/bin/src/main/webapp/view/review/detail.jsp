<!DOCTYPE html>
<html lang="ko">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MOIT | 모집글 상세</title>

<style>

:root{
    --c1:#B6FFFA;
    --c2:#98E4FF;
    --c3:#80B3FF;
    --c4:#687EFF;

    --bg:#f7faff;
    --white:#fff;
    --text:#222;
    --gray:#777;

    --shadow:0 5px 15px rgba(0,0,0,.07);
    --radius:20px;
}

*{
    margin:0;
    padding:0;
    box-sizing:border-box;
}

body{
    background:var(--bg);
    font-family:sans-serif;
}

/* container */
.container{
    width:1300px;
    max-width:95%;
    margin:auto;
}

/* ================= HEADER (요청 버전) ================= */

header{
    background:white;
    height:80px;
    box-shadow:var(--shadow);
}

.header-inner{
    height:100%;
    display:flex;
    justify-content:space-between;
    align-items:center;
}

.logo{
    font-size:30px;
    font-weight:bold;
    color:var(--c4);
}

nav{
    display:flex;
    gap:30px;
}

nav a{
    text-decoration:none;
    color:#333;
}

.search-top{
    width:250px;
    padding:12px;
    border-radius:30px;
    border:1px solid #ddd;
}

.user-box{
    display:flex;
    align-items:center;
    gap:20px;
}

.alarm{
    font-size:22px;
}

.profile{
    display:flex;
    align-items:center;
    gap:12px;
    padding:8px 15px;
    border-radius:30px;
    border:1px solid #eee;
    background:white;
}

.profile-img{
    width:40px;
    height:40px;
    border-radius:50%;
    background:var(--c4);
    color:white;
    display:flex;
    align-items:center;
    justify-content:center;
    font-weight:bold;
}

.profile-info{
    display:flex;
    flex-direction:column;
}

.profile-info strong{font-size:14px;}
.profile-info span{font-size:12px;color:var(--gray);}

/* ================= LAYOUT ================= */

.detail-wrap{
    margin-top:30px;
    display:grid;
    grid-template-columns:2fr 1fr;
    gap:25px;
}

.left-panel{
    display:flex;
    flex-direction:column;
    gap:25px;
}

/* IMAGE */
.image-box{
    background:white;
    padding:20px;
    border-radius:20px;
    box-shadow:var(--shadow);
}

.main-image{
    height:450px;
    border-radius:20px;
    overflow:hidden;
    background:white;
}

.thumb-list{
    display:flex;
    gap:10px;
    margin-top:15px;
}

.thumb{
    flex:1;
    height:80px;
    border-radius:10px;
    overflow:hidden;
}

/* CONTENT */
.content-box{
    background:white;
    padding:30px;
    border-radius:20px;
    box-shadow:var(--shadow);
}

.badge{
    display:inline-block;
    padding:7px 15px;
    background:#dfffe4;
    color:#009933;
    border-radius:50px;
    margin-bottom:15px;
}

.title{
    font-size:32px;
    margin-bottom:20px;
}

.tags{
    display:flex;
    gap:10px;
    flex-wrap:wrap;
    margin-bottom:20px;
}

.tag{
    background:#eef7ff;
    color:var(--c4);
    padding:8px 12px;
    border-radius:50px;
}

/* SIDE */
.side-box{
    background:white;
    padding:25px;
    border-radius:20px;
    box-shadow:var(--shadow);
    margin-bottom:20px;
}

.info-row{
    display:flex;
    justify-content:space-between;
    margin-bottom:15px;
}

.btn{
    width:100%;
    border:none;
    padding:15px;
    border-radius:12px;
    cursor:pointer;
}

.btn-primary{
    background:var(--c4);
    color:white;
}

.btn-outline{
    margin-top:10px;
    background:white;
    border:2px solid var(--c4);
    color:var(--c4);
}

/* ================= TABS (복구 완료) ================= */

.tabs{
    background:white;
    border-radius:20px;
    box-shadow:var(--shadow);
    overflow:hidden;
}

.tab-header{
    display:flex;
}

.tab-btn{
    flex:1;
    padding:20px;
    text-align:center;
    cursor:pointer;
    border-bottom:3px solid transparent;
}

.tab-btn.active{
    border-color:var(--c4);
    color:var(--c4);
    font-weight:bold;
}

.tab-content{padding:25px;}

.tab-panel{display:none;}
.tab-panel.active{display:block}

/* REVIEW */
.rating-box{
    display:flex;
    gap:40px;
    margin-bottom:20px;
}

.rating-score h1{
    font-size:60px;
    color:var(--c4);
}

.stars{color:gold}

.bar-line{
    width:200px;
    height:8px;
    background:#eee;
    border-radius:10px;
    overflow:hidden;
}

.bar-fill{
    height:100%;
    background:orange;
}

/* COMMENT */
.comment{
    padding:15px 0;
    border-bottom:1px solid #eee;
}

.comment-images{
    display:flex;
    gap:10px;
    margin-top:10px;
}

.comment-images img{
    width:100px;
    height:100px;
    object-fit:cover;
    border-radius:10px;
}

/* 추천모임 */
.recommend-card{
    display:flex;
    gap:10px;
    margin-bottom:12px;
}

.recommend-card img{
    width:60px;
    height:60px;
    border-radius:10px;
    object-fit:cover;
}

.recommend-title{
    font-size:12px;
    font-weight:bold;
}

.recommend-tag{
    font-size:10px;
    color:#666;
}

.report-btn{
    border:none;
    background:#ffeded;
    color:#e53935;
    padding:6px 12px;
    border-radius:8px;
    cursor:pointer;
    font-size:12px;
    font-weight:600;
}

.report-btn:hover{
    background:#ffd6d6;
}

.content-top{
    display:flex;
    justify-content:space-between;
    align-items:flex-start;
    margin-bottom:15px;
}

.comment-top{
    display:flex;
    justify-content:space-between;
    align-items:center;
    margin-bottom:5px;
}

.inquiry-btn{
    width:100%;
    margin-top:12px;
    border:none;
    background:var(--c2);
    color:#222;
    padding:12px;
    border-radius:10px;
    cursor:pointer;
    font-weight:600;
}

/* SIDEBAR AD */

.sidebar-ad{
    margin-top:25px;
    min-height:450px;

    background:linear-gradient(
        180deg,
        var(--c4),
        var(--c3)
    );

    border-radius:20px;
    padding:25px;

    display:flex;
    flex-direction:column;
    justify-content:center;
    align-items:center;

    text-align:center;
    color:white;

    box-shadow:var(--shadow);
}

.sidebar-ad .ad-tag{
    background:rgba(255,255,255,.2);
    padding:5px 12px;
    border-radius:20px;
    margin-bottom:15px;
    font-size:12px;
}

.sidebar-ad h3{
    font-size:24px;
    margin-bottom:15px;
    line-height:1.4;
}

.sidebar-ad p{
    margin-bottom:20px;
    line-height:1.6;
}

.sidebar-ad a{
    background:white;
    color:var(--c4);

    text-decoration:none;

    padding:12px 20px;
    border-radius:12px;

    font-weight:bold;
}

</style>
</head>

<body>

<header>
<div class="container header-inner">

    <div class="logo">MOIT</div>

    <nav>
        <a href="#">홈</a>
        <a href="#">모집찾기</a>
        <a href="#">커뮤니티</a>
        <a href="#">공지사항</a>
    </nav>

    <input class="search-top" placeholder="검색">

    <div class="user-box">
        <div class="alarm">🔔</div>

        <div class="profile">
            <div class="profile-img">J</div>
            <div class="profile-info">
                <strong>예진님</strong>
                <span>일반회원</span>
            </div>
        </div>
    </div>

</div>
</header>

<div class="container">

<div class="detail-wrap">

<!-- LEFT -->
<div class="left-panel">

    <div class="image-box">

        <div class="main-image">
            <img src="https://images.unsplash.com/photo-1501555088652-021faa106b9b?auto=format&fit=crop&w=900&q=80"
                 style="width:100%;height:100%;object-fit:cover;">
        </div>

        <div class="thumb-list">
            <div class="thumb"><img src="https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=150&q=80" style="width:100%;height:100%;object-fit:cover;"></div>
            <div class="thumb"><img src="https://images.unsplash.com/photo-1533240332313-0db49b459ad6?auto=format&fit=crop&w=150&q=80" style="width:100%;height:100%;object-fit:cover;"></div>
            <div class="thumb"><img src="https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=150&q=80" style="width:100%;height:100%;object-fit:cover;"></div>
            <div class="thumb"><img src="https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=150&q=80" style="width:100%;height:100%;object-fit:cover;"></div>
        </div>

    </div>

    <div class="content-box">
        <div class="content-top">
            <div class="badge">모집중</div>
            <button class="report-btn">🚨 모임 신고</button>
        </div>
        <h1 class="title">등산 좋아하는 사람 모여라!</h1>

        <div class="tags">
            <div class="tag">운동</div>
            <div class="tag">등산</div>
            <div class="tag">서울</div>
        </div>

        <div class="description">
            주말마다 함께 등산할 분들을 모집합니다.
        </div>
    </div>

    <!-- ✅ TABS 복구 -->
    <div class="tabs">

        <div class="tab-header">
            <div class="tab-btn active" onclick="showTab(0)">상세정보</div>
            <div class="tab-btn" onclick="showTab(1)">신청자</div>
            <div class="tab-btn" onclick="showTab(2)">후기</div>
            <div class="tab-btn" onclick="showTab(3)">Q&A</div>
        </div>

        <div class="tab-content">

            <div class="tab-panel active">
                <h3>모임 안내</h3>
                <p>✔ 토요일 9시</p>
            </div>

            <div class="tab-panel">
                <p>홍길동 외 7명</p>
            </div>
            
            
<!-- 내부분 -->
            <div class="tab-panel">

                <div class="rating-box">
                    <div class="rating-score">
                        <h1>4.8</h1>
                        <div class="stars">★★★★★</div>
                        <p>총 후기 12개</p>
                    </div>

                    <div>
                        <div class="bar">
                            <span>5점</span>
                            <div class="bar-line">
                                <div class="bar-fill" style="width:80%"></div>
                            </div>
                            <span>10</span>
                        </div>

                        <div class="bar">
                            <span>4점</span>
                            <div class="bar-line">
                                <div class="bar-fill" style="width:20%"></div>
                            </div>
                            <span>2</span>
                        </div>

                        <div class="bar">
                            <span>3점</span>
                            <div class="bar-line">
                                <div class="bar-fill" style="width:0%"></div>
                            </div>
                            <span>0</span>
                        </div>
                    </div>
                </div>

                <!-- COMMENTS -->
                <div class="comment">
                    <div class="comment-top">
                        <div>
                            <div class="comment-name">김모잇</div>
                            <div class="comment-date">2026.06.15</div>
                        </div>

                        <button class="report-btn">신고</button>
                    </div>

                    <div class="comment-images">
                        <img src="https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=150&q=80">
                        <img src="https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=150&q=80">
                        <img src="https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?auto=format&fit=crop&w=150&q=80">
                    </div>

                    <p>분위기 너무 좋았어요!</p>
                </div>

                <div class="comment">
                    <div class="comment-top">
                        <div>
                            <div class="comment-name">이모임</div>
                            <div class="comment-date">2026.06.12</div>
                        </div>

                        <button class="report-btn">신고</button>
                    </div>
                    <p>초보도 부담 없이 참여 가능</p>
                </div>

            </div>

            <div class="tab-panel">

                <h3>Q&A</h3>

                <div class="comment">
                    <div class="comment-name">익명</div>
                    <div class="comment-date">2026.06.18</div>
                    <p>초보인데 참여 가능할까요?</p>
                </div>

                <div class="comment">
                    <div class="comment-name">작성자</div>
                    <div class="comment-date">2026.06.18</div>
                    <p>네 초보도 환영입니다!</p>
                </div>

            </div>

        </div>
<!-- 내부분 -->


    </div>

</div>

<!-- RIGHT -->
<div>

    <div class="side-box">
        <h3>모집 정보</h3>
        <div class="info-row"><span>인원</span><span>8/10</span></div>
        <div class="info-row"><span>지역</span><span>서울</span></div>

        <button class="btn btn-primary">신청하기</button>
        <button class="btn btn-outline">♡ 관심</button>
    </div>

    <div class="side-box">
        <h3>작성자</h3>
        <p>홍길동</p>

        <button class="inquiry-btn">
            💬 모임글 문의하기
        </button>
    </div>

    <!-- ✅ 추천모임 복구 -->
    <div class="side-box">
        <h3>추천 모임</h3>

        <div class="recommend-card">
            <img src="https://images.unsplash.com/photo-1501555088652-021faa106b9b">
            <div>
                <div class="recommend-title">북한산 등산</div>
                <div class="recommend-tag">서울</div>
            </div>
        </div>

        <div class="recommend-card">
            <img src="https://images.unsplash.com/photo-1533240332313-0db49b459ad6">
            <div>
                <div class="recommend-title">트레킹</div>
                <div class="recommend-tag">경기</div>
            </div>
        </div>

    </div>

    <div class="sidebar-ad">

        <div class="ad-tag">
            ADVERTISEMENT
        </div>

        <h3>
            프리미엄<br>
            모임 홍보
        </h3>

        <p>
            내 모임을 메인 화면에<br>
            노출해 보세요
        </p>

        <a href="#">
            광고 신청
        </a>

    </div>

</div>

</div>

</div>

<script>
function showTab(i){
    const tabs=document.querySelectorAll(".tab-btn");
    const panels=document.querySelectorAll(".tab-panel");

    tabs.forEach(t=>t.classList.remove("active"));
    panels.forEach(p=>p.classList.remove("active"));

    tabs[i].classList.add("active");
    panels[i].classList.add("active");
}
</script>

</body>
</html>