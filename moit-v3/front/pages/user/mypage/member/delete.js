import React from 'react';
import { Alert, Button, Card, Col, Form, Input, Row, Typography } from 'antd';
import { ExclamationCircleOutlined } from '@ant-design/icons';

const { Title } = Typography;

function UserMyMemberDeletePage() {
  const [form] = Form.useForm();

  const handleSubmit = (values) => {
    console.log('회원 탈퇴:', values);

    // TODO
    // Redux Saga 연결 후 회원 탈퇴 API 호출
  };

  const handleCancel = () => {
    window.history.back();
  };

  return (
    <div className="mypage-main-content">
      <Card className="mypage-user-info">
        {/* 제목 */}
        <Title level={3} className="member-edit-title">
          회원 탈퇴
        </Title>

        {/* 탈퇴 안내 */}
        <Alert
          className="withdraw-notice"
          type="warning"
          showIcon
          icon={<ExclamationCircleOutlined />}
          message="회원 탈퇴 안내"
          description={
            <ul>
              <li>탈퇴 후에는 로그인이 불가능합니다.</li>
              <li>작성한 게시글과 댓글은 삭제되지 않습니다.</li>
              <li>회원 정보는 탈퇴 회원으로 처리됩니다.</li>
              <li>탈퇴 후에는 복구가 어렵습니다.</li>
            </ul>
          }
        />

        {/* 탈퇴 폼 */}
        <Form form={form} layout="vertical" onFinish={handleSubmit}>
          <Row gutter={[20, 0]}>
            <Col xs={24} md={12}>
              <Form.Item
                label="비밀번호 확인"
                name="password"
                rules={[
                  {
                    required: true,
                    message: '현재 비밀번호를 입력해주세요.',
                  },
                ]}
              >
                <Input.Password placeholder="현재 비밀번호를 입력하세요." />
              </Form.Item>
            </Col>
          </Row>

          {/* 버튼 */}
          <div className="mypage-btn-group">
            <Button onClick={handleCancel}>취소</Button>

            <Button danger htmlType="submit">
              회원 탈퇴
            </Button>
          </div>
        </Form>
      </Card>
    </div>
  );
}

export default UserMyMemberDeletePage;
