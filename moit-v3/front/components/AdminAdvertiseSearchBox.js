import React from 'react';
import { Input, Select, Button, Space } from 'antd';

function AdminAdvertiseSearchBox({ 
  tab, 
  searchText, 
  setSearchText, 
  status, 
  setStatus, 
  sort, 
  setSort, 
  onSearch 
}) {

  // 탭별 상태/유형 옵션 분기
  const getStatusOptions = () => {
    if (tab === 'approval') {
      return [
        { value: '', label: '전체 상태' },
        { value: 'WAITING', label: '승인 대기' },
        { value: 'PAYMENT_WAITING', label: '결제 대기' },
        { value: 'REJECTED', label: '반려' },
      ];
    } else if (tab === 'payment') {
      return [
        { value: '', label: '전체 유형' },
        { value: 'NEW', label: '신규 결제' },
        { value: 'EXTENSION', label: '연장 결제' },
        { value: 'WAITING', label: '결제 대기' },
      ];
    } else {
      return [
        { value: '', label: '전체 상태' },
        { value: 'BEFORE_OPEN', label: '게시 전' },
        { value: 'OPEN', label: '게시 중' },
        { value: 'CLOSED', label: '종료' },
      ];
    }
  };

  return (
    <div style={{ marginBottom: 20, background: '#fff', padding: '16px', borderRadius: '8px', boxShadow: '0 1px 2px rgba(0,0,0,0.05)' }}>
      <Space wrap>
        {/* 검색어 */}
        <Input
          placeholder="검색어를 입력하세요."
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          onPressEnter={onSearch}
          style={{ width: 250 }}
          allowClear
        />

        {/* 상태/유형 필터 */}
        <Select
          value={status}
          onChange={(value) => setStatus(value)}
          style={{ width: 150 }}
          options={getStatusOptions()}
        />

        {/* 정렬 */}
        <Select
          value={sort}
          onChange={(value) => setSort(value)}
          style={{ width: 160 }}
          options={[
            { value: '', label: '최신 등록순' },
            { value: 'start', label: '시작/게시 빠른순' },
            { value: 'end', label: '종료 임박순' },
            { value: 'budget', label: '예산 높은순' },
            { value: 'impressions', label: '노출수순' },
            { value: 'clicks', label: '클릭수순' },
          ]}
        />

        {/* 검색 버튼 */}
        <Button type="primary" onClick={onSearch}>
          검색
        </Button>
      </Space>
    </div>
  );
}

export default AdminAdvertiseSearchBox;