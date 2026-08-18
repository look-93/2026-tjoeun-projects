import React from 'react';
import { useRouter } from 'next/router';
import {
  Breadcrumb,
  Button,
  Card,
  Divider,
  Space,
  Tag,
  Typography,
} from 'antd';

const { Title, Text } = Typography;

function questionDetail() {
  const router = useRouter();

  return (
    <div className="qna-write-page">

      <div className="qna-write-header">

        <Breadcrumb
          items={[
            { title: '모임글 1:1 문의' },
            { title: '문의 상세' },
          ]}
        />

        <Title level={2} className="qna-write-title">
          모임 1:1 문의
        </Title>

        <Text type="secondary">
          문의 내용을 확인해주세요.
        </Text>

      </div>


      <Card className="qna-write-card">

        <Title level={4}>
          문의 정보
        </Title>


        {/* 제목 */}
        <div className="qna-detail-field">

          <Text className="qna-detail-label">
            제목
          </Text>

          <div className="qna-detail-input">
          </div>

        </div>


        {/* 작성자 */}
        <div className="qna-detail-info">

          <Text className="qna-detail-label">
            작성자
          </Text>

          <div className="qna-detail-text">
          </div>

        </div>


        {/* 작성일 */}
        <div className="qna-detail-info">

          <Text className="qna-detail-label">
            작성일
          </Text>

          <div className="qna-detail-text">
          </div>

        </div>


        {/* 상태 */}
        <div className="qna-detail-info">

          <Text className="qna-detail-label">
            상태
          </Text>

          <div className="qna-detail-text">
            <Tag>
            </Tag>
          </div>

        </div>


        <Divider />


        {/* 문의 내용 */}
        <div className="qna-detail-content">

          <Text className="qna-detail-label">
            문의 내용
          </Text>

          <div className="qna-detail-textarea">
          </div>

        </div>


        <Divider />


        {/* 답변 */}
        <div className="qna-answer-section">

          <Title level={4}>
            답변
          </Title>

          <div className="qna-answer-textarea">
          </div>

        </div>


        {/* 버튼 */}
        <div className="qna-write-actions">

          <Space>

            <Button
              onClick={() => router.back()}
            >
              목록으로
            </Button>

          </Space>

        </div>

      </Card>

    </div>
  );
}

export default questionDetail;