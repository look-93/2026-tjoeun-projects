import React, { useEffect } from 'react';
import { Card, Row, Col, Typography } from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import { getMyInfoRequest } from '../../reducers/userReducer';

const { Title, Text } = Typography;

function AdminHeader() {
  const dispatch = useDispatch();

  const user = useSelector(
    (state) => state.user.user
  );

  useEffect(() => {
    if (!user?.nickname || !user?.memberTypeId) {
      dispatch(getMyInfoRequest());
    }
  }, [dispatch, user?.nickname, user?.memberTypeId]);

  const memberTypeName =
    Number(user?.memberTypeId) === 4
      ? '최고관리자'
      : '관리자';

  return (
    <Card
      className="admin-header"
      bordered={false}
    >
      <Row
        align="middle"
        justify="space-between"
      >

        <Col>
          <Title
            level={3}
            className="admin-header-title"
          >
            관리자관리
          </Title>
        </Col>

        <Col>
          <Text className="admin-header-user">
            {user?.nickname || '관리자'}님 ({memberTypeName})
          </Text>
        </Col>

      </Row>
    </Card>
  );
}

export default AdminHeader;