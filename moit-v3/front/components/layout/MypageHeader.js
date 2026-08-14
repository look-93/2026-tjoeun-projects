import React from 'react';
import { Avatar, Tag } from 'antd';

function MypageHeader() {
  return (
    <div className="mypage-profile-card">
      <Avatar size={64} src={'#'}>
        손예진
      </Avatar>

      <div className="mypage-profile-info">
        <h6>예진이짱</h6>
        <p>예진@예진</p>

        <Tag>예진이</Tag>
      </div>
    </div>
  );
}

export default MypageHeader;
