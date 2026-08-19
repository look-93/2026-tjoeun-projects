import React, { useEffect, useState} from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { qnaListRequest } from '../../../reducers/qnaReducer';
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
  const dispatch = useDispatch();

  const { qnaList, loading } = useSelector((state) => state.qna);

  const [page, setPage] = useState(1);
  const [searchType, setSearchType] = useState('title');
  const [keyword, setKeyword] = useState('');
  const [searchKeyword, setSearchKeyword] = useState('');

  useEffect(() => {
    dispatch(
      qnaListRequest({
        page,
        type: searchType,
        keyword: searchKeyword,
      })
    );
  }, [dispatch, page, searchType, searchKeyword]);

  const list = qnaList?.list || [];

  // 통계
  const stats = [
    {
      title: '전체 문의',
      value: qnaList?.totalCnt || 0,
      suffix: '건',
      icon: FileTextOutlined,
    },
    {
      title: '답변 대기',
      value: list.filter((qna) => qna.qnaStatus === 'PENDING').length,
      suffix: '건',
      icon: ClockCircleOutlined,
    },
    {
      title: '답변 완료',
      value: list.filter((qna) => qna.qnaStatus === 'ANSWERED').length,
      suffix: '건',
      icon: CheckCircleOutlined,
    },
  ];

  const columns = [
    {
      title: '번호',
      dataIndex: 'questionId',
      key: 'questionId',
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
      dataIndex: 'qnaStatus',
      key: 'qnaStatus',
      width: 120,
      align: 'center',
      render: (qnaStatus) =>
        qnaStatus === 'PENDING' ? (
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
      render: (date) =>
        date ? new Date(date).toLocaleString('ko-KR') : '-',
    },
    {
      title: '답변일',
      key: 'answeredAt',
      width: 160,
      align: 'center',
      render: (_, record) =>
        record.answer?.createdAt
          ? new Date(record.answer.createdAt).toLocaleString('ko-KR')
          : '-',
    },
  ];

  // 검색
  const handleSearch = () => {
    setPage(1);
    setSearchKeyword(keyword);
  };

  // 검색 초기화
  const handleReset = () => {
    setKeyword('');
    setSearchKeyword('');
    setSearchType('title');
    setPage(1);
  };

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
                value={searchType}
                onChange={setSearchType}
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

              <Input
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                onPressEnter={handleSearch}
                placeholder="검색어를 입력하세요."
                allowClear
              />

              <Button
                type="primary"
                icon={<SearchOutlined />}
                onClick={handleSearch}
              >
                검색
              </Button>

              <Button onClick={handleReset}>초기화</Button>
            </Space.Compact>
          </Col>
        </Row>
      </Card>

      {/* 문의 목록 */}
      <Card className="mypage-qna-list">
        <Title level={3}>내 문의 내역</Title>

        <Table
          rowKey="questionId"
          columns={columns}
          dataSource={list}
          loading={loading}
          pagination={{
            current: qnaList?.page || 1,
            pageSize: 10,
            total: qnaList?.totalCnt || 0,
            showSizeChanger: false,
            onChange: (newPage) => {
              setPage(newPage);
            },
          }}
          scroll={{ x: 900 }}
        />
      </Card>
    </div>
  );
}

export default UserMyQuestionPage;