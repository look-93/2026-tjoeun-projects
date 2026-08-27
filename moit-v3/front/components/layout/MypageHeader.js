import React from "react";
import { Avatar, Tag } from "antd";

function MypageHeader({ user, point = 0 }) {

    console.log("===== MYPAGE HEADER USER =====");
    console.log(user);

    console.log("===== MYPAGE HEADER POINT =====");
    console.log(point);

    const getMemberType = (memberTypeId) => {
        switch (Number(memberTypeId)) {
            case 1:
                return "일반회원";

            case 2:
                return "제휴업체";

            case 3:
                return "관리자";

            case 4:
                return "최고관리자";

            default:
                return "-";
        }
    };

    const getProfileImageUrl = (profileUrl) => {

        if (!profileUrl) {
            return "/images/moit.png";
        }

        if (profileUrl === "/images/moit.png") {
            return "/images/moit.png";
        }

        if (profileUrl.startsWith("http")) {
            return profileUrl;
        }

        return `${process.env.NEXT_PUBLIC_API_BASE_URL}${profileUrl}`;
    };

    return (
        <div className="mypage-profile-card">

            {/* =========================
                프로필 이미지
            ========================= */}
            <div className="mypage-profile-image">
                <Avatar
                    size={72}
                    src={getProfileImageUrl(user?.profileUrl)}
                >
                    {!user?.profileUrl &&
                        user?.nickname?.charAt(0)}
                </Avatar>
            </div>


            {/* =========================
                회원 정보
            ========================= */}
            <div className="mypage-profile-info">

                {/* 닉네임 */}
                <h6>
                    {user?.nickname || "-"}
                </h6>

                {/* 이메일 */}
                <p>
                    {user?.email || "-"}
                </p>

                {/* 회원 유형 */}
                <Tag>
                    {getMemberType(user?.memberTypeId)}
                </Tag>

            </div>


            {/* =========================
                포인트
            ========================= */}
            <div className="mypage-profile-point">

                <span className="mypage-point-label">
                    보유 포인트
                </span>

                <strong>
                    {Number(point || 0).toLocaleString()}
                    <span> P</span>
                </strong>

            </div>

        </div>
    );
}

export default MypageHeader;