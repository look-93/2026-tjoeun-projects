import React, { useState } from 'react';
import {
  Avatar,
  Button,
  Card,
  Col,
  DatePicker,
  Form,
  Input,
  Radio,
  Row,
  Select,
  Typography,
  Upload,
} from 'antd';
import { CameraOutlined, UserOutlined } from '@ant-design/icons';
import moment from 'moment';
const { Title, Text } = Typography;

function UserMyMemberEditPage() {
  const [form] = Form.useForm();

  const user = {
    loginId: 'hong123',
    nickname: '홍길동',
    email: 'hong@example.com',
    mobile: '01012345678',
    gender: 'M',
    birth: '1995-01-10',
    grade: '일반회원',
    profileUrl: null,
    interests: ['운동', '독서', '여행'],
  };

  const interests = [
    { value: '운동', label: '🏃 운동' },
    { value: '여행', label: '✈️ 여행' },
    { value: '게임', label: '🎮 게임' },
    { value: '독서', label: '📚 독서' },
    { value: '맛집', label: '🍽️ 맛집' },
    { value: '영화', label: '🎬 영화' },
    { value: '음악', label: '🎵 음악' },
    { value: '요리', label: '🍳 요리' },
  ];

  const [profilePreview, setProfilePreview] = useState(user.profileUrl);

  const handleProfileChange = ({ file }) => {
    if (!file.originFileObj) {
      return;
    }

    const url = URL.createObjectURL(file.originFileObj);
    setProfilePreview(url);
  };

  const handleSubmit = (values) => {
    console.log('회원정보 수정:', values);
  };

  return (
    <div className="mypage-main-content">
      <Card className="mypage-user-info">
        {/* 제목 */}
        <Title level={3} className="member-edit-title">
          회원정보 수정
        </Title>

        {/* 프로필 / 회원등급 */}
        <div className="mypage-edit-profile">
          {/* 프로필 */}
          <div className="mypage-profile-upload">
            <Avatar
              size={110}
              src={profilePreview}
              icon={!profilePreview && <UserOutlined />}
            />

            <Upload
              showUploadList={false}
              beforeUpload={() => false}
              onChange={handleProfileChange}
              accept="image/*"
            >
              <Button icon={<CameraOutlined />} style={{ marginTop: 14 }}>
                프로필 변경
              </Button>
            </Upload>
          </div>

          {/* 회원등급 */}
          <div className="mypage-grade-box">
            <Text type="secondary">회원등급</Text>

            <div className="mypage-grade">{user.grade}</div>
          </div>
        </div>

        {/* 회원정보 */}
        <Form
          form={form}
          layout="vertical"
          initialValues={{
            loginId: user.loginId,
            nickname: user.nickname,
            email: user.email,
            mobile: user.mobile,
            gender: user.gender,
            birth: user.birth ? moment(user.birth, 'YYYY-MM-DD') : null,
            interests: user.interests,
          }}
          onFinish={handleSubmit}
        >
          <Row gutter={[20, 0]}>
            {/* 아이디 */}
            <Col xs={24} md={12}>
              <Form.Item label="아이디" name="loginId">
                <Input disabled />
              </Form.Item>
            </Col>

            {/* 닉네임 */}
            <Col xs={24} md={12}>
              <Form.Item label="닉네임" name="nickname">
                <Input placeholder="닉네임을 입력하세요." />
              </Form.Item>
            </Col>

            {/* 이메일 */}
            <Col xs={24} md={12}>
              <Form.Item label="이메일" name="email">
                <Input type="email" placeholder="이메일을 입력하세요." />
              </Form.Item>
            </Col>

            {/* 전화번호 */}
            <Col xs={24} md={12}>
              <Form.Item label="전화번호" name="mobile">
                <Input maxLength={11} placeholder="전화번호를 입력하세요." />
              </Form.Item>
            </Col>

            {/* 성별 */}
            <Col xs={24} md={12}>
              <Form.Item label="성별" name="gender">
                <Radio.Group>
                  <Radio value="M">남성</Radio>
                  <Radio value="F">여성</Radio>
                  <Radio value="N">비공개</Radio>
                </Radio.Group>
              </Form.Item>
            </Col>

            {/* 생년월일 */}
            <Col xs={24} md={12}>
              <Form.Item label="생년월일" name="birth">
                <DatePicker
                  style={{ width: '100%' }}
                  placeholder="생년월일을 선택하세요"
                />
              </Form.Item>
            </Col>

            {/* 관심사 */}
            <Col span={24}>
              <Form.Item label="관심사" name="interests">
                <Select
                  mode="multiple"
                  placeholder="관심사를 선택해주세요."
                  options={interests}
                />
              </Form.Item>
            </Col>
          </Row>

          {/* 버튼 */}
          <div className="mypage-btn-group">
            <Button onClick={() => window.history.back()}>취소</Button>

            <Button type="primary" htmlType="submit">
              저장하기
            </Button>
          </div>
        </Form>
      </Card>
    </div>
  );
}

export default UserMyMemberEditPage;
