import React from 'react';
import {
  Button,
  Card,
  Col,
  Input,
  Row,
  Select,
  Space,
  Statistic,
  Table,
  Tag,
  Typography,
  Rate,
} from 'antd';
import {
  EditOutlined,
  DeleteOutlined,
  FileTextOutlined,
  LockOutlined,
} from '@ant-design/icons';
import MyPageStatCard from '../../../components/MyPageStatCard';

const { Title, Text } = Typography;

function UserMyReviewPage() {
  // 샘플 데이터
  const reviews = [
    {
      key: 1,
      reviewId: 1,
      meetupId: 101,
      createdAt: '2026.06.10',
      rating: 5,
      content:
        '즐겁게 참여할 수 있었던 모임이었습니다. 분위기도 좋고 다음에도 참여하고 싶어요!',
      isPublic: 'Y',
    },
    {
      key: 2,
      reviewId: 2,
      meetupId: 102,
      createdAt: '2026.05.15',
      rating: 4,
      content: '좋은 분들과 함께해서 즐거웠습니다. 다음 모임도 기대됩니다.',
      isPublic: 'N',
    },
    {
      key: 3,
      reviewId: 3,
      meetupId: 103,
      createdAt: '2026.04.20',
      rating: 3,
      content: '모임 장소가 조금 아쉬웠지만 전반적으로 괜찮았습니다.',
      isPublic: 'Y',
    },
  ];

  const columns = [
    {
      title: '모임 정보',
      dataIndex: 'meetupId',
      key: 'meetupId',
      width: '20%',
      render: (meetupId, record) => (
        <div>
          <Text strong>모임 #{meetupId}</Text>

          <br />

          <Text type="secondary" style={{ fontSize: 12 }}>
            {record.createdAt}
          </Text>
        </div>
      ),
    },

    {
      title: '후기 내용',
      dataIndex: 'content',
      key: 'content',
      width: '45%',
      render: (content, record) => (
        <div>
          <Rate
            disabled
            value={record.rating}
            style={{
              fontSize: 14,
              marginBottom: 6,
            }}
          />

          <div
            style={{
              color: '#475569',
              lineHeight: 1.5,
              whiteSpace: 'pre-wrap',
            }}
          >
            {content}
          </div>
        </div>
      ),
    },

    {
      title: '공개 상태',
      dataIndex: 'isPublic',
      key: 'isPublic',
      width: '15%',
      align: 'center',
      render: (isPublic) =>
        isPublic === 'N' ? (
          <Tag color="error" icon={<LockOutlined />}>
            비공개
          </Tag>
        ) : (
          <Tag color="blue">전체공개</Tag>
        ),
    },

    {
      title: '관리',
      key: 'action',
      width: '20%',
      align: 'center',
      render: (_, record) => (
        <Space size={6}>
          <Button size="small" icon={<EditOutlined />}>
            수정
          </Button>

          <Button size="small" danger icon={<DeleteOutlined />}>
            삭제
          </Button>
        </Space>
      ),
    },
  ];

  const totalReviews = reviews.length;
  const privateReviews = reviews.filter(
    (review) => review.isPublic === 'N',
  ).length;
  const stats = [
    {
      title: '작성 후기',
      value: totalReviews,
      suffix: '개',
      icon: FileTextOutlined,
    },
    {
      title: '비공개 후기',
      value: privateReviews,
      suffix: '개',
      icon: LockOutlined,
    },
  ];

  return (
    <div className="mypage-reviews">
      {/* 통계 */}
      <MyPageStatCard stats={stats} />

      {/* 검색 / 정렬 */}
      <Card className="mypage-review-filter">
        <Row justify="space-between" align="middle" gutter={[16, 16]}>
          <Col xs={24} md={14}>
            <Space.Compact style={{ width: '100%' }}>
              <Input placeholder="후기 내용 검색" allowClear />

              <Button type="primary">검색</Button>

              <Button>초기화</Button>
            </Space.Compact>
          </Col>

          <Col xs={24} md={6}>
            <Select
              defaultValue="latest"
              style={{ width: '100%' }}
              options={[
                {
                  value: 'latest',
                  label: '최신순',
                },
                {
                  value: 'rating',
                  label: '별점순',
                },
              ]}
            />
          </Col>
        </Row>
      </Card>

      {/* 후기 목록 */}
      <Card className="mypage-review-list">
        <Title level={3}>내 작성 후기</Title>

        <Table
          columns={columns}
          dataSource={reviews}
          pagination={{
            pageSize: 10,
            showSizeChanger: false,
          }}
          scroll={{ x: 800 }}
        />
      </Card>
    </div>
  );
}

export default UserMyReviewPage;
