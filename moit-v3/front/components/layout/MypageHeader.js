import React from "react";
import { Avatar, Tag } from "antd";

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

    return (
        <div className="mypage-profile-card">

            {/* 프로필 이미지 */}
            <Avatar
                size={64}
                src={user?.profileUrl || "/images/moit.png"}
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