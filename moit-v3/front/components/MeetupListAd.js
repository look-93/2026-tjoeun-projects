import React from 'react';
import { Card, Typography } from 'antd';

const { Text } = Typography;

function MeetupListAd({ ad }) {
  return (
    <div className="meetup-list-ad">
      <Card
        hoverable
        className="meetup-list-ad"
        styles={{ body: { padding: 0 } }}
      >
        {ad?.image ? (
          <img
            src={ad.image}
            alt={ad.title || '광고'}
            className="meetup-ad-image"
          />
        ) : (
          <div className="ad-placeholder">
            <Text>광고 이미지 준비중</Text>
          </div>
        )}
      </Card>
    </div>
  );
}

export default MeetupListAd;
