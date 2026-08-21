import React, { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useDispatch, useSelector } from 'react-redux';
import {
  qnaDetailRequest,
  qnaDeleteRequest,
  qnaAnswerDeleteRequest,
  qnaSatisfactionRequest,
} from '../../../reducers/qnaReducer';

import {
  fetchMeetupDetailRequest,
} from '../../../reducers/meetupReducer';

import {
  Breadcrumb,
  Button,
  Card,
  Descriptions,
  Divider,
  Space,
  Tag,
  Typography,
} from 'antd';

const { Title, Text, Paragraph } = Typography;

function questionDetail() {
  const router = useRouter();
  const dispatch = useDispatch();

  const { questionId } = router.query;
  const { qna, deleteSuccess, answerDeleteSuccess, error } = useSelector((state) => state.qna);

  const { user } = useSelector((state) => state.user);
  const { meetup } = useSelector((state) => state.meetup);

  // 상세 조회
  useEffect(() => {
    if (!router.isReady || !questionId) return;
    dispatch(qnaDetailRequest(Number(questionId)));
  }, [router.isReady, questionId, dispatch]);

  // 모임 문의인 경우 모임 상세 조회
  useEffect(() => {
    if (!qna?.parentId) return;
    if (qna?.category !== 'MEETUP') return;

    dispatch(
      fetchMeetupDetailRequest(Number(qna.parentId))
    );
  }, [qna?.parentId, qna?.category, dispatch]);
  
  // 삭제 성공 시 알림 후 목록 이동
  useEffect(() => {
    if (!deleteSuccess) return;

    alert('문의가 삭제되었습니다.');
    router.push('/user/mypage/question');
  }, [deleteSuccess, router]);

  // 답변 삭제 성공
  useEffect(() => {
    if (!answerDeleteSuccess) return;

    alert('답변이 삭제되었습니다.');
    dispatch(qnaDetailRequest(Number(questionId)));
  }, [answerDeleteSuccess, questionId, dispatch]);

  useEffect(() => {
    if (error) { alert(error); }
  }, [error]);

  const isMeetup = qna?.category === 'MEETUP';

  // 답변 등록/수정/삭제 권한
  const canAnswer =
    user?.memberTypeId === 3 ||
    user?.memberTypeId === 4 ||
    (
      user?.memberTypeId === 1 &&
      meetup?.memberId === user?.memberId
    );

  const title = isMeetup ? '모임 1:1 문의 상세' : '관리자 1:1 문의 상세';

  const status = qna?.qnaStatus || 'PENDING';

  const statusText = status === 'ANSWERED'
    ? '답변완료'
    : '답변대기';

  const statusColor = status === 'ANSWERED'
    ? 'green'
    : 'orange';

  const publicText = qna?.isPublic === 'N'
    ? '비공개 🔒'
    : '공개';

  const createdAt = qna?.createdAt
    ? new Date(qna.createdAt).toLocaleString()
    : '-';

  const answer = qna?.answer || {};

  const answerCreatedAt = answer.createdAt
    ? new Date(answer.createdAt).toLocaleString()
    : '-';

  const handleDelete = () => {
    if (!questionId) return;
    if (!window.confirm('문의를 삭제하시겠습니까?')) return;
    dispatch(qnaDeleteRequest(Number(questionId)));
  };

  const handleAnswerDelete = () => {
    if (!qna?.questionId || !answer.answerId) return;
    if (!window.confirm('답변을 삭제하시겠습니까?')) return;
    dispatch(
      qnaAnswerDeleteRequest({
        questionId: Number(qna.questionId),
        answerId: Number(answer.answerId),
      })
    );
  };

  const handleSatisfaction = (score) => {
    if (!answer.answerId) return;
    if (answer.satisfaction) {
      if (!window.confirm('이미 만족도 평가를 하셨습니다. 다시 평가하시겠습니까?')) {
        return;
      }
    }

    dispatch(
      qnaSatisfactionRequest({
        answerId: Number(answer.answerId),
        data: {satisfaction: score,},
      })
    );
    alert(`${score}점으로 평가되었습니다.`);
  };

  return (
    <div className="qna-write-page">
      <div className="qna-write-header">

        <Title level={2} className="qna-write-title">
          {title}
        </Title>

        <Text type="secondary">
          문의 내용을 확인할 수 있습니다.
        </Text>

      </div>
        <div className="qna-write-actions">
          <Space>

              {status === 'PENDING' && canAnswer && (
                <Button type="primary" onClick={() =>
                    router.push(`/user/qna/answerWrite?questionId=${qna?.questionId}`)}>
                  답변 등록
                </Button>
              )}
            
            <Button onClick={() => router.push(`/user/qna/questionEdit?questionId=${qna?.questionId}`)}>
              수정
            </Button>

            <Button danger onClick={handleDelete}>
              삭제
            </Button>
          </Space>
        </div>

      <Card className="qna-write-card">
        <Title level={4}>
          문의 정보
        </Title>

        <Descriptions
          bordered
          column={1}
          size="middle"
        >
          <Descriptions.Item label="제목">
            {qna?.title || '-'}
          </Descriptions.Item>

          <Descriptions.Item label="작성자">
            {qna?.nickname || qna?.memberId || '-'}
          </Descriptions.Item>

          <Descriptions.Item label="등록일">
            {createdAt}
          </Descriptions.Item>

          <Descriptions.Item label="문의 상태">
            <Tag color={statusColor}>
              {statusText}
            </Tag>
          </Descriptions.Item>

          <Descriptions.Item label="비공개 여부">
            {publicText}
          </Descriptions.Item>
        </Descriptions>

        <Divider />

        <Title level={5}>
          문의 내용
        </Title>

        <Card
          size="small"
          className="qna-detail-content"
        >
          <Paragraph
            style={{
              whiteSpace: 'pre-wrap',
              marginBottom: 0,
              minHeight: 180,
            }}
          >
            {qna?.content || '-'}
          </Paragraph>
        </Card>

      </Card>

      <Card
        className="qna-write-card"
        style={{ marginTop: 16 }}
      >
        <Title level={4}>
          답변 정보
        </Title>

        {status === 'ANSWERED' && qna?.answer ? (
          <>
            <Descriptions
              bordered
              column={1}
              size="middle"
            >
              <Descriptions.Item label="답변자">
                {answer.nickname || answer.memberId || '-'}
              </Descriptions.Item>

              <Descriptions.Item label="답변 등록일">
                {answerCreatedAt}
              </Descriptions.Item>
            </Descriptions>

            <Divider />

            <Title level={5}>
              답변 내용
            </Title>

            <Card
              size="small"
              className="qna-detail-content"
            >
              <Paragraph
                style={{
                  whiteSpace: 'pre-wrap',
                  marginBottom: 0,
                  minHeight: 150,
                }}
              >
                {answer.content || '-'}
              </Paragraph>
            </Card>

            <div className="qna-write-actions">
              <Space>
                {canAnswer && (
                  <Button
                    type="primary"
                    onClick={() =>
                      router.push(
                        `/user/qna/answerEdit?answerId=${answer.answerId}&questionId=${qna?.questionId}`
                      )
                    }
                  >
                    답변 수정
                  </Button>
                )}

                {canAnswer && (
                  <Button danger onClick={handleAnswerDelete}>
                    답변 삭제
                  </Button>
                )}
              </Space>
            </div>

            <Divider />

            <Title level={5}>
              답변 만족도
            </Title>

            <Text type="secondary">
              답변이 도움이 되었나요?
            </Text>

            <div style={{ marginTop: 12 }}>
              <Space>
                <Button
                  type={answer.satisfaction === 1 ? 'primary' : 'default'}
                  onClick={() => handleSatisfaction(1)}
                >
                  1점
                </Button>

                <Button
                  type={answer.satisfaction === 2 ? 'primary' : 'default'}
                  onClick={() => handleSatisfaction(2)}
                >
                  2점
                </Button>

                <Button
                  type={answer.satisfaction === 3 ? 'primary' : 'default'}
                  onClick={() => handleSatisfaction(3)}
                >
                  3점
                </Button>

                <Button
                  type={answer.satisfaction === 4 ? 'primary' : 'default'}
                  onClick={() => handleSatisfaction(4)}
                >
                  4점
                </Button>

                <Button
                  type={answer.satisfaction === 5 ? 'primary' : 'default'}
                  onClick={() => handleSatisfaction(5)}
                >
                  5점
                </Button>
              </Space>
            </div>
          </>
        ) : (
          <>
            <Text type="secondary">
              아직 등록된 답변이 없습니다.
            </Text>

          </>
        )}
      </Card>
    </div>
  );
}

export default questionDetail;