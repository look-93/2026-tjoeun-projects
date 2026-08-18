import React from 'react';
import { Card, Typography, Space, Avatar, Divider } from 'antd';
import { UserOutlined } from '@ant-design/icons';

const { Title, Text, Paragraph } = Typography;

function QnaSection({ qnaLists = [] }) {
  return (
    <div>
      <Title level={4}>Q&A</Title>

      <Space direction="vertical" style={{ width: '100%' }}>
        {qnaLists.map((qna) => (
          <Card key={qna.id} hoverable className="qna-card">
            <Space>
              <Avatar icon={<UserOutlined />} />

              <div>
                <Text strong>{qna.nickname}</Text>

                <div>
                  <Text type="secondary">{qna.title}</Text>
                </div>
              </div>
            </Space>

            <Paragraph style={{ marginTop: 16 }}>{qna.content}</Paragraph>

            {qna.answer && (
              <>
                <Divider />

                <Text strong>답변</Text>

                <Paragraph>{qna.answer}</Paragraph>
              </>
            )}
          </Card>
        ))}
      </Space>
    </div>
  );
}

export default QnaSection;
