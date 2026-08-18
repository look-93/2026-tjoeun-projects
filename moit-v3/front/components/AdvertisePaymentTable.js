import { Button, Table } from 'antd';

function AdvertisePaymentTable({
  dataSource,
  page,
  size,
  onDetail,
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
      title: '광고명',
      dataIndex: 'title',
      key: 'title',
    },
    {
      title: '광고주',
      dataIndex: 'advertiserNickname',
      key: 'advertiserNickname',
    },
    {
      title: '결제 유형',
      dataIndex: 'paymentType',
      key: 'paymentType',
    },
    {
      title: '광고 등급',
      dataIndex: 'adGrade',
      key: 'adGrade',
    },
    {
      title: '결제 금액',
      dataIndex: 'amount',
      key: 'amount',
      render: (value) =>
        value != null
          ? `${Number(value).toLocaleString()}원`
          : '-',
    },
    {
      title: '결제 상태',
      dataIndex: 'paymentStatus',
      key: 'paymentStatus',
      align: 'center',
    },
    {
      title: '관리',
      key: 'action',
      align: 'center',
      render: (_, record) => (
        <Button
          size="small"
          onClick={() => onDetail(record.adId)}
        >
          상세
        </Button>
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

export default AdvertisePaymentTable;