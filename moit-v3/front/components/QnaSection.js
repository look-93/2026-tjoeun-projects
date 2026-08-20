import React, { useEffect } from 'react';
import { Card, Typography, Space, Avatar } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import { useDispatch, useSelector } from 'react-redux';
import { useRouter } from 'next/router';

import { qnaMeetupListRequest } from '../reducers/qnaReducer';

const { Title, Text, Paragraph } = Typography;

function QnaSection({ qnaLists = [], meetupId }) {
  const dispatch = useDispatch();
  const router = useRouter();

  // Redux의 특정 모임 Q&A 목록
  const reduxQnaLists = useSelector((state) => state.qna.qnaList || []);

  // 특정 모임 Q&A 조회
  useEffect(() => {
    if(!meetupId) return;
    dispatch(qnaMeetupListRequest(meetupId));
  }, [dispatch, meetupId]);

  return (
    <div>
      <Title level={4}>Q&A</Title>

      <Space direction="vertical" style={{ width: '100%' }}>
        {reduxQnaLists.map((qna) => (
          <Card key={qna.questionId} hoverable className="qna-card"
            onClick={() => router.push(`/user/qna/detail/${qna.questionId}`)}
          >
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

          </Card>
        ))}
      </Space>
    </div>
  );
}

export default QnaSection;
