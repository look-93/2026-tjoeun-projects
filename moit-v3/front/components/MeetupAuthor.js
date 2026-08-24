import React from "react";
import { Card, Space, Avatar, Button, Typography } from "antd";
import { UserOutlined, MessageOutlined } from "@ant-design/icons";
import { useRouter } from "next/router";

const { Text } = Typography;

function MeetupAuthor({ meetup }) {
    const router = useRouter();

    const handleQnaClick = () => {
        router.push(`/user/qna/questionWrite?type=MEETUP&meetupId=${meetup.meetupId}`);
    };
    return (
        <Card title="작성자" className="meetup-side-card">
            <Space>
                <Avatar size={48} icon={<UserOutlined />} />

                <div>
                    <Text strong>{meetup.nickname}</Text>

                    <div>
                        <Text type="secondary">모임 개설자</Text>
                    </div>
                </div>
            </Space>

            <Button
                block
                icon={<MessageOutlined />}
                style={{ marginTop: 20 }}
                onClick={handleQnaClick}
            >
                개설자에게 문의하기
            </Button>
        </Card>
    );
}

export default MeetupAuthor;
