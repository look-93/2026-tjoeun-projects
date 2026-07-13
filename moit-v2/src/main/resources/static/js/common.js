function getCsrfHeaders() {
    const token = document.querySelector('meta[name="_csrf"]').content;
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    return {
        "Content-Type": "application/json",
        [header]: token
    };
}

function gotoDetailOrLogin(meetupId) {
    const isLogin = document.getElementById("isLogin").value === "true";

    if (!isLogin) {
        alert("로그인이 필요한 서비스입니다.");
        location.href = "/user/member/login";
        return;
    }

    location.href = "/meetup/detail?meetupId=" + meetupId;
}