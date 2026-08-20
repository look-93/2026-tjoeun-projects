import React, { useState, useEffect } from "react";
import { Button, Card, Space, Table, Tag, Typography } from "antd";
import {
    FileTextOutlined,
    TeamOutlined,
    StarOutlined,
    HeartOutlined,
} from "@ant-design/icons";
import { useSelector, useDispatch } from "react-redux";
import { useRouter } from "next/router";
import {
    fetchMyMeetupsRequest,
    fetchMeetupApplicantsRequest,
    updateApplicationStatusRequest,
} from "../../../reducers/meetupReducer";
import MeetupApplicantModal from "../../../components/MeetupApplicantModal";
import MyPageStatCard from "../../../components/MyPageStatCard";

// http://localhost:3000/user/mypage/meetup

const { Title, Text } = Typography;

function UserMyMeetupPage() {
    const router = useRouter();
    const dispatch = useDispatch();

    //신청자관리모달
    const [applicantModalOpen, setApplicantModalOpen] = useState(false);
    const [selectedMeetupId, setSelectedMeetupId] = useState(null);

    const { myMeetups, meetupApplicants, loading } = useSelector(
        (state) => state.meetup,
    );

    const handleApprove = (applicationId) => {
        console.log("신청자 승인:", applicationId);

        dispatch(
            updateApplicationStatusRequest({
                applicationId,
                applyStatus: "APPROVED",
            }),
        );
    };

    const handleReject = ({ applicationId, rejectReason }) => {
        console.log("신청자 거절:", applicationId);
        console.log("거절 사유:", rejectReason);

        // TODO: 거절 Saga 연결
        // dispatch(
        //     rejectMeetupApplicantRequest({
        //         applicationId,
        //         rejectReason,
        //     }),
        // );
    };

    useEffect(() => {
        dispatch(
            fetchMyMeetupsRequest({
                page: 0,
                size: 10,
            }),
        );
    }, [dispatch]);

    const handleApplicantManage = (meetupId) => {
        setSelectedMeetupId(meetupId);
        setApplicantModalOpen(true);
        dispatch(
            fetchMeetupApplicantsRequest({
                meetupId,
                page: 0,
                size: 10,
            }),
        );
    };

    // 통계
    const stats = [
        {
            title: "내 모집글",
            value: 12,
            suffix: "개",
            icon: FileTextOutlined,
        },
        {
            title: "신청 모임",
            value: 8,
            suffix: "개",
            icon: TeamOutlined,
        },
        {
            title: "작성 후기",
            value: 16,
            suffix: "개",
            icon: StarOutlined,
        },
        {
            title: "관심 모임",
            value: 6,
            suffix: "개",
            icon: HeartOutlined,
        },
    ];

    // 테이블
    const columns = [
        {
            title: "번호",
            key: "number",
            align: "center",
            render: (_, record, index) => index + 1,
        },
        {
            title: "모임명",
            dataIndex: "title",
            key: "title",
            render: (title) => <Text strong>{title}</Text>,
        },
        {
            title: "모임일",
            dataIndex: "meetupAt",
            key: "meetupAt",
            render: (meetupAt) =>
                meetupAt ? meetupAt.replace("T", " ").slice(0, 16) : "-",
        },
        {
            title: "신청 인원",
            dataIndex: "totalParticipants",
            key: "totalParticipants",
            align: "center",
            render: (totalParticipants) => `${totalParticipants ?? 0}명`,
        },
        {
            title: "작성일",
            dataIndex: "createdAt",
            key: "createdAt",
            render: (createdAt) =>
                createdAt ? createdAt.replace("T", " ").slice(0, 16) : "-",
        },
        {
            title: "상태",
            dataIndex: "meetupStatus",
            key: "meetupStatus",
            align: "center",
            render: (meetupStatus) => {
                if (meetupStatus === "COMPLETED") {
                    return <Tag>종료</Tag>;
                }

                if (meetupStatus === "WEATHER_CANCELED") {
                    return <Tag color="error">기상악화 취소</Tag>;
                }

                if (meetupStatus === "CANCELED") {
                    return <Tag color="error">취소</Tag>;
                }

                return <Tag color="processing">진행중</Tag>;
            },
        },
        {
            title: "관리",
            key: "manage",
            align: "center",
            render: (_, record) => (
                <Space>
                    <Button
                        size="small"
                        onClick={() =>
                            router.push(
                                `/user/meetup/write?meetupId=${record.id}`,
                            )
                        }
                    >
                        수정
                    </Button>

                    <Button
                        size="small"
                        onClick={() => handleApplicantManage(record.id)}
                    >
                        신청자 관리
                    </Button>
                </Space>
            ),
        },
    ];

    return (
        <div className="mypage-container">
            {/* 통계 */}
            <MyPageStatCard stats={stats} />

            {/* 신청한 모임 */}
            <Card className="mypage-meetup-section" style={{ marginTop: 20 }}>
                <Title level={3}>개설한 모임</Title>

                <Table
                    columns={columns}
                    dataSource={myMeetups}
                    pagination={{
                        pageSize: 10,
                        showSizeChanger: false,
                    }}
                    scroll={{ x: 600 }}
                />
            </Card>
            <MeetupApplicantModal
                open={applicantModalOpen}
                onCancel={() => setApplicantModalOpen(false)}
                loading={loading}
                meetupApplicants={meetupApplicants}
                onApprove={handleApprove}
                onReject={handleReject}
            />
        </div>
    );
}

export default UserMyMeetupPage;
