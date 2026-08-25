import React, { useState, useEffect } from 'react';
import { Row, Col, Button, Input, Typography, Space, Avatar, Spin, Paragraph } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import { useDispatch, useSelector } from 'react-redux';
import { getCommentsRequest, createCommentRequest } from '../reducers/reviewReducer';

const { Title, Text } = Typography;

export default function ReviewComments({ reviewId }) {
  const dispatch = useDispatch();
  const [commentText, setCommentText] = useState('');
  const [replyTo, setReplyTo] = useState(null);
  const [replyText, setReplyText] = useState('');

  // Redux에서 해당 리뷰의 댓글 상태 가져오기
  const { comments, commentLoading } = useSelector((state) => {
    const reviewState = state.review || state.reviewReducer || {};
    return {
      comments: reviewState.commentsMap?.[reviewId] || [],
      commentLoading: reviewState.commentLoadingMap?.[reviewId] || false,
    };
  });

  // 컴포넌트가 마운트되거나 새로고침될 때 댓글 목록 불러오기
  useEffect(() => {
    if (reviewId) {
      dispatch(getCommentsRequest(reviewId));
    }
  }, [dispatch, reviewId]);

  const handleCommentSubmit = (parentCommentId = null) => {
    const content = parentCommentId ? replyText : commentText;
    if (!content || !content.trim()) {
      alert('댓글 내용을 입력해주세요.');
      return;
    }

    dispatch(
      createCommentRequest({
        reviewId,
        content: content.trim(),
        parentCommentId,
      })
    );

    if (parentCommentId) {
      setReplyText('');
      setReplyTo(null);
    } else {
      setCommentText('');
    }
  };

  return (
    <div style={{ marginTop: 16, borderTop: '1px solid #f0f0f0', paddingTop: 16 }}>
      <Title level={5} style={{ fontSize: '14px', marginBottom: 12 }}>
        💬 댓글
      </Title>

      <Space.Compact style={{ width: '100%', marginBottom: 16 }}>
        <Input
          placeholder="따뜻한 댓글을 남겨주세요"
          value={commentText}
          onChange={(e) => setCommentText(e.target.value)}
          onPressEnter={() => handleCommentSubmit(null)}
        />
        <Button type="primary" onClick={() => handleCommentSubmit(null)}>
          등록
        </Button>
      </Space.Compact>

      {commentLoading ? (
        <div style={{ textAlign: 'center', padding: '10px' }}>
          <Spin size="small" />
        </div>
      ) : (
        <Space direction="vertical" size={12} style={{ width: '100%' }}>
          {comments && comments.length > 0 ? (
            comments.map((comment) => (
              <div
                key={comment.id || comment.commentId}
                style={{
                  padding: '8px 12px',
                  background: '#fafafa',
                  borderRadius: '6px',
                  marginLeft: comment.parentCommentId ? '24px' : '0px',
                }}
              >
                <Row justify="space-between" align="middle">
                  <Col>
                    <Text strong style={{ fontSize: '13px' }}>
                      {comment.memberNickname || comment.nickname || '익명'}
                    </Text>
                  </Col>
                  <Col>
                    <Text type="secondary" style={{ fontSize: '11px' }}>
                      {comment.createdAt ? String(comment.createdAt).substring(0, 10) : ''}
                    </Text>
                  </Col>
                </Row>
                
                {/* 댓글 내용 */}
                <Paragraph style={{ margin: '4px 0', fontSize: '13px', wordBreak: 'break-all' }}>
                  {comment.content}
                </Paragraph>

                {!comment.parentCommentId && (
                  <Button
                    type="link"
                    size="small"
                    style={{ padding: 0, height: 'auto', fontSize: '12px' }}
                    onClick={() => setReplyTo(replyTo === comment.id ? null : comment.id)}
                  >
                    답글쓰기
                  </Button>
                )}

                {replyTo === comment.id && (
                  <div style={{ marginTop: 8, display: 'flex', gap: '8px' }}>
                    <Input
                      size="small"
                      placeholder="답글을 남겨주세요"
                      value={replyText}
                      onChange={(e) => setReplyText(e.target.value)}
                      onPressEnter={() => handleCommentSubmit(comment.id)}
                    />
                    <Button size="small" type="primary" onClick={() => handleCommentSubmit(comment.id)}>
                      등록
                    </Button>
                  </div>
                )}
              </div>
            ))
          ) : (
            <Text type="secondary" style={{ fontSize: '13px' }}>
              등록된 댓글이 없습니다. 첫 댓글을 남겨보세요!
            </Text>
          )}
        </Space>
      )}
    </div>
  );
}