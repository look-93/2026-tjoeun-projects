import React from 'react';
import { Button, Card, Table, Tag, Typography } from 'antd';
import {
  FileTextOutlined,
  TeamOutlined,
  StarOutlined,
  HeartOutlined,
} from '@ant-design/icons';

import MyPageStatCard from '../../../components/MyPageStatCard';

// http://localhost:3000/user/mypage/meetup

const { Title, Text } = Typography;

function UserMyMeetupPage() {
  // 샘플 데이터
  const meetupData = [
    {
      key: 1,
      title: '코딩 스터디',
      date: '2026.06.10',
      status: '종료',
      review: '작성',
    },
    {
      key: 2,
      title: '운동 모임',
      date: '2026.05.15',
      status: '종료',
      review: '작성완료',
    },
    {
      key: 3,
      title: '독서 모임',
      date: '2026.07.20',
      status: '진행중',
      review: null,
    },
  ];

  // 통계
  const stats = [
    {
      title: '내 모집글',
      value: 12,
      suffix: '개',
      icon: FileTextOutlined,
    },
    {
      title: '신청 모임',
      value: 8,
      suffix: '개',
      icon: TeamOutlined,
    },
    {
      title: '작성 후기',
      value: 16,
      suffix: '개',
      icon: StarOutlined,
    },
    {
      title: '관심 모임',
      value: 6,
      suffix: '개',
      icon: HeartOutlined,
    },
  ];

  // 테이블
  const columns = [
    {
      title: '모임명',
      dataIndex: 'title',
      key: 'title',
      render: (title) => <Text strong>{title}</Text>,
    },
    {
      title: '모임일',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: '상태',
      dataIndex: 'status',
      key: 'status',
      align: 'center',
      render: (status) =>
        status === '종료' ? (
          <Tag>종료</Tag>
        ) : (
          <Tag color="processing">진행중</Tag>
        ),
    },
    {
      title: '후기',
      dataIndex: 'review',
      key: 'review',
      align: 'center',
      render: (review) => {
        // 후기를 작성할 수 없는 경우
        if (!review) {
          return '-';
        }

        // 이미 작성한 경우
        if (review === '작성완료') {
          return (
            <Button size="small" disabled>
              작성완료
            </Button>
          );
        }

        // 작성 가능한 경우
        return (
          <Button type="primary" size="small">
            후기 작성
          </Button>
        );
      },
    },
  ];

  return (
    <div className="mypage-container">
      {/* 통계 */}
      <MyPageStatCard stats={stats} />

      {/* 신청한 모임 */}
      <Card className="mypage-meetup-section" style={{ marginTop: 20 }}>
        <Title level={3}>신청한 모임</Title>

        <Table
          columns={columns}
          dataSource={meetupData}
          pagination={{
            pageSize: 10,
            showSizeChanger: false,
          }}
          scroll={{ x: 600 }}
        />
      </Card>
    </div>
  );
}

export default UserMyMeetupPage;
