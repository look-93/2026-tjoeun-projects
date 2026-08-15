import React from 'react';
import {
  Button,
  Card,
  Col,
  Input,
  Row,
  Select,
  Space,
  Table,
  Tag,
  Typography,
} from 'antd';
import {
  SearchOutlined,
  LockOutlined,
  FileTextOutlined,
  ClockCircleOutlined,
  CheckCircleOutlined,
} from '@ant-design/icons';

import MyPageStatCard from '../../../components/MyPageStatCard';

const { Title, Text } = Typography;

function UserMyQuestionPage() {
  // 샘플 데이터
  const qnaList = [
    {
      key: 1,
      questionId: 1,
      category: 'MEETUP',
      title: '모임 신청은 어떻게 하나요?',
      isPublic: 'Y',
      status: 'PENDING',
      createdAt: '2026.08.10 14:20:00',
      answeredAt: null,
    },
    {
      key: 2,
      questionId: 2,
      category: 'ADMIN',
      title: '회원 탈퇴 관련 문의드립니다.',
      isPublic: 'N',
      status: 'ANSWERED',
      createdAt: '2026.08.08 10:30:00',
      answeredAt: '2026.08.08 15:20:00',
    },
    {
      key: 3,
      questionId: 3,
      category: 'MEETUP',
      title: '모임 장소가 변경되었나요?',
      isPublic: 'Y',
      status: 'ANSWERED',
      createdAt: '2026.08.05 09:10:00',
      answeredAt: '2026.08.05 11:40:00',
    },
  ];

  // 통계
  const stats = [
    {
      title: '전체 문의',
      value: qnaList.length,
      suffix: '건',
      icon: FileTextOutlined,
    },
    {
      title: '답변 대기',
      value: qnaList.filter((qna) => qna.status === 'PENDING').length,
      suffix: '건',
      icon: ClockCircleOutlined,
    },
    {
      title: '답변 완료',
      value: qnaList.filter((qna) => qna.status === 'ANSWERED').length,
      suffix: '건',
      icon: CheckCircleOutlined,
    },
  ];

  const columns = [
    {
      title: '번호',
      dataIndex: 'key',
      key: 'key',
      width: 80,
      align: 'center',
    },
    {
      title: '문의구분',
      dataIndex: 'category',
      key: 'category',
      width: 120,
      align: 'center',
      render: (category) =>
        category === 'MEETUP' ? '모임 문의' : '관리자 문의',
    },
    {
      title: '제목',
      dataIndex: 'title',
      key: 'title',
      render: (title) => (
        <Text strong style={{ cursor: 'pointer' }}>
          {title}
        </Text>
      ),
    },
    {
      title: '공개여부',
      dataIndex: 'isPublic',
      key: 'isPublic',
      width: 120,
      align: 'center',
      render: (isPublic) =>
        isPublic === 'Y' ? (
          <Tag color="blue">🔓 공개</Tag>
        ) : (
          <Tag color="error" icon={<LockOutlined />}>
            비공개
          </Tag>
        ),
    },
    {
      title: '답변상태',
      dataIndex: 'status',
      key: 'status',
      width: 120,
      align: 'center',
      render: (status) =>
        status === 'PENDING' ? (
          <Tag color="warning">답변 대기</Tag>
        ) : (
          <Tag color="success">답변 완료</Tag>
        ),
    },
    {
      title: '등록일',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 160,
      align: 'center',
    },
    {
      title: '답변일',
      dataIndex: 'answeredAt',
      key: 'answeredAt',
      width: 160,
      align: 'center',
      render: (date) => date || '-',
    },
  ];

  return (
    <div className="mypage-qna">
      {/* 통계 */}
      <MyPageStatCard stats={stats} />

      {/* 검색 */}
      <Card className="mypage-qna-filter">
        <Row justify="space-between" align="middle" gutter={[16, 16]}>
          <Col xs={24} md={14}>
            <Space.Compact style={{ width: '100%' }}>
              <Select
                defaultValue="title"
                style={{ width: 110 }}
                options={[
                  {
                    value: 'title',
                    label: '제목',
                  },
                  {
                    value: 'content',
                    label: '내용',
                  },
                ]}
              />

              <Input placeholder="검색어를 입력하세요." allowClear />

              <Button type="primary" icon={<SearchOutlined />}>
                검색
              </Button>

              <Button>초기화</Button>
            </Space.Compact>
          </Col>
        </Row>
      </Card>

      {/* 문의 목록 */}
      <Card className="mypage-qna-list">
        <Title level={3}>내 문의 내역</Title>

        <Table
          columns={columns}
          dataSource={qnaList}
          pagination={{
            pageSize: 10,
            showSizeChanger: false,
          }}
          scroll={{ x: 900 }}
        />
      </Card>
    </div>
  );
}

export default UserMyQuestionPage;
