import { Button, Table } from 'antd';

function AdvertiseStatusTable({
  dataSource,
  page,
  size,
  onDetail,
  onStatusChange,
}) {
  const BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";

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
            src={`${BASE_URL}${record.imageList[0].imageUrl}`}
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
      title: '상태',
      dataIndex: 'status',
      key: 'status',
      align: 'center',
      render: (value, record) => (
        <select
          value={value}
          onChange={(e) =>
            onStatusChange(
              record.adId,
              e.target.value
            )
          }
        >
          <option value="PENDING">
            PENDING
          </option>

          <option value="OPEN">
            OPEN
          </option>

          <option value="CLOSED">
            CLOSED
          </option>
        </select>
      ),
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
      title: '노출수',
      dataIndex: 'impressions',
      key: 'impressions',
      align: 'center',
      render: (value) => value ?? 0,
    },
    {
      title: '클릭수',
      dataIndex: 'clicks',
      key: 'clicks',
      align: 'center',
      render: (value) => value ?? 0,
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
        scroll={{ x: 1100 }}
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

export default AdvertiseStatusTable;