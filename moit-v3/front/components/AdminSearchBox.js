import React from 'react';
import { Row, Col, Select, Input, Button } from 'antd';

function AdminSearchBox({
  conditions = [],
  placeholder = '검색어를 입력하세요',
  onSearch,
}) {
  return (
    <div className="admin-search-box">
      <Row gutter={[16, 16]} style={{ width: '100%' }}>
        {/* 검색 조건들 */}
        {conditions.map((condition, index) => (
          <Col flex={condition.width || '120px'} key={condition.key || index}>
            <Select
              className="admin-search-condition"
              defaultValue={condition.defaultValue}
              style={{ width: '100%' }}
              options={condition.options}
            />
          </Col>
        ))}

        {/* 검색어 */}
        <Col flex="auto">
          <Input className="admin-search-input" placeholder={placeholder} />
        </Col>

        {/* 검색 버튼 */}
        <Col>
          <Button
            className="admin-search-button"
            type="primary"
            onClick={onSearch}
          >
            검색
          </Button>
        </Col>
      </Row>
    </div>
  );
}

export default AdminSearchBox;
