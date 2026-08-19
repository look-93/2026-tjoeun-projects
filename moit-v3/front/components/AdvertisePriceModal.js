import { useState } from 'react';
import {
  Modal,
  Table,
  InputNumber,
  message,
} from 'antd';

// 가격 따로 빼서 사용자랑 같이 사용
import {
  NEW_PRICES,
  EXTENSION_PRICES,
  POSITION_PRICES,
} from '../../constants/advertisePrice';

function AdvertisePriceModal({
  open,
  onClose,
}) {

  const [newPrices, setNewPrices] = useState(NEW_PRICES);
  const [extensionPrices, setExtensionPrices] = useState(EXTENSION_PRICES);
  const [positionPrices, setPositionPrices] = useState(POSITION_PRICES);

  const updatePrice = (
    setter,
    key,
    field,
    value
  ) => {
    setter((prev) =>
      prev.map((item) =>
        item.key === key
          ? {
              ...item,
              [field]: value || 0,
            }
          : item
      )
    );
  };

  const priceInput = (
    value,
    record,
    field,
    setter
  ) => (
    <InputNumber
      value={value}
      min={0}
      step={1000}
      style={{ width: '100%' }}
      formatter={(value) =>
        `${value}`.replace(
          /\B(?=(\d{3})+(?!\d))/g,
          ','
        )
      }
      parser={(value) =>
        value.replace(/,/g, '')
      }
      onChange={(nextValue) =>
        updatePrice(
          setter,
          record.key,
          field,
          nextValue
        )
      }
    />
  );

  const priceColumns = [
    {
      title: '기간',
      dataIndex: 'days',
      key: 'days',
      width: 100,
      render: (days) => `${days}일`,
    },
    {
      title: '일반',
      dataIndex: 'generalPrice',
      key: 'generalPrice',
      render: (value, record) =>
        priceInput(
          value,
          record,
          'generalPrice',
          setNewPrices
        ),
    },
    {
      title: '프리미엄',
      dataIndex: 'premiumPrice',
      key: 'premiumPrice',
      render: (value, record) =>
        priceInput(
          value,
          record,
          'premiumPrice',
          setNewPrices
        ),
    },
  ];

  const extensionColumns = [
    {
      title: '기간',
      dataIndex: 'days',
      key: 'days',
      width: 100,
      render: (days) => `${days}일`,
    },
    {
      title: '일반',
      dataIndex: 'generalPrice',
      key: 'generalPrice',
      render: (value, record) =>
        priceInput(
          value,
          record,
          'generalPrice',
          setExtensionPrices
        ),
    },
    {
      title: '프리미엄',
      dataIndex: 'premiumPrice',
      key: 'premiumPrice',
      render: (value, record) =>
        priceInput(
          value,
          record,
          'premiumPrice',
          setExtensionPrices
        ),
    },
  ];

  const positionColumns = [
    {
      title: '광고 위치',
      dataIndex: 'label',
      key: 'label',
    },
    {
      title: '추가금',
      dataIndex: 'price',
      key: 'price',
      render: (value, record) =>
        priceInput(
          value,
          record,
          'price',
          setPositionPrices
        ),
    },
  ];

  const handleSave = () => {
    console.log('신규 가격', newPrices);
    console.log('연장 가격', extensionPrices);
    console.log('위치 추가금', positionPrices);

    message.success(
      '가격표가 저장되었습니다.'
    );

    onClose();
  };

  return (
    <Modal
      title="광고 가격표"
      open={open}
      onCancel={onClose}
      onOk={handleSave}
      okText="저장"
      cancelText="취소"
      width={750}
    >
      <h3>신규 광고</h3>

      <Table
        dataSource={newPrices}
        columns={priceColumns}
        pagination={false}
        rowKey="key"
        size="small"
      />

      <h3 style={{ marginTop: 24 }}>
        연장 광고
      </h3>

      <Table
        dataSource={extensionPrices}
        columns={extensionColumns}
        pagination={false}
        rowKey="key"
        size="small"
      />

      <h3 style={{ marginTop: 24 }}>
        위치별 추가금
      </h3>

      <Table
        dataSource={positionPrices}
        columns={positionColumns}
        pagination={false}
        rowKey="key"
        size="small"
      />
    </Modal>
  );
}

export default AdvertisePriceModal;