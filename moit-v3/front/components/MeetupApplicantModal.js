import React, { useState } from "react";
import {
    Button,
    Input,
    message,
    Modal,
    Space,
    Table,
    Tag,
    Typography,
} from "antd";

const { Text } = Typography;
function MeetupApplicantModal({
    open,
    onCancel,
    loading,
    meetupApplicants,
    onApprove,
    onReject,
}) {
    const [rejectModalOpen, setRejectModalOpen] = useState(false);
    const [selectedApplicantId, setSelectedApplicantId] = useState(null);
    const [rejectReason, setRejectReason] = useState("");
    console.log(meetupApplicants);
    // 거절 버튼 클릭
    const handleRejectClick = (applicationId) => {
        setSelectedApplicantId(applicationId);
        setRejectReason("");
        setRejectModalOpen(true);
    };

    // 거절하기
    const handleRejectSubmit = () => {
        if (!rejectReason.trim()) {
            message.warning("거절 사유를 입력해주세요.");
            return;
        }

        onReject({
            applicationId: selectedApplicantId,
            rejectReason: rejectReason.trim(),
        });

        setRejectModalOpen(false);
        setSelectedApplicantId(null);
        setRejectReason("");
    };

    const columns = [
        {
            title: "이름",
            dataIndex: "nickname",
            key: "nickname",
        },
        {
            title: "신청일",
            dataIndex: "updateAt",
            key: "updateAt",
            render: (createdAt) =>
                createdAt ? createdAt.replace("T", " ").slice(0, 16) : "-",
        },
        {
            title: "AI 한줄평",
            dataIndex: "aiSummary",
            key: "aiSummary",
            width: 220,
            render: (aiSummary) => (
                <Text
                    ellipsis={{
                        tooltip: aiSummary || "신뢰도가 높은 회원입니다.",
                    }}
                >
                    {aiSummary || "신뢰도가 높은 회원입니다."}
                </Text>
            ),
        },

        {
            title: "신청 상태",
            dataIndex: "applyStatus",
            key: "applyStatus",
            align: "center",
            render: (status) => {
                if (status === "PENDING") {
                    return <Tag color="gold">승인 대기</Tag>;
                }

                if (status === "APPROVED") {
                    return <Tag color="green">승인</Tag>;
                }

                if (status === "REJECTED") {
                    return <Tag color="red">거절</Tag>;
                }

                return <Tag>{status}</Tag>;
            },
        },
        {
            title: "거절 사유",
            dataIndex: "rejectReason",
            key: "rejectReason",
            width: 220,
            render: (rejectReason, record) => {
                if (record.applyStatus !== "REJECTED") {
                    return "-";
                }

                return (
                    <Text ellipsis={{ tooltip: rejectReason }}>
                        {rejectReason || "-"}
                    </Text>
                );
            },
        },
        {
            title: "관리",
            key: "action",
            align: "center",
            render: (_, record) => (
                <Space>
                    {record.applyStatus === "PENDING" && (
                        <>
                            <Button
                                type="primary"
                                size="small"
                                onClick={() => onApprove(record.id)}
                            >
                                승인
                            </Button>

                            <Button
                                danger
                                size="small"
                                onClick={() => handleRejectClick(record.id)}
                            >
                                거절
                            </Button>
                        </>
                    )}
                </Space>
            ),
        },
    ];

    return (
        <>
            {/* 신청자 관리 Modal */}
            <Modal
                title="신청자 관리"
                open={open}
                onCancel={onCancel}
                footer={[
                    <Button key="close" onClick={onCancel}>
                        닫기
                    </Button>,
                ]}
                width={1100}
            >
                <Table
                    rowKey={(record) => record.id}
                    loading={loading}
                    columns={columns}
                    dataSource={meetupApplicants?.applicants || []}
                    pagination={{
                        pageSize: 10,
                        showSizeChanger: false,
                    }}
                    scroll={{ x: 1000 }}
                />
            </Modal>

            {/* 거절 사유 Modal */}
            <Modal
                title="신청자 거절"
                open={rejectModalOpen}
                onCancel={() => setRejectModalOpen(false)}
                onOk={handleRejectSubmit}
                okText="거절하기"
                cancelText="취소"
                okButtonProps={{ danger: true }}
            >
                <Input.TextArea
                    rows={4}
                    placeholder="신청자를 거절하는 사유를 입력해주세요."
                    value={rejectReason}
                    onChange={(e) => setRejectReason(e.target.value)}
                    maxLength={200}
                    showCount
                />
            </Modal>
        </>
    );
}

export default MeetupApplicantModal;
