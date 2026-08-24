import React from "react";
import { Avatar, Tag } from "antd";
import api from '../../api/axios';

function MypageHeader({ user }) {

    console.log("===== MYPAGE HEADER USER =====");
    console.log(user);

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

        // 기본 프로필 이미지
        if (profileUrl === "/images/moit.png") {
            return "/images/moit.png";
        }

        // 이미 전체 URL이면 그대로 사용
        if (profileUrl.startsWith("http")) {
            return profileUrl;
        }

        // Spring Boot에서 제공하는 업로드 이미지
        return `${process.env.NEXT_PUBLIC_API_BASE_URL}${profileUrl}`;
    };

    return (
        <div className="mypage-profile-card">

            {/* 프로필 이미지 */}
            <Avatar
                size={64}
                src={getProfileImageUrl(user?.profileUrl)}
            >
                {!user?.profileUrl &&
                    user?.nickname?.charAt(0)}
            </Avatar>

            {/* 회원 정보 */}
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

        </div>
    );
}

export default MypageHeader;