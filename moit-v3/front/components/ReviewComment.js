import React, { useState, useEffect } from 'react';
import { Button, Input, Spin } from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import { getCommentsRequest, createCommentRequest, deleteCommentRequest } from '../reducers/reviewReducer';

export default function ReviewComments({ reviewId }) {
  const dispatch = useDispatch();
  const [commentText, setCommentText] = useState('');
  const [replyTo, setReplyTo] = useState(null);
  const [replyText, setReplyText] = useState('');

  // Redux에서 해당 리뷰의 댓글 상태 및 에러 상태 가져오기
  // 1. 가져올 때도 소문자 commentError
  const { comments, commentLoading, commentError } = useSelector((state) => {
    const reviewState = state.review || state.reviewReducer || {};
    const safeId = Number(reviewId);
    
    return {
      comments: reviewState.commentsMap?.[reviewId] || reviewState.commentsMap?.[safeId] || [],
      commentLoading: reviewState.commentLoadingMap?.[reviewId] ?? reviewState.commentLoadingMap?.[safeId] ?? reviewState.commentLoading ?? false,
      commentError: reviewState.commentError, // 👈 여기
    };
  });

  // 2. 사용할 때도 소문자 commentError
  useEffect(() => {
    if (commentError) {
      alert(commentError); // 👈 여기도 소문자로 변경!
    }
  }, [commentError]); // 👈 여기도 소문자로 변경!

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

    // 댓글 등록 직후 목록을 다시 불러옵니다.
    setTimeout(() => {
      dispatch(getCommentsRequest(reviewId));
    }, 200);
  };

  const handleDeleteComment = (commentId) => {
    if (window.confirm('정말 이 댓글을 삭제하시겠습니까?')) {
      dispatch(
        deleteCommentRequest({
          commentId,
          reviewId,
        })
      );
    }
  };

  return (
    <div style={{ marginTop: 16, borderTop: '1px solid #f0f0f0', paddingTop: 16 }}>
      <div style={{ fontSize: '14px', fontWeight: 'bold', marginBottom: 12 }}>💬 댓글</div>

      <div style={{ display: 'flex', gap: '8px', marginBottom: 16 }}>
        <Input
          placeholder="따뜻한 댓글을 남겨주세요"
          value={commentText}
          onChange={(e) => setCommentText(e.target.value)}
          onPressEnter={() => handleCommentSubmit(null)}
        />
        <Button type="primary" onClick={() => handleCommentSubmit(null)}>
          등록
        </Button>
      </div>

      {commentLoading ? (
        <div style={{ textAlign: 'center', padding: '10px' }}>
          <Spin size="small" />
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {comments && comments.length > 0 ? (
            comments.map((comment) => {
              const commentId = comment.id || comment.commentId;
              return (
                <div
                  key={commentId}
                  style={{
                    padding: '8px 12px',
                    background: '#fafafa',
                    borderRadius: '6px',
                  }}
                >
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <span style={{ fontSize: '13px', fontWeight: 'bold' }}>
                      {comment.memberNickname || comment.nickname || comment.member?.nickname || '익명'}
                    </span>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <span style={{ fontSize: '11px', color: '#8c8c8c' }}>
                        {comment.createdAt ? String(comment.createdAt).substring(0, 10) : ''}
                      </span>
                      <Button
                        type="text"
                        danger
                        size="small"
                        style={{ padding: 0, height: 'auto', fontSize: '11px' }}
                        onClick={() => handleDeleteComment(commentId)}
                      >
                        삭제
                      </Button>
                    </div>
                  </div>

                  <div style={{ margin: '4px 0', fontSize: '13px', wordBreak: 'break-all' }}>
                    {comment.content}
                  </div>

                  <Button
                    type="link"
                    size="small"
                    style={{ padding: 0, height: 'auto', fontSize: '12px' }}
                    onClick={() => setReplyTo(replyTo === commentId ? null : commentId)}
                  >
                    답글쓰기
                  </Button>

                  {replyTo === commentId && (
                    <div style={{ marginTop: 8, display: 'flex', gap: '8px' }}>
                      <Input
                        size="small"
                        placeholder="답글을 남겨주세요"
                        value={replyText}
                        onChange={(e) => setReplyText(e.target.value)}
                        onPressEnter={() => handleCommentSubmit(commentId)}
                      />
                      <Button size="small" type="primary" onClick={() => handleCommentSubmit(commentId)}>
                        등록
                      </Button>
                    </div>
                  )}

                  {/* 자식 대댓글(children) 재귀 표시 */}
                  {comment.children && comment.children.length > 0 && (
                    <div style={{ marginTop: 8, paddingLeft: 16, borderLeft: '2px solid #e8e8e8' }}>
                      {comment.children.map((child) => {
                        const childId = child.id || child.commentId;
                        return (
                          <div key={childId} style={{ marginTop: 8 }}>
                            <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                              <span style={{ fontSize: '12px', fontWeight: 'bold' }}>
                                {child.memberNickname || child.nickname || '익명'}
                              </span>
                              <Button
                                type="text"
                                danger
                                size="small"
                                style={{ padding: 0, height: 'auto', fontSize: '10px' }}
                                onClick={() => handleDeleteComment(childId)}
                              >
                                삭제
                              </Button>
                            </div>
                            <div style={{ fontSize: '12px', wordBreak: 'break-all' }}>{child.content}</div>
                          </div>
                        );
                      })}
                    </div>
                  )}
                </div>
              );
            })
          ) : (
            <div style={{ fontSize: '13px', color: '#8c8c8c' }}>
              등록된 댓글이 없습니다. 첫 댓글을 남겨보세요!
            </div>
          )}
        </div>
      )}
    </div>
  );
}