import React from 'react';
import { Row, Col, Card, Typography } from 'antd';

const { Text } = Typography;

function AdBanner({ ad }) {
  return (
    <Row>
      <Col span={24}>
        <Card hoverable className="main-ad-card">
          {ad?.image ? (
            <img src={ad.image} alt={ad.title || '광고'} className="ad-image" />
          ) : (
            <div className="ad-placeholder">
              <Text type="secondary">광고 이미지</Text>
            </div>
          )}
        </Card>
      </Col>
    </Row>
  );
}

export default AdBanner;
