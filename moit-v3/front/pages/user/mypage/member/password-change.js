import React from 'react';
import { Alert, Button, Card, Form, Input, Progress, Typography } from 'antd';

const { Title, Text } = Typography;

function UserMyMemberPasswordChangePage() {
  return (
    <div className="mypage-password">
      <Card className="mypage-user-info">
        <Title level={3} className="member-edit-title">
          비밀번호 변경
        </Title>

        {/* 오류 메시지 */}
        {/* 기능 연결 후 사용 */}
        {/* <Alert
          message="현재 비밀번호가 올바르지 않습니다."
          type="error"
          showIcon
          style={{ marginBottom: 24 }}
        /> */}

        <Form layout="vertical" className="password-change-form">
          {/* 현재 비밀번호 */}
          <Form.Item label="현재 비밀번호" name="currentPassword">
            <Input.Password size="large" placeholder="현재 비밀번호 입력" />
          </Form.Item>

          {/* 새 비밀번호 */}
          <Form.Item label="새 비밀번호" name="newPassword">
            <Input.Password size="large" placeholder="새 비밀번호 입력" />
          </Form.Item>

          {/* 비밀번호 강도 */}
          <div style={{ marginTop: -12, marginBottom: 20 }}>
            <Progress percent={60} showInfo={false} size="small" />

            <Text type="secondary">비밀번호 강도: 보통</Text>
          </div>

          {/* 비밀번호 유출 검사 */}
          <Alert
            message="안전한 비밀번호입니다."
            type="success"
            showIcon
            style={{ marginBottom: 24 }}
          />

          {/* 새 비밀번호 확인 */}
          <Form.Item label="새 비밀번호 확인" name="passwordCheck">
            <Input.Password size="large" placeholder="새 비밀번호 확인" />
          </Form.Item>

          {/* 비밀번호 일치 */}
          <Text type="success">비밀번호가 일치합니다.</Text>

          {/* 버튼 */}
          <div className="mypage-btn-group">
            <Button>취소</Button>

            <Button type="primary" htmlType="submit">
              변경하기
            </Button>
          </div>
        </Form>
      </Card>
    </div>
  );
}

export default UserMyMemberPasswordChangePage;
