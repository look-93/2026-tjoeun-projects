import React from 'react';
import { useRouter } from 'next/router';
import { useDispatch } from 'react-redux';
import { qnaCreateRequest } from '../../../reducers/qnaReducer';
import {
  Breadcrumb,
  Button,
  Card,
  Checkbox,
  Form,
  Input,
  Space,
  Typography,
} from 'antd';

const { Title, Text } = Typography;
const { TextArea } = Input;

function questionWrite() {
  const router = useRouter();
  const dispatch = useDispatch();

  const { type, meetupId } = router.query;

  const isMeetup = type === 'MEETUP';

  const title = isMeetup ? '모임 1:1 문의 등록' : '관리자 1:1 문의 등록';

  const breadcrumbTitle = isMeetup ? '모임글 1:1 문의' : '관리자 1:1 문의';

  return (
    <div className="qna-write-page">
      <div className="qna-write-header">
        <Breadcrumb
          items={[{ title: breadcrumbTitle }, { title: '문의 등록' }]}
        />

        <Title level={2} className="qna-write-title">
          {title}
        </Title>

        <Text type="secondary">문의 내용을 작성해주세요.</Text>
      </div>

      <Card className="qna-write-card">
        <Title level={4}>문의 정보 입력</Title>

        <Form layout="vertical"
            onFinish={(values) => {
              dispatch( qnaCreateRequest({ ...values, parentId: Number(meetupId), category: 'MEETUP',
                isPublic: values.isPublic ? 'N' : 'Y',
              }) );
            }}
          >
          <Form.Item label="제목" name="title">
            <Input size="large" placeholder="제목을 입력하세요." />
          </Form.Item>

          <Form.Item label="문의 내용" name="content">
            <TextArea rows={10} placeholder="문의 내용을 입력하세요." />
          </Form.Item>

          <Form.Item name="isPublic" valuePropName="checked">
            <Checkbox>비공개 문의</Checkbox>
          </Form.Item>

          <div className="qna-write-actions">
            <Space>
              <Button type="primary" htmlType="submit">등록하기</Button>
              <Button onClick={() => router.back()}>취소</Button>
            </Space>
          </div>
        </Form>
      </Card>
    </div>
  );
}

export default questionWrite;
