import React from 'react';
import { Card } from 'antd';

function MeetupAd({ ad }) {
  return (
    <Card hoverable className="meetup-ad-card">
      {ad?.image ? (
        <img
          src={ad.image}
          alt={ad.title || '광고'}
          className="meetup-ad-image"
        />
      ) : (
        <div className="ad-placeholder">광고 이미지</div>
      )}
    </Card>
  );
}

export default MeetupAd;
