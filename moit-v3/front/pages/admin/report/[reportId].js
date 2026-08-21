// pages/admin/report/[reportId].js
// 관리자 신고 상세 페이지
// 신고 상세 조회 + 승인/반려 + 삭제

import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useRouter } from 'next/router';
import {
    fetchReportsDetailRequest,
    deleteReportRequest,
    deleteReportSuccess,
    resetReportState,
} from '../../../../reducers/reportReducer';
import {
    Card, Radio, Input, Button, Typography, Space, Divider, message,
    Descriptions, Tag, Modal, Spin
} from 'antd';

const { Title } = Typography;

function ReportDetailPage() {
    const router = useRouter();
    const dispatch = useDispatch();

    const { reportId } = router.query; // 동적라우팅
    const {
        currentReport,
        fetchDetail,
        delete: deleteState
    } = useSelector((state) => state.report);



    // --- 신고 상세 조회 ---
    useEffect(() => {
        if (!router.isReady) {
            return;
        }
        dispatch( fetchReportsDetailRequest({reportId: Number(reportId)}) );
    }, [router.isReady, reportId]);
    
    // --- 오류 ---
    useEffect(() => {
        if (fetchDetail.error) {
            message.error(fetchDetail.error);
        }
    }, [fetchDetail.error]);
    
    // --- 신고 삭제 성공 ---
    useEffect(() => {
        if (deleteState.success) {
            message.success('신고 내역이 삭제되었습니다.');
            router.push('/user/meetup/report');
        }
    }, [deleteState.success, router]);
    
    
    // --- 신고 대상 한글 표시 ---
    const getTargetTypeText = (targetType) => {
        if (targetType === 'MEETUP') {
            return '모임';
        }
        if (targetType === 'REVIEW') {
            return '후기';
        }
    };

    // --- 신고 사유 한글 표시 ---
    const getReasonCodeText = (reasonCode)=> {
        switch (reasonCode) {
            case 'ABUSE':
                return '욕설/비방';

            case 'SPAM':
                return '도배/스팸';

            case 'FAKE_INFO':
                return '허위 정보';

            case 'AD':
                return '광고성 게시물';

            case 'NOSHOW':
                return '노쇼';

            default:
                return '기타';
        }
    };

    // 처리 상태
    const getStatusTag = (status) => {
        if (status === 'PENDING') {
            return (
                <Tag color="orange">
                    처리 대기
                </Tag>
            );
        }
        if (status === 'APPROVED') {
            return (
                <Tag color="green">
                    승인
                </Tag>
            );
        }
        if (status === 'REJECTED') {
            return (
                <Tag color="red">
                    반려
                </Tag>
            );
        }
    };

    // 뱃지
    const getStatusCodeTag = (statusCode) => {
        if (statusCode === 'ACTIVE') {
            return (
                <Tag color="green">
                    정상
                </Tag>
            );
        }
        if (statusCode === 'WARNING') {
            return (
                <Tag color="orange">
                    주의
                </Tag>
            );
        }
        if (statusCode === 'DANGER') {
            return (
                <Tag color="red">
                    위험
                </Tag>
            );
        }
    };


    //////////////////////////////////////////////////////
    // 해당 신고 대상 글 보기
    const handleTargetView = () => {
        // 모임 신고
        if (currentReport.targetType === 'MEETUP') {
            router.push(
                `/user/meetup/detail?meetupId=${currentReport.targetId}`
            );
            return;
        }

        // 리뷰 신고
        if (currentReport.targetType === 'REVIEW') {
            router.push(
                `/user/meetup/review/detail?reviewId=${currentReport.targetId}`
            );
        }
    };

    // 신고 수정 페이지 이동
    const handleUpdate = () => {
        router.push(
            `/user/meetup/report/edit/${reportId}`
        );
    };
    
    // 신고 삭제 요청
    const handleDelete = () => {
        Modal.confirm({
            title: '신고 내역을 삭제하시겠습니까?',
            content: '삭제 후에는 신고 내역을 확인할 수 없습니다.',
            okText: '삭제',
            cancelText: '취소',

            okButtonProps: {
                danger: true
            },
            onOk: ()=> {
                dispatch(
                    deleteReportRequest({
                        reportId: Number(reportId)
                    })
                );
            }
        });
    };
    
    // 로딩
    if (fetchDetail.loading || !currentReport) {
        return (
            <Spin size="large" />
        );
    }

    return (
        <div className="report-detail-page">
            <Card>
                <Title level={2}>
                    신고 상세보기
                </Title>

                <Descriptions
                    bordered
                    column={1}
                >
                    {/* 신고 번호 */}
                    <Descriptions.Item label="신고번호">
                        {currentReport.reportId}번 신고글
                    </Descriptions.Item>

                    <Descriptions.Item label="신고자">
                        {currentReport.memberNickname}
                    </Descriptions.Item>

                    <Descriptions.Item label="신뢰도점수 & 뱃지">
                        {currentReport.trustScore}점
                        {' '}
                        {getStatusCodeTag(
                            currentReport.statusCode
                        )}
                    </Descriptions.Item>

                    {/* 신고 대상 & 신고 대상 ID */}
                    <Descriptions.Item label="신고 대상 ID">
                        {getTargetTypeText(
                            currentReport.targetType
                        )}
                        {' '}
                        {currentReport.targetId}번 게시글
                    </Descriptions.Item>

                    {/* 신고 사유 */}
                    <Descriptions.Item label="신고 사유">
                        {getReasonCodeText(
                            currentReport.reasonCode
                        )}
                    </Descriptions.Item>

                    {/* 신고 상세 내용 */}
                    <Descriptions.Item label="상세 내용">
                        {
                            currentReport.reasonDetail
                                ? currentReport.reasonDetail
                                : '작성된 상세 내용이 없습니다.'
                        }
                    </Descriptions.Item>

                    {/* 신고 처리 상태 */}
                    <Descriptions.Item label="처리 상태">
                        {getStatusTag(
                            currentReport.status
                        )}
                    </Descriptions.Item>

                    {/* 신고 작성일 */}
                    <Descriptions.Item label="신고일">
                        {/* {currentReport.createdAt} */}
                        {currentReport.createdAt?.slice(0, 10)}
                    </Descriptions.Item>

                    {/* 수정일이 있을 때만 표시 */}
                    {
                        currentReport.updatedAt && (
                            <Descriptions.Item label="수정일자">
                                {/* {currentReport.updatedAt} */}
                                {currentReport.updatedAt?.slice(0, 10)}
                            </Descriptions.Item>
                        )
                    }
                </Descriptions>


                <Space style={{marginTop:20}}>
                    {/* 신고 목록 */}
                    <Button
                        onClick={() =>
                            router.push('/user/meetup/report')
                        }
                    >
                        목록
                    </Button>

                    {/* 신고당한 원본 글 */}
                    <Button
                        onClick={handleTargetView}
                    >
                        해당 글 보기
                    </Button>

                    {/* PENDING일 때만 수정 가능 */}
                    {
                        currentReport.status === 'PENDING' && (
                            <Space>
                                <Button type="primary" onClick={handleUpdate}>수정</Button>
                                <Button type="danger" onClick={handleDelete}>삭제</Button>
                            </Space>
                        )
                    }

                  

                </Space>
            </Card>
        </div>
    );
}

export default ReportDetailPage;