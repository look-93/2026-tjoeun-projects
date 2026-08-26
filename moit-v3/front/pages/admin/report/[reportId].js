// pages/admin/report/[reportId].js
// 관리자 신고 상세 페이지
// 신고 상세 조회 + 승인/반려 + 삭제

import { useEffect, useState } from 'react';
// Redux에 액션 전송, Redux에 저장된 값을 꺼내오기
import { useDispatch, useSelector } from 'react-redux';
import { useRouter } from 'next/router';

import {
    fetchAdminReportsDetailRequest,
    updateAdminReportRequest,
    fetchMemberReportTrustInfoRequest,
    deleteAdminReportRequest,
    fetchAdminReportAuditLogsRequest,
    aiReportAnalysisRequest,
} from '../../../reducers/reportReducer';

import {
    Card, Input, Button, Typography, Space, Alert,
    message, Descriptions, Modal, Spin, Text
} from 'antd';

import ReportStatusTag from '../../../components/ReportStatusTag';
import ReportStatusCodeTag from '../../../components/ReportStatusCodeTag';

const { Title } = Typography;

function ReportDetailPage() {
    const router = useRouter();
    const dispatch = useDispatch();

    const [processReason, setProcessReason] = useState('');

    const { reportId } = router.query; // 동적라우팅

    const {
        currentReport,
        adminFetchDetail,
        adminUpdate,
        trustInfoFetch,     // 신뢰도 점수 조회
        adminDelete,

        auditLogs,
        auditLogFetch,

        aiAnalysis,         // // AI가 분석해서 보내준 최종 결과
        aiAnalysisLoading,
        aiAnalysisError,

    } = useSelector((state) => state.report);




    // --- 신고 상세 조회 ---
    useEffect(() => {
        if (!router.isReady) {
            return;
        }
        dispatch( fetchAdminReportsDetailRequest({reportId: Number(reportId)}) );
        dispatch( fetchAdminReportAuditLogsRequest({reportId: Number(reportId)}) );
    }, [router.isReady, dispatch, reportId]);

    // --- 페이지 진입 ---
    useEffect(() => {
        if (currentReport?.memberId) {
            dispatch(
                fetchMemberReportTrustInfoRequest({
                    targetMemberId: currentReport.memberId,
                })
            );
        }
    }, [currentReport?.memberId]);
    
    // --- 신고 처리 후 재조회 ---
    useEffect(() => {
        if (adminUpdate.success) {
            message.success('신고 처리가 완료되었습니다.');

            // 신고 상세 재조회
            dispatch(
                fetchAdminReportsDetailRequest({
                    reportId: router.query.reportId,
                })
            );

            // 신고당한 사람의 신뢰도점수 재조회
            if (currentReport?.memberId) {
                dispatch(
                    fetchMemberReportTrustInfoRequest({
                        targetMemberId: currentReport.memberId,
                    })
                );
            }

            // 관리자 처리 감사 로그 재조회
            dispatch(
                fetchAdminReportAuditLogsRequest({
                    reportId: Number(reportId),
                })
            );
        }
    }, [adminUpdate.success]);
    
    // --- 신고 삭제 성공 ---
    useEffect(() => {
        if (adminDelete.success) {
            message.success('신고 내역이 삭제되었습니다.');
            router.push('/admin/report');
        }
    }, [adminDelete.success, router]);
    
    // --- adminFetchDetail 오류 ---
    useEffect(() => {
        if (adminFetchDetail.error) {
            message.error(adminFetchDetail.error);
        }
    }, [adminFetchDetail.error]);

    // --- adminUpdate 오류 ---
    useEffect(() => {
        if (adminUpdate.error) {
            message.error(adminUpdate.error);
        }
    }, [adminUpdate.error]);
    






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
                // http://localhost:3000/user/meetup/review/detailreview?reviewId=10&meetupId=3
                `/user/meetup/review/detailreview?reviewId=${currentReport.targetId}&meetupId=${currentReport.meetupId}`
            );
        }
    };

    // 관리자 신고 AI 판단 보조 요청
    const handleAiAnalysis = () => {
        if (!reportId) { return; }
        dispatch( aiReportAnalysisRequest({reportId}) );
    };

    // 신고 수정 - 승인 (신뢰도점수/뱃지/처리상태 변경)
    const handleApproved = () => {
        if (!processReason.trim()) {
            message.warning('처리 사유를 입력해주세요.');
            return;
        }

        dispatch(
            updateAdminReportRequest({
                reportId: Number(reportId),
                processDto: {
                    status: 'APPROVED',
		            processReason: processReason,
                }
            })
        )
    };

    // 신고 수정 - 반려 (처리상태 변경)
    const handleRejected = () => {
        if (!processReason.trim()) {
            message.warning('처리 사유를 입력해주세요.');
            return;
        }

        dispatch(
            updateAdminReportRequest({
                reportId: Number(reportId),
                processDto: {
                    status: 'REJECTED',
                    processReason: processReason,
                }
            })
        )
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
                dispatch( deleteAdminReportRequest({reportId: Number(reportId)}) );
            }
        });
    };
    
    // 로딩
    if (adminFetchDetail.loading || !currentReport) {
        return (
            <Spin size="large" />
        );
    }

    if (!currentReport) {
        return <div>로딩중...</div>;
    }

    return (
        <div className="report-detail-page">
            <Card>
                <Title level={2}>관리자 신고 상세보기</Title>

                <Descriptions bordered column={1}>
                    {/* 신고 번호 */}
                    <Descriptions.Item label="신고번호 (reportId)">
                        {currentReport?.reportId}번 신고글
                    </Descriptions.Item>

                    <Descriptions.Item label="신고자 (memberId)">
                        {currentReport.memberNickname ?? '-'}
                        {' '}
                        ({currentReport.memberId ?? '-'}번)
                        {' => '}
                        {currentReport.trustScore}점{' '}
                        <ReportStatusCodeTag statusCode={currentReport.targetStatusCode} />
                    </Descriptions.Item>

                    <Descriptions.Item label="신고 대상 회원 (targetMemberId)">
                        {currentReport.targetMemberNickname ?? '-'}
                        {' '}
                        ({currentReport.targetMemberId ?? '-'}번)
                        {' => '}
                        {currentReport.targetTrustScore}점{' '}
                        <ReportStatusCodeTag statusCode={currentReport.targetStatusCode} />
                    </Descriptions.Item>

                    {/* 신고 대상 & 신고 대상 ID */}
                    <Descriptions.Item label="게시글 번호">
                        {getTargetTypeText(
                            currentReport.targetType
                        )}
                        ({currentReport.targetType})
                        {' '}
                        {currentReport.targetId}번 게시글
                    </Descriptions.Item>

                    {/* 신고 사유 */}
                    <Descriptions.Item label="신고 사유">
                        {getReasonCodeText(
                            currentReport.reasonCode
                        )}
                        ({currentReport.reasonCode})
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
                        <ReportStatusTag status={currentReport.status} />
                    </Descriptions.Item>

                    {/* 신고 작성일 */}
                    <Descriptions.Item label="신고일">
                        {/* {currentReport.createdAt} */}
                        {currentReport.createdAt?.replace('T', ' ').slice(0, 19)}
                    </Descriptions.Item>

                    {/* 수정일이 있을 때만 표시 */}
                    {
                        currentReport.userUpdatedAt && (
                            <Descriptions.Item label="수정일자">
                                {currentReport.userUpdatedAt?.replace('T', ' ').slice(0, 19)}
                            </Descriptions.Item>
                        )
                    }
                </Descriptions>

                {/* ============================== */}
                {/* AI 신고 판단 보조 */}
                {/* ============================== */}
                <Card title="AI 판단 보조" style={{ marginTop: 24 }}>
                    <Space
                        direction="vertical"
                        size="middle"
                        style={{ width: '100%' }}
                    >
                        <p>
                            신고 내용과 신고 대상 원문,
                            운영 기준 및 과거 유사 사례를 기반으로 분석합니다.
                        </p>

                        <span style={{color: '#888'}}>
                            ※ AI 결과는 참고용이며 최종 승인 및 반려 결정은 관리자가 수행합니다.
                        </span>

                        <Button
                            type="primary"
                            onClick={handleAiAnalysis}
                            loading={aiAnalysisLoading}
                            disabled={!reportId}
                        >
                            AI 판단 보조 요청
                        </Button>

                        {/* AI가 분석해서 보내준 최종 결과 Error */}
                        {aiAnalysisError && (
                            <Alert
                                type="error"
                                showIcon
                                message="AI 분석 실패"
                                description={aiAnalysisError}
                            />
                        )}

                        {/* AI가 분석해서 보내준 최종 결과가 있다면 */}
                        {aiAnalysis && (
                            <Card size="small" title="AI 분석 결과">
                                <div
                                    style={{
                                        whiteSpace: 'pre-wrap',
                                        wordBreak: 'break-word',
                                        lineHeight: 1.7,
                                    }}
                                >
                                    {aiAnalysis}
                                </div>
                            </Card>
                        )}
                    </Space>
                </Card>


                {/* 관리자 처리 사유 */}
                {currentReport.status === 'PENDING' && (
                    <div style={{ marginTop: 20 }}>
                        <Title level={5}>처리 사유</Title>

                        <Input.TextArea
                            rows={4}
                            placeholder="승인 또는 반려 사유를 입력하세요."
                            value={processReason}
                            onChange={(e) => {
                                setProcessReason(e.target.value);
                            }}
                        />
                    </div>
                )}


                <Space style={{marginTop:20}}>
                    {/* 신고 목록 */}
                    <Button onClick={() => router.push('/admin/report')}>
                        목록
                    </Button>

                    {/* 신고당한 원본 글 */}
                    <Button onClick={handleTargetView}>
                        해당 글 보기
                    </Button>

                    {/* PENDING일 때만 수정 가능 */}
                    {
                        currentReport.status === 'PENDING' && (
                            <Space>
                                <Button type="primary" onClick={handleApproved}>승인</Button>
                                <Button danger onClick={handleRejected}>반려</Button>
                                <Button type="danger" onClick={handleDelete}>삭제</Button>
                            </Space>
                        )
                    }
                </Space>
            </Card>



            <Card style={{ marginTop: 20 }}>
            <Title level={4}>관리자 처리 이력</Title>

            {auditLogFetch.loading ? (
                <Spin />
            ) : auditLogs && auditLogs.length > 0 ? (

                auditLogs.map((log) => (
                    <Descriptions
                        key={log.auditLogId}
                        bordered
                        column={1}
                        size="small"
                        style={{ marginBottom: 12 }}
                    >
                        <Descriptions.Item label="처리 일시">
                            {log.processedAt
                                ? log.processedAt.replace('T', ' ').slice(0, 19)
                                : '-'}
                        </Descriptions.Item>

                        <Descriptions.Item label="처리 관리자 (adminMemberId)">
                            {log.adminNickname || '-'}
                            {' '}
                            ({log.adminMemberId || '-'})
                        </Descriptions.Item>

                        <Descriptions.Item label="처리 상태">
                            <Space>
                                <ReportStatusTag status={log.previousStatus} />
                                <span> → </span>
                                <ReportStatusTag status={log.changedStatus} />
                            </Space>
                        </Descriptions.Item>

                        <Descriptions.Item label="관리자 처리 사유">
                            {log.processReason || '-'}
                        </Descriptions.Item>

                        <Descriptions.Item label="신뢰도 점수 변동">
                            {log.trustScoreChange != null
                                ? `${log.trustScoreChange > 0 ? '+' : ''}${log.trustScoreChange}점`
                                : '-'}
                        </Descriptions.Item>
                    </Descriptions>
                ))

            ) : (
                <div>
                    처리 이력이 없습니다.
                </div>
            )}
        </Card>
        </div>
    );
}

export default ReportDetailPage;