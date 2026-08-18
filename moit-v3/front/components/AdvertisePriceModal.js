import { useState } from 'react';
import {
  Modal,
  Table,
  InputNumber,
  message,
} from 'antd';

function AdvertisePriceModal({
  open,
  onClose,
}) {
  const [newPrices, setNewPrices] = useState([
    {
      key: 'new-1',
      days: 1,
      generalPrice: 10000,
      premiumPrice: 18000,
    },
    {
      key: 'new-7',
      days: 7,
      generalPrice: 70000,
      premiumPrice: 120000,
    },
    {
      key: 'new-14',
      days: 14,
      generalPrice: 120000,
      premiumPrice: 210000,
    },
    {
      key: 'new-30',
      days: 30,
      generalPrice: 250000,
      premiumPrice: 390000,
    },
    {
      key: 'new-60',
      days: 60,
      generalPrice: 450000,
      premiumPrice: 700000,
    },
    {
      key: 'new-90',
      days: 90,
      generalPrice: 630000,
      premiumPrice: 980000,
    },
  ]);

  const [extensionPrices, setExtensionPrices] = useState([
    {
      key: 'extension-7',
      days: 7,
      generalPrice: 60000,
      premiumPrice: 105000,
    },
    {
      key: 'extension-14',
      days: 14,
      generalPrice: 110000,
      premiumPrice: 195000,
    },
    {
      key: 'extension-30',
      days: 30,
      generalPrice: 230000,
      premiumPrice: 360000,
    },
    {
      key: 'extension-60',
      days: 60,
      generalPrice: 420000,
      premiumPrice: 650000,
    },
    {
      key: 'extension-90',
      days: 90,
      generalPrice: 590000,
      premiumPrice: 900000,
    },
  ]);

  const [positionPrices, setPositionPrices] = useState([
    {
      key: 'MEETUP_LIST_BANNER',
      label: '모집목록 배너',
      price: 10000,
    },
    {
      key: 'MEETUP_LIST_SIDEBAR',
      label: '모집목록 사이드',
      price: 10000,
    },
    {
      key: 'MEETUP_DETAIL_SIDEBAR',
      label: '모임 상세 사이드',
      price: 10000,
    },
  ]);

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
      visible={open}
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