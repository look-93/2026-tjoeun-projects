import { Row, Col, Button, Table, Tag } from 'antd';
import AdminStatCard from '../../components/AdminStatCard';
import AdminSearchBox from '../../components/AdminSearchBox';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useState } from 'react';
import {
  qnaAdminListRequest,
} from '../../reducers/qnaReducer';
// http://localhost:3000/admin/question

function AdminQuestionPage() {
  const dispatch = useDispatch();

  const {
    adminQnaList,
    adminQnaTotal,
    adminQnaPage,
    adminQnaSize,
    adminQnaTotalPage,
    adminQnaStartPage,
    adminQnaEndPage,
    adminQnaAllCnt,
    adminQnaPendingCnt,
    adminQnaAnsweredCnt,
    adminQnaTodayCnt,
    loading,
  } = useSelector((state) => state.qna);

  const [searchParams, setSearchParams] = useState({
    type: 'all',
    keyword: '',
    status: 'all',
    startDate: '',
    endDate: '',
    page: 1,
    pageSize: 10,
  });

  useEffect(() => {
    dispatch(qnaAdminListRequest(searchParams));
  }, [dispatch, searchParams]);

  const stats = [
    { title: '전체 문의', value: adminQnaAllCnt, suffix: '건' },
    { title: '답변 대기', value: adminQnaPendingCnt, suffix: '건' },
    { title: '답변 완료', value: adminQnaAnsweredCnt, suffix: '건' },
    { title: '오늘 등록', value: adminQnaTodayCnt, suffix: '건' },
  ];

  const handleSearch = (values) => {
    setSearchParams({
      ...searchParams,
      ...values,
      page: 1,
    });
  };

  const handlePageChange = (page) => {
    setSearchParams({
      ...searchParams,
      page,
    });
  };

  const columns = [
    {
      title: '번호',
      dataIndex: 'questionId',
      key: 'questionId',
      width: 80,
      align: 'center',
    },
    {
      title: '카테고리',
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
    },
    {
      title: '작성자',
      dataIndex: 'nickname',
      key: 'nickname',
      width: 120,
    },
    {
      title: '작성일',
      dataIndex: 'createdAt',
      key: 'createdAt',
      width: 180,
      align: 'center',
      render: (value) =>
        value ? new Date(value).toLocaleString('ko-KR') : '',
    },
    {
      title: '상태',
      dataIndex: 'qnaStatus',
      key: 'qnaStatus',
      width: 120,
      align: 'center',
      render: (status) =>
        status === 'ANSWERED' ? (
          <Tag color="green">답변 완료</Tag>
        ) : (
          <Tag color="orange">답변 대기</Tag>
        ),
    },
    {
      title: 'AI 상태',
      dataIndex: 'analysisStatus',
      key: 'analysisStatus',
      width: 120,
      align: 'center',
      render: (status) => {
        if (!status) {
          return '-';
        }

        if (status === 'NORMAL') {
          return <Tag color="green">정상</Tag>;
        }

        return <Tag color="red">주의</Tag>;
      },
    },
  ];

  return (
    <>
      {/* 통계 */}
      <Row gutter={[16, 16]}>
        {stats.map((stat) => (
          <Col xs={24} sm={12} md={12} lg={6} key={stat.title}>
            <AdminStatCard {...stat} />
          </Col>
        ))}
      </Row>

      {/* 검색 영역 */}
      <AdminSearchBox
        conditions={[
          {
            key: 'type',
            defaultValue: 'all',
            options: [
              { value: 'all', label: '전체' },
              { value: 'title', label: '제목' },
              { value: 'content', label: '내용' },
              { value: 'nickname', label: '작성자' },
            ],
          },
          {
            key: 'status',
            defaultValue: 'all',
            options: [
              { value: 'all', label: '전체 상태' },
              { value: 'PENDING', label: '답변 대기' },
              { value: 'ANSWERED', label: '답변 완료' },
            ],
          },
        ]}
        onSearch={handleSearch}
      />

      {/* 문의 목록 */}
      <div className="admin-table-box">
        <Table
          loading={loading}
          columns={columns}
          dataSource={adminQnaList}
          pagination={{
            current: adminQnaPage,
            pageSize: adminQnaSize,
            total: adminQnaTotal,
            showSizeChanger: false,
            onChange: handlePageChange,
          }}
          rowKey="questionId"
          scroll={{ x: 1000 }}
        />
      </div>
    </>
  );
}

export default AdminQuestionPage;