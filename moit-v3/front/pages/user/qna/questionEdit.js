import React, { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import { useDispatch, useSelector } from 'react-redux';
import {
  qnaDetailRequest,
  qnaUpdateRequest,
} from '../../../reducers/qnaReducer';

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

function questionEdit() {
  const router = useRouter();
  const dispatch = useDispatch();

  const { question } = router.query;

  const { qna, loading, success, error } = useSelector(
    (state) => state.qna
  );

  const [form] = Form.useForm();
  const [submitted, setSubmitted] = useState(false);

  useEffect(() => {
    if (!router.isReady || !question) return;

    dispatch(
      qnaDetailRequest(Number(question))
    );
  }, [router.isReady, question, dispatch]);

  useEffect(() => {
    if (!qna) return;

    form.setFieldsValue({
      title: qna.title || '',
      content: qna.content || '',
      isPublic: qna.isPublic === 'N',
    });
  }, [qna, form]);

  useEffect(() => {
    if (!submitted || !success) return;

    router.push(
      `/user/qna/questionDetail?questionId=${question}`
    );
  }, [
    submitted,
    success,
    router,
    question,
  ]);

  useEffect(() => {
    if (error) {
      setSubmitted(false);
      alert(error);
    }
  }, [error]);

  const isMeetup = qna?.category === 'MEETUP';

  const title = isMeetup ? '모임 1:1 문의 수정' : '관리자 1:1 문의 수정';

  const handleSubmit = (values) => {
    if (!question) return;
    if (!window.confirm('문의 내용을 수정하시겠습니까?')) {
      return;
    }
    setSubmitted(true);

    dispatch(
      qnaUpdateRequest({
        questionId: Number(question),
        data: {
          title: values.title,
          content: values.content,
          isPublic: values.isPublic ? 'N' : 'Y',
        },
      })
    );
  };

  const handleCancel = () => {
    router.back();
  };

  return (
    <div className="qna-write-page">
      <div className="qna-write-header">

        <Title
          level={2}
          className="qna-write-title"
        >
          {title}
        </Title>

        <Text type="secondary">
          문의 내용을 수정할 수 있습니다.
        </Text>

      </div>

      <Card className="qna-write-card">

        <Title level={4}>
          문의 정보 수정
        </Title>

        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
        >

          <Form.Item
            label="제목"
            name="title"
            rules={[
              {
                required: true,
                message: '제목을 입력해주세요.',
              },
            ]}
          >
            <Input
              size="large"
              placeholder="제목을 입력하세요."
            />
          </Form.Item>

          <Form.Item
            label="문의 내용"
            name="content"
            rules={[
              {
                required: true,
                message: '문의 내용을 입력해주세요.',
              },
            ]}
          >
            <TextArea
              rows={10}
              placeholder="문의 내용을 입력하세요."
              maxLength={1000}
              showCount
            />
          </Form.Item>

          <Form.Item
            name="isPublic"
            valuePropName="checked"
          >
            <Checkbox>
              비공개 문의
            </Checkbox>
          </Form.Item>

          <div className="qna-write-actions">

            <Space>

              <Button
                type="primary"
                htmlType="submit"
                loading={loading && submitted}
              >
                수정하기
              </Button>

              <Button onClick={handleCancel}>
                취소
              </Button>

            </Space>

          </div>

        </Form>

      </Card>

    </div>
  );
}

export default questionEdit;