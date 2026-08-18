import { Button, Table } from 'antd';

function AdvertiseApprovalTable({
  dataSource,
  page,
  size,
  onDetail,
  onApprove,
  onReject,
}) {
  const columns = [
    {
      title: '번호',
      key: 'number',
      width: 70,
      align: 'center',
      render: (_, record, index) =>
        (page - 1) * size + index + 1,
    },
    {
      title: '이미지',
      key: 'image',
      width: 100,
      align: 'center',
      render: (_, record) => {
        if (!record.imageList?.length) {
          return '-';
        }

        return (
          <img
            src={record.imageList[0].imageUrl}
            alt={record.title}
            style={{
              width: 70,
              height: 45,
              objectFit: 'cover',
            }}
          />
        );
      },
    },
    {
      title: '광고명',
      dataIndex: 'title',
      key: 'title',
    },
    {
      title: '광고주',
      dataIndex: 'advertiserNickname',
      key: 'advertiserNickname',
      width: 120,
    },
    {
      title: '승인 상태',
      dataIndex: 'approvalStatus',
      key: 'approvalStatus',
      align: 'center',
    },
    {
      title: '광고 기간',
      key: 'period',
      render: (_, record) => (
        <>
          {formatDate(record.startDatetime)}
          {' ~ '}
          {formatDate(record.endDatetime)}
        </>
      ),
    },
    {
      title: '관리',
      key: 'action',
      width: 200,
      align: 'center',
      render: (_, record) => (
        <div
          style={{
            display: 'flex',
            gap: 6,
            justifyContent: 'center',
          }}
        >
          <Button
            size="small"
            onClick={() => onDetail(record.adId)}
          >
            상세
          </Button>

          <Button
            size="small"
            type="primary"
            onClick={() => onApprove(record.adId)}
          >
            승인
          </Button>

          <Button
            size="small"
            danger
            onClick={() => onReject(record.adId)}
          >
            반려
          </Button>
        </div>
      ),
    },
  ];

  return (
    <div className="admin-table-box">
      <Table
        columns={columns}
        dataSource={dataSource}
        rowKey="adId"
        pagination={false}
        scroll={{ x: 1000 }}
      />
    </div>
  );
}

function formatDate(value) {
  if (!value) {
    return '-';
  }

  return value.substring(0, 10);
}

export default AdvertiseApprovalTable;