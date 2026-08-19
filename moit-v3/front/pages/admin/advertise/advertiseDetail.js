import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import {
  Card,
  Descriptions,
  Button,
  Tag,
  Image,
  Row,
  Col,
  Spin,
  message,
  Divider,
} from 'antd';

import {
  getAdvertiseAdminDetail,
  approveAdvertise,
  rejectAdvertise,
} from '../../../api/advertiseAdminApi';


function AdvertiseDetailPage() {

  const router = useRouter();

  const { adId, tab } = router.query;

  const [advertise, setAdvertise] = useState(null);
  const [loading, setLoading] = useState(false);


  // 상세 조회
  const loadDetail = async () => {

    if (!adId) {
      return;
    }

    try {

      setLoading(true);

      const response =
        await getAdvertiseAdminDetail(adId);

      setAdvertise(response.data);

    } catch (error) {

      console.error(
        '광고 상세 조회 실패',
        error
      );

      message.error(
        '광고 정보를 불러오지 못했습니다.'
      );

    } finally {

      setLoading(false);
    }
  };


  useEffect(() => {

    if (router.isReady) {
      loadDetail();
    }

  }, [router.isReady, adId]);


  // 목록으로
  const handleBack = () => {
    router.push({
      pathname: '/admin/advertise/advertise',
      query: {
        tab: tab || 'approval',
      },
    });
  };


  // 승인
  const handleApprove = async () => {

    if (
      !window.confirm(
        '이 광고를 승인하시겠습니까?'
      )
    ) {
      return;
    }

    try {

      await approveAdvertise(adId);

      message.success(
        '광고가 승인되었습니다.'
      );

      await loadDetail();

    } catch (error) {

      console.error(
        '광고 승인 실패',
        error
      );

      message.error(
        error.response?.data?.message ||
        '광고 승인에 실패했습니다.'
      );
    }
  };


  // 반려
  const handleReject = async () => {

    const rejectReason =
      window.prompt(
        '반려 사유를 입력해주세요.'
      );

    if (!rejectReason?.trim()) {
      return;
    }

    try {

      await rejectAdvertise(
        adId,
        rejectReason.trim()
      );

      message.success(
        '광고가 반려되었습니다.'
      );

      await loadDetail();

    } catch (error) {

      console.error(
        '광고 반려 실패',
        error
      );

      message.error(
        error.response?.data?.message ||
        '광고 반려에 실패했습니다.'
      );
    }
  };


  if (loading) {

    return (
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          padding: 100,
        }}
      >
        <Spin size="large" />
      </div>
    );

  }


  if (!advertise) {

    return (
      <Card>
        광고 정보를 찾을 수 없습니다.
      </Card>
    );

  }


  const imageList =
    advertise.imageList || [];


  return (
    <div>

      {/* 상단 */}

      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          marginBottom: 24,
        }}
      >

        <h2
          style={{
            margin: 0,
          }}
        >
          광고 상세
        </h2>

        <Button
          onClick={handleBack}
        >
          목록으로
        </Button>

      </div>
    
      {/* 통계 */}
      <Card
        title="광고 통계"
        style={{
          marginBottom: 20,
        }}
      >

        <Row gutter={16}>

          <Col span={8}>
            <Card>
              노출수
              <h2>
                {advertise.impressions ?? 0}
              </h2>
            </Card>
          </Col>


          <Col span={8}>
            <Card>
              클릭수
              <h2>
                {advertise.clicks ?? 0}
              </h2>
            </Card>
          </Col>


          <Col span={8}>
            <Card>
              CTR
              <h2>
                {calculateCtr(
                  advertise.impressions,
                  advertise.clicks
                )}
              </h2>
            </Card>
          </Col>

        </Row>

      </Card>

      {/* 기본 정보 */}

      <Card
        title="광고 정보"
        style={{
          marginBottom: 20,
        }}
      >

        <Descriptions
          bordered
          column={2}
        >

          <Descriptions.Item
            label="광고 번호"
          >
            {advertise.adId}
          </Descriptions.Item>


          <Descriptions.Item
            label="광고주"
          >
            {advertise.advertiserNickname || '-'}
          </Descriptions.Item>


          <Descriptions.Item
            label="광고명"
            span={2}
          >
            {advertise.title || '-'}
          </Descriptions.Item>


          <Descriptions.Item
            label="광고 유형"
          >
            {advertise.adChannel || '-'}
          </Descriptions.Item>


          <Descriptions.Item
            label="광고 등급"
          >
            <AdGradeTag
              value={advertise.adGrade}
            />
          </Descriptions.Item>


          <Descriptions.Item
            label="승인 상태"
          >
            <ApprovalStatusTag
              value={
                advertise.approvalStatus
              }
            />
          </Descriptions.Item>


          <Descriptions.Item
            label="광고 상태"
          >
            <AdStatusTag
              value={advertise.status}
            />
          </Descriptions.Item>


          <Descriptions.Item
            label="광고 기간"
          >
            {formatDate(
              advertise.startDatetime
            )}
            {' ~ '}
            {formatDate(
              advertise.endDatetime
            )}
          </Descriptions.Item>


          <Descriptions.Item
            label="등록일"
          >
            {formatDateTime(
              advertise.createdAt
            )}
          </Descriptions.Item>

        </Descriptions>

      </Card>


      {/* 광고 내용 */}

      <Card
        title="광고 내용"
        style={{
          marginBottom: 20,
        }}
      >

        <Descriptions
          bordered
          column={1}
        >

          <Descriptions.Item
            label="광고 제목"
          >
            {advertise.title || '-'}
          </Descriptions.Item>


          <Descriptions.Item
            label="광고 내용"
          >

            <div
              style={{
                whiteSpace: 'pre-wrap',
                minHeight: 100,
              }}
            >
              {advertise.content || '-'}
            </div>

          </Descriptions.Item>


          <Descriptions.Item
            label="랜딩 URL"
          >

            {advertise.landingUrl ? (

              <a
                href={advertise.landingUrl}
                target="_blank"
                rel="noreferrer"
              >
                {advertise.landingUrl}
              </a>

            ) : (
              '-'
            )}

          </Descriptions.Item>

        </Descriptions>

      </Card>


      {/* 광고 타겟 */}

      <Card
        title="광고 타겟"
        style={{
          marginBottom: 20,
        }}
      >

        <Descriptions
          bordered
          column={2}
        >

          <Descriptions.Item
            label="최소 연령"
          >
            {advertise.targetAgeMin != null
              ? `${advertise.targetAgeMin}세`
              : '-'}
          </Descriptions.Item>


          <Descriptions.Item
            label="최대 연령"
          >
            {advertise.targetAgeMax != null
              ? `${advertise.targetAgeMax}세`
              : '-'}
          </Descriptions.Item>


          <Descriptions.Item
            label="성별"
          >
            {formatGender(
              advertise.targetGender
            )}
          </Descriptions.Item>


          <Descriptions.Item
            label="광고 위치"
          >
            {advertise.position || '-'}
          </Descriptions.Item>

        </Descriptions>

      </Card>


      {/* 이미지 */}

      <Card
        title="광고 이미지"
        style={{
          marginBottom: 20,
        }}
      >

        {imageList.length > 0 ? (

          <Row gutter={[16, 16]}>

            {imageList.map((image) => (

              <Col
                xs={24}
                sm={12}
                md={8}
                key={
                  image.imageId ||
                  image.imageUrl
                }
              >

                <Image
                  src={image.imageUrl}
                  alt={advertise.title}
                  style={{
                    width: '100%',
                    height: 180,
                    objectFit: 'cover',
                  }}
                />

                <div
                  style={{
                    marginTop: 8,
                    textAlign: 'center',
                  }}
                >
                  {image.imageType || ''}
                </div>

              </Col>

            ))}

          </Row>

        ) : (

          <div>
            등록된 이미지가 없습니다.
          </div>

        )}

      </Card>


      {/* 결제 정보 */}

      <Card
        title="결제 정보"
        style={{
          marginBottom: 20,
        }}
      >

        <Descriptions
          bordered
          column={2}
        >

          <Descriptions.Item
            label="결제 유형"
          >
            {advertise.paymentType || '-'}
          </Descriptions.Item>


          <Descriptions.Item
            label="결제 상태"
          >
            <PaymentStatusTag
              value={
                advertise.paymentStatus
              }
            />
          </Descriptions.Item>


          <Descriptions.Item
            label="결제 금액"
          >
            {formatPrice(
              advertise.amount
            )}
          </Descriptions.Item>


          <Descriptions.Item
            label="총 예산"
          >
            {formatPrice(
              advertise.totalBudget
            )}
          </Descriptions.Item>


          <Descriptions.Item
            label="결제일"
          >
            {formatDateTime(
              advertise.paymentAt
            )}
          </Descriptions.Item>

        </Descriptions>

      </Card>


      {/* 반려 사유 */}

      {advertise.rejectReason && (

        <Card
          title="반려 정보"
          style={{
            marginBottom: 20,
          }}
        >

          <Descriptions
            bordered
            column={1}
          >

            <Descriptions.Item
              label="반려 사유"
            >
              {advertise.rejectReason}
            </Descriptions.Item>


            <Descriptions.Item
              label="반려일"
            >
              {formatDateTime(
                advertise.rejectedAt
              )}
            </Descriptions.Item>

          </Descriptions>

        </Card>

      )}


      <Divider />


      {/* 관리자 버튼 */}

      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          gap: 10,
        }}
      >

        <Button
          onClick={handleBack}
        >
          목록
        </Button>


        {tab === 'approval' &&
          advertise.approvalStatus === 'WAITING' && (
            <>
              <Button
                type="primary"
                onClick={handleApprove}
              >
                승인
              </Button>

              <Button
                danger
                onClick={handleReject}
              >
                반려
              </Button>
            </>
        )}

      </div>

    </div>
  );
}


/* 승인 상태 */
function ApprovalStatusTag({
  value,
}) {

  if (value === 'APPROVED') {
    return (
      <Tag color="green">
        승인
      </Tag>
    );
  }

  if (value === 'REJECTED') {
    return (
      <Tag color="red">
        반려
      </Tag>
    );
  }

  return (
    <Tag color="orange">
      승인 대기
    </Tag>
  );
}


/* 광고 상태 */
function AdStatusTag({
  value,
}) {

  if (value === 'OPEN') {
    return (
      <Tag color="green">
        게시 중
      </Tag>
    );
  }

  if (value === 'CLOSED') {
    return (
      <Tag>
        종료
      </Tag>
    );
  }

  return (
    <Tag color="orange">
      게시 전
    </Tag>
  );
}


/* 광고 등급 */
function AdGradeTag({
  value,
}) {

  if (value === 'PREMIUM') {
    return (
      <Tag color="gold">
        PREMIUM
      </Tag>
    );
  }

  return (
    <Tag>
      GENERAL
    </Tag>
  );
}


/* 결제 상태 */
function PaymentStatusTag({
  value,
}) {

  if (value === 'PAID') {
    return (
      <Tag color="green">
        결제 완료
      </Tag>
    );
  }

  if (value === 'CANCELLED') {
    return (
      <Tag color="red">
        결제 취소
      </Tag>
    );
  }

  return (
    <Tag color="orange">
      결제 대기
    </Tag>
  );
}


/* 성별 */
function formatGender(value) {

  if (value === 'M') {
    return '남성';
  }

  if (value === 'F') {
    return '여성';
  }

  if (value === 'ALL') {
    return '전체';
  }

  return value || '-';
}


/* 날짜 */
function formatDate(value) {

  if (!value) {
    return '-';
  }

  return String(value).substring(
    0,
    10
  );
}


/* 날짜 + 시간 */
function formatDateTime(value) {

  if (!value) {
    return '-';
  }

  return String(value)
    .replace('T', ' ')
    .substring(0, 19);
}


/* 금액 */
function formatPrice(value) {

  if (value == null) {
    return '-';
  }

  return `${Number(
    value
  ).toLocaleString()}원`;
}


/* CTR */
function calculateCtr(
  impressions,
  clicks
) {

  const impressionCount =
    Number(impressions || 0);

  const clickCount =
    Number(clicks || 0);

  if (impressionCount === 0) {
    return '0.00%';
  }

  return (
    (clickCount / impressionCount) *
    100
  ).toFixed(2) + '%';
}


export default AdvertiseDetailPage;