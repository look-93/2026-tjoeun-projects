import { Row, Col, Button, Table, Tag, Spin } from 'antd';
import AdminStatCard from '../../components/AdminStatCard';
import AdminSearchBox from '../../components/AdminSearchBox';
import AdminListTabs from '../../components/AdminListTabs';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

// 💡 reviewReducer에 정의된 정확한 액션 이름들로 임포트합니다!
import {
  getReviewListRequest,
  deleteReviewRequest,
  // changeReviewVisibilityRequest 액션이 사가에 있다면 함께 임포트, 혹은 아래 참고
} from '../../reducers/reviewReducer'; 

function AdminReviewPage() {
  const dispatch = useDispatch();

  // Redux 스토어에서 리뷰 데이터 가져오기 (totalCount, totalPage도 함께 가져올 수 있습니다)
  const { reviews, totalCount, loading } = useSelector(
    (state) => state.review
  );

  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 10;

  // 검색 및 탭 조건
  const [searchType, setSearchType] = useState('all');
  const [status, setStatus] = useState('all');
  const [searchText, setSearchText] = useState('');
  const [listType, setListType] = useState('all');

  // 통계 카드 데이터 바인딩
  const publicCount = reviews ? reviews.filter((r) => r.isPublic === 'Y' || r.isPublic === true).length : 0;
  const hiddenCount = reviews ? reviews.filter((r) => r.isPublic === 'N' || r.isPublic === false).length : 0;

  const stats = [
    { title: '전체 후기', value: totalCount || 0, suffix: '개' },
    { title: '공개된 후기', value: publicCount, suffix: '개' },
    { title: '비공개 후기', value: hiddenCount, suffix: '개' },
  ];

  const listTabs = [{ key: 'all', label: '전체 후기' }];

  // 테이블 컬럼 정의
  const adminColumns = [
    {
      title: '번호',
      dataIndex: 'id',
      key: 'id',
      width: 80,
      align: 'center',
      render: (_, record, index) => (reviews ? totalCount - ((currentPage - 1) * pageSize + index) : 0),
    },
    {
      title: '모임 번호',
      dataIndex: 'meetupId',
      key: 'meetupId',
      width: 100,
      align: 'center',
    },
    {
      title: '작성자',
      dataIndex: 'memberNickname',
      key: 'memberNickname',
      width: 120,
      align: 'center',
    },
    {
      title: '내용',
      dataIndex: 'content',
      key: 'content',
      ellipsis: true,
    },
    {
      title: '상태',
      dataIndex: 'isPublic',
      key: 'isPublic',
      width: 100,
      align: 'center',
      render: (isPublic) =>
        isPublic === 'N' || isPublic === false ? (
          <Tag color="default">비공개</Tag>
        ) : (
          <Tag color="success">공개</Tag>
        ),
    },
    {
      title: '작성일',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 150,
      align: 'center',
      render: (createdAt) =>
        createdAt ? createdAt.replace('T', ' ').slice(0, 16) : '-',
    },
    {
      title: '관리',
      key: 'action',
      width: 160,
      align: 'center',
      render: (_, record) => {
        const isHidden = record.isPublic === 'N' || record.isPublic === false;
        return (
          <div style={{ display: 'flex', gap: 6, justifyContent: 'center' }}>
            <Button
              size="small"
              onClick={() => handleToggleVisibility(record.id)}
            >
              {isHidden ? '공개 전환' : '비공개 처리'}
            </Button>
            <Button
              size="small"
              danger
              onClick={() => handleDelete(record.id)}
            >
              삭제
            </Button>
          </div>
        );
      },
    },
  ];

  // 검색 핸들러
  const handleSearch = (values) => {
    setSearchType(values.category || 'all');
    setSearchText(values.keyword || '');
    setStatus(values.status || 'all');
    setCurrentPage(1);
  };

  // 💡 데이터 요청: getReviewListRequest 액션 사용
  useEffect(() => {
    dispatch(
      getReviewListRequest({
        page: currentPage - 1,
        size: pageSize,
        searchType: searchType === 'all' ? null : searchType,
        searchText: searchText || null,
        status: status === 'all' ? null : status,
      })
    );
  }, [currentPage, searchType, searchText, status, dispatch]);

  // 공개/비공개 토글 핸들러 (사이드 이펙트 처리에 맞게 연동)
  const handleToggleVisibility = (reviewId) => {
  dispatch({
    type: 'review/changeVisibilityRequest',
    payload: reviewId,
  });
};

  // 💡 삭제 요청: deleteReviewRequest 액션 사용 (리듀서에서 목록 자동 필터링 처리됨)
  const handleDelete = (reviewId) => {
    dispatch(deleteReviewRequest(reviewId));
  };

  if (!reviews) {
    return (
      <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', height: '300px', gap: '16px' }}>
        <Spin size="large" />
        <span>후기 정보를 불러오는 중입니다...</span>
      </div>
    );
  }

  return (
    <>
      <Row gutter={[16, 16]}>
        {stats.map((stat) => (
          <Col xs={24} sm={12} md={12} lg={8} key={stat.title}>
            <AdminStatCard {...stat} />
          </Col>
        ))}
      </Row>

      <AdminListTabs tabs={listTabs} activeTab={listType} onChange={setListType} />

      <AdminSearchBox
        conditions={[
          {
            key: 'category',
            defaultValue: 'all',
            options: [
              { value: 'all', label: '전체' },
              { value: 'content', label: '내용' },
              { value: 'nickname', label: '작성자' },
            ],
          },
          {
            key: 'status',
            defaultValue: 'all',
            options: [
              { value: 'all', label: '전체 상태' },
              { value: 'public', label: '공개' },
              { value: 'hidden', label: '비공개' },
            ],
          },
        ]}
        onSearch={handleSearch}
      />

      <div className="admin-table-box">
        <Table
          columns={adminColumns}
          dataSource={reviews}
          pagination={{
            current: currentPage,
            pageSize: pageSize,
            total: totalCount,
            onChange: (page) => setCurrentPage(page),
            showSizeChanger: false,
          }}
          rowKey="id"
          scroll={{ x: 800 }}
        />
      </div>
    </>
  );
}

export default AdminReviewPage;