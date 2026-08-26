import React from 'react';
import { Card, Typography, Space, Avatar } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import { useRouter } from 'next/router';

const { Title, Text, Paragraph } = Typography;

function QnaSection({ qnaLists = [], meetupId }) {
    const router = useRouter();

    return (
        <div>
            <Title level={4}>Q&A</Title>

            <Space direction="vertical" style={{ width: '100%' }}>
                {qnaLists.length === 0 ? (
                    <Card>
                        <Text type="secondary">
                            아직 등록된 Q&A가 없습니다.
                        </Text>
                    </Card>
                ) : (
                    qnaLists.map((qna) => (
                        <Card
                            key={qna.questionId}
                            hoverable
                            className="qna-card"
                            onClick={() =>
                                router.push(
                                    `/user/qna/questionDetail?questionId=${qna.questionId}`
                                )
                            }
                        >
                            <Space>
                                <Avatar icon={<UserOutlined />} />

                                <div>
                                    <Text strong>
                                        {qna.nickname || '익명'}
                                    </Text>

                                    <div>
                                        <Text type="secondary">
                                            {qna.title}
                                        </Text>
                                    </div>
                                </div>
                            </Space>

                            <Paragraph style={{ marginTop: 16 }}>
                                {qna.content}
                            </Paragraph>
                        </Card>
                    ))
                )}
            </Space>
        </div>
    );
}

export default QnaSection;