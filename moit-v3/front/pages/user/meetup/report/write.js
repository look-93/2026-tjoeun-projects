import React, { useState } from 'react';
import { useRouter } from 'next/router';
import { Card, Radio, Input, Button, Typography, Space, Divider } from 'antd';

const { Title, Text } = Typography;
const { TextArea } = Input;

function ReportWritePage() {
  const router = useRouter();

  const { type, targetId } = router.query;

  const [reasonCode, setReasonCode] = useState(null);
  const [keywords, setKeywords] = useState('');
  const [reasonDetail, setReasonDetail] = useState('');

  const isMeetup = type === 'MEETUP';

  const title = isMeetup ? '모임 신고하기' : '후기 신고하기';

  const reasons = [
    { value: 'ABUSE', label: '욕설/비방' },
    { value: 'SPAM', label: '도배/스팸' },
    { value: 'FAKE_INFO', label: '허위 정보' },
    { value: 'AD', label: '광고성 게시물' },
    { value: 'NOSHOW', label: '노쇼' },
    { value: 'ETC', label: '기타' },
  ];

  return (
    <div className="report-write-page">
      <Card className="report-write-card">
        <Title level={2}>{title}</Title>

        <Text type="secondary">신고 사유를 선택하고 내용을 작성해주세요.</Text>

        <Divider />

        <div className="report-write-field">
          <Title level={5}>
            신고 사유 <span className="report-required">(필수)</span>
          </Title>

          <Radio.Group
            value={reasonCode}
            onChange={(e) => setReasonCode(e.target.value)}
          >
            <Space direction="vertical">
              {reasons.map((reason) => (
                <Radio key={reason.value} value={reason.value}>
                  {reason.label}
                </Radio>
              ))}
            </Space>
          </Radio.Group>
        </div>

        <div className="report-write-field">
          <Title level={5}>
            AI 신고 내용 작성 <Text type="secondary">(선택)</Text>
          </Title>

          <TextArea
            value={keywords}
            onChange={(e) => setKeywords(e.target.value)}
            maxLength={150}
            showCount
            placeholder="키워드 예시) 욕설, 반복적인 비방, 불쾌한 표현"
            rows={4}
          />

          <Button
            type="primary"
            style={{ marginTop: 10 }}
            disabled={!reasonCode || !keywords.trim()}
          >
            키워드 작성
          </Button>
        </div>

        <div className="report-write-field">
          <Title level={5}>
            상세 내용 <Text type="secondary">(선택)</Text>
          </Title>

          <TextArea
            value={reasonDetail}
            onChange={(e) => setReasonDetail(e.target.value)}
            maxLength={200}
            showCount
            rows={6}
            placeholder="신고 내용을 자세히 입력해주세요."
          />
        </div>

        <div className="report-write-actions">
          <Space>
            <Button onClick={() => router.back()}>취소</Button>

            <Button type="primary">신고 등록</Button>
          </Space>
        </div>
      </Card>
    </div>
  );
}

export default ReportWritePage;
