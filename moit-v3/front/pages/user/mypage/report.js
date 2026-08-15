import React from 'react';
import { Card, Table, Tag, Typography } from 'antd';

const { Title } = Typography;

function UserMyReportPage() {
  // 나중에 Redux / API 연결
  const reportData = [
    {
      key: 1,
      reportId: 1,
      targetType: 'MEETUP',
      targetId: 101,
      reasonCode: 'ABUSE',
      status: 'PENDING',
      createdAt: '2026.08.10',
    },
    {
      key: 2,
      reportId: 2,
      targetType: 'REVIEW',
      targetId: 205,
      reasonCode: 'SPAM',
      status: 'APPROVED',
      createdAt: '2026.08.08',
    },
    {
      key: 3,
      reportId: 3,
      targetType: 'MEETUP',
      targetId: 115,
      reasonCode: 'FAKE_INFO',
      status: 'REJECTED',
      createdAt: '2026.08.05',
    },
  ];

  const reasonMap = {
    ABUSE: '욕설/비방',
    SPAM: '도배/스팸',
    FAKE_INFO: '허위 정보',
    AD: '광고성 게시물',
    NOSHOW: '노쇼',
    ETC: '기타',
  };

  const statusMap = {
    PENDING: {
      text: '처리중',
      color: 'processing',
    },
    APPROVED: {
      text: '처리완료',
      color: 'success',
    },
    REJECTED: {
      text: '반려',
      color: 'error',
    },
  };

  const columns = [
    {
      title: '신고번호',
      dataIndex: 'reportId',
      key: 'reportId',
      align: 'center',
    },
    {
      title: '대상',
      dataIndex: 'targetType',
      key: 'targetType',
      align: 'center',
      render: (type) => (
        <Tag color={type === 'MEETUP' ? 'blue' : 'purple'}>
          {type === 'MEETUP' ? '모임' : '후기'}
        </Tag>
      ),
    },
    {
      title: '대상 글 번호 ID',
      dataIndex: 'targetId',
      key: 'targetId',
      align: 'center',
    },
    {
      title: '신고사유',
      dataIndex: 'reasonCode',
      key: 'reasonCode',
      align: 'center',
      render: (reason) => reasonMap[reason] || reason,
    },
    {
      title: '상태',
      dataIndex: 'status',
      key: 'status',
      align: 'center',
      render: (status) => {
        const current = statusMap[status];

        return <Tag color={current?.color}>{current?.text || status}</Tag>;
      },
    },
    {
      title: '신고일',
      dataIndex: 'createdAt',
      key: 'createdAt',
      align: 'center',
    },
    {
      title: '관리',
      key: 'action',
      align: 'center',
      render: (_, record) => (
        <a href={`/user/meetup/report/detail?reportId=${record.reportId}`}>
          상세
        </a>
      ),
    },
  ];

  return (
    <div className="mylist-page">
      <Card className="mylist-card">
        <Title level={3}>내 신고 내역</Title>

        <Table
          columns={columns}
          dataSource={reportData}
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

export default UserMyReportPage;
