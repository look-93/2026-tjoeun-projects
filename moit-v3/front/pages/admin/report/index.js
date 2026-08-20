// pages/admin/report/index.js
// 관리자 신고 목록 페이지

import { Row, Col, Button, Input, Select, Table } from 'antd';
import AdminStatCard from '../../../components/AdminStatCard';
import AdminSearchBox from '../../../components/AdminSearchBox';
import AdminListTabs from '../../../components/AdminListTabs';
import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useRouter } from 'next/router';
// http://localhost:3000/admin/report

function AdminReportPage() {
  //테스트용 테이터
  const serverData = { allcnt: 1200, pending: 1000, close: 1200 };
  const stats = [
    { title: '전체 신고', value: serverData.allcnt, suffix: '개' },
    { title: '신고 대기', value: serverData.running, suffix: '개' },
    { title: '신고 승인', value: serverData.close, suffix: '개' },
    { title: '모집 마감', value: 100, suffix: '개' },
  ];

  // =====================================================
  // 신고 대상 한글
  // =====================================================
  const getTargetTypeText = (targetType) => {
      if (targetType === 'MEETUP') {
          return '모임';
      }
      if (targetType === 'REVIEW') {
          return '후기';
      }
  };

  // =====================================================
  // 신고 사유 한글
  // =====================================================
  const getReasonCodeText = (reasonCode) => {

      switch (reasonCode) {

          case 'ABUSE':
              return '욕설/비방';

          case 'SPAM':
              return '도배/스팸';

          case 'FAKE_INFO':
              return '허위 정보';

          case 'AD':
              return '광고성 게시물';

          case 'NOSHOW':
              return '노쇼';

          case 'ETC':
              return '기타';

          default:
              return reasonCode;
      }
  };


  // =====================================================
  // 처리 상태
  // =====================================================
  const getStatusTag = (status) => {
      if (status === 'PENDING') {
          return (
              <Tag color="orange">
                  처리 대기
              </Tag>
          );
      }
      if (status === 'APPROVED') {
          return (
              <Tag color="green">
                  승인
              </Tag>
          );
      }
      if (status === 'REJECTED') {
          return (
              <Tag color="red">
                  반려
              </Tag>
          );
      }
  }

  // =====================================================
  // 뱃지 statusCode, statusName
  // =====================================================
  const getStatusCodeTag = (statusCode) => {
      if (statusCode === 'ACTIVE') {
          return (
              <Tag color="green">
                  정상
              </Tag>
          );
      }
      if (statusCode === 'WARNING') {
          return (
              <Tag color="orange">
                  주의
              </Tag>
          );
      }
      if (statusCode === 'DANGER') {
          return (
              <Tag color="red">
                  위험
              </Tag>
          );
      }

      return '-';
  };

  const adminColumns = [
    {
      title: '신고번호',
      dataIndex: 'reportId',
      key: 'reportId',
      width: 100,
      align: 'center',
    },
    {
      title: '신고자',
      dataIndex: 'memberNickname',
      key: 'memberNickname'
    },
    {
      title: '신뢰도 점수',
      dataIndex: 'trustScore',
      key: 'trustScore'
    },
    {
      title: '뱃지',
      dataIndex: 'statusCode',
      key: 'statusCode',

      render: (statusCode) => (
          getStatusCodeTag(statusCode)
      )
    },
    {
      title: '신고 대상',
      dataIndex: 'targetType',
      key: 'targetType',

      render: (targetType) => (
          getTargetTypeText(targetType)
      )
    },
    {
      title: '대상 ID',
      dataIndex: 'targetId',
      key: 'targetId'
    },
    {
      title: '신고 사유',
      dataIndex: 'reasonCode',
      key: 'reasonCode',

      render: (reasonCode) => (
          getReasonCodeText(reasonCode)
      )
    },
    {
      title: '처리 상태',
      dataIndex: 'status',
      key: 'status',

      render: (status) => (
          getStatusTag(status)
      )
    },
    {
      title: '신고일',
      dataIndex: 'createdAt',
      key: 'createdAt',

      render: (createdAt) => createdAt?.slice(0, 10)
    },
    {
      title: '상태',
      dataIndex: 'status',
      key: 'status',
      align: 'center',
      render: (_, record) => (
        <div style={{ display: 'flex', gap: 8, justifyContent: 'center' }}>
          <Button size="small">수정</Button>
          <Button size="small" danger>삭제</Button>
        </div>
      ),
    },
  ];

  const adminData = [
    {
      key: 1,
      reportId: 1,
      memberNickname: 'user01',
      trustScore: '보라',
      statusCode: '김보라',
      targetType: 'bora@moit.com',
      targetId: '2026-08-01',
      reasonCode: '',
      status: '',
      createdAt: '',
    },
    {
      key: 2,
      reportId: 2,
      memberNickname: 'user02',
      trustScore: '철수',
      statusCode: '김철수',
      targetType: 'chulsoo@moit.com',
      targetId: '2026-08-01',
      reasonCode: '',
      status: '',
      createdAt: '',
    },
  ];
  


  // 체크박스
  const [checkStrictly, setCheckStrictly] = useState(false);

  const rowSelection = {
    checkStrictly,
    onChange: (selectedRowKeys, selectedRows) => {
      console.log('선택된 ID:', selectedRowKeys);
      console.log('선택된 데이터:', selectedRows);
    },
  };

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

      {/* 검색 영역 조건1개*/}
      {/* <AdminSearchBox
        conditions={[
          {
            key: 'searchType',
            defaultValue: 'title',
            options: [
              { value: 'title', label: '제목' },
              { value: 'content', label: '내용' },
            ],
          },
        ]}
      /> */}

      {/* 검색 영역 조건2개*/}
      <AdminSearchBox
        conditions={[
          {
            key: 'filter',
            defaultValue: 'all',
            options: [
              { value: 'all', label: '전체' },
              { value: 'targetType', label: 'MEETUP' },
              { value: 'targetType', label: 'REVIEW' },
              { value: 'deleteYn', label: 'DELETE' },
            ],
          },
          {
            key: 'status',
            defaultValue: 'all',
            options: [
              { value: 'all', label: '분류' },
              { value: 'status', label: 'PENDING' },
              { value: 'status', label: 'APPROVED' },
              { value: 'status', label: 'REJECTED' },
              { value: 'deleteYn', label: 'DELETE' },
            ],
          },
          {
            key: 'search',
            defaultValue: 'all',
            options: [
              { value: 'all', label: '키워드' },
              { value: 'memberNickname', label: '작성자' },
              { value: 'reasonCode', label: '사유' },
            ],
          },
        ]}
      />

      {/* 모임 목록 */}
      <div className="admin-table-box">
        <Table
          rowSelection={rowSelection}
          columns={adminColumns}
          dataSource={adminData}
          pagination={{ pageSize: 10, showSizeChanger: false }}
          rowKey="id"
          scroll={{ x: 800 }}
        />
      </div>
    </>
  );
}
export default AdminReportPage;
