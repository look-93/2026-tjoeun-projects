import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/router'; // Next.js 라우터 사용
import { useDispatch, useSelector } from 'react-redux';
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
  Rate,
  message,
  Modal,
} from 'antd';
import {
  EditOutlined,
  DeleteOutlined,
  FileTextOutlined,
  LockOutlined,
  ExclamationCircleOutlined,
} from '@ant-design/icons';
import MyPageStatCard from '../../../components/MyPageStatCard';

import {
  getReviewListRequest,
  deleteReviewRequest,
} from '../../../reducers/reviewReducer';

const { Title, Text } = Typography;

function UserMyReviewPage() {
  const dispatch = useDispatch();
  const router = useRouter(); // Next.js 라우터 훅

  const { reviews = [], totalCount = 0, loading } = useSelector(
    (state) => state.review || {}
  );

  const [keyword, setKeyword] = useState('');
  const [searchKeyword, setSearchKeyword] = useState('');
  const [sort, setSort] = useState('id,desc');

  useEffect(() => {
    dispatch(
      getReviewListRequest({
        keyword: searchKeyword,
        sort: sort,
        page: 0,
        size: 10,
      })
    );
  }, [dispatch, searchKeyword, sort]);

  const handleSearch = () => {
    setSearchKeyword(keyword);
  };

  const handleReset = () => {
    setKeyword('');
    setSearchKeyword('');
  };

  const handleSortChange = (value) => {
    if (value === 'latest') {
      setSort('id,desc');
    } else if (value === 'rating') {
      setSort('rating,desc');
    }
  };

  const handleDelete = (reviewId) => {
    Modal.confirm({
      title: '정말 후기를 삭제하시겠습니까?',
      icon: <ExclamationCircleOutlined />,
      okText: '삭제',
      okType: 'danger',
      cancelText: '취소',
      onOk() {
        dispatch(deleteReviewRequest(reviewId));
        message.success('후기가 삭제되었습니다.');
        dispatch(getReviewListRequest({ keyword: searchKeyword, sort, page: 0, size: 10 }));
      },
    });
  };

  const handleEdit = (reviewId) => {
    router.push(`/user/mypage/review/edit/${reviewId}`);
  };

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
            {record.createdAt ? record.createdAt.substring(0, 10) : ''}
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
          <Button
            size="small"
            icon={<EditOutlined />}
            onClick={() => handleEdit(record.reviewId || record.id)}
          >
            수정
          </Button>
          <Button
            size="small"
            danger
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(record.reviewId || record.id)}
          >
            삭제
          </Button>
        </Space>
      ),
    },
  ];

  const totalReviews = totalCount || reviews.length;
  const privateReviews = reviews.filter(
    (review) => review.isPublic === 'N'
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
      <MyPageStatCard stats={stats} />

      <Card className="mypage-review-filter" style={{ marginBottom: 16 }}>
        <Row justify="space-between" align="middle" gutter={[16, 16]}>
          <Col xs={24} md={14}>
            <Space.Compact style={{ width: '100%' }}>
              <Input
                placeholder="후기 내용 검색"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                onPressEnter={handleSearch}
                allowClear
              />
              <Button type="primary" onClick={handleSearch}>
                검색
              </Button>
              <Button onClick={handleReset}>초기화</Button>
            </Space.Compact>
          </Col>

          <Col xs={24} md={6}>
            <Select
              defaultValue="latest"
              style={{ width: '100%' }}
              onChange={handleSortChange}
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

      <Card className="mypage-review-list">
        <Title level={3} style={{ marginBottom: 16 }}>내 작성 후기</Title>

        <Table
          columns={columns}
          dataSource={reviews}
          rowKey={(record) => record.reviewId || record.id}
          loading={loading}
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