import { useEffect, useState } from 'react';
import { useRouter } from 'next/router';
import {
  Button,
  Card,
  Descriptions,
  Image,
  Tag,
  Space,
  message,
  Spin,
} from 'antd';

import {
  getAdvertiseDetail,
  deleteAdvertise,
} from '../../../api/advertiseApi';

function AdvertiseDetailPage() {
  const router = useRouter();
  const { adId } = router.query;

  const [advertise, setAdvertise] = useState(null);
  const [loading, setLoading] = useState(false);

  // 광고 상세 조회
  const loadAdvertiseDetail = async () => {
    if (!adId) return;

    try {
      setLoading(true);

      const response = await getAdvertiseDetail(adId);

      setAdvertise(response.data);

    } catch (error) {
      console.error('광고 상세 조회 실패', error);

      message.error(
        error.response?.data?.message ||
        '광고 정보를 불러오지 못했습니다.'
      );

      router.push('/user/mypage/advertiseList');

    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (!router.isReady) return;

    loadAdvertiseDetail();
  }, [router.isReady, adId]);

  // 수정
  const handleEdit = () => {
    router.push(
      `/user/mypage/advertiseWrite?adId=${adId}`
    );
  };

  // 삭제
  const handleDelete = async () => {
    if (!window.confirm('정말 삭제하시겠습니까?')) {
      return;
    }

    try {
      await deleteAdvertise(adId);

      message.success('광고가 삭제되었습니다.');

      router.push('/user/mypage/advertiseList');

    } catch (error) {
      console.error('광고 삭제 실패', error);

      message.error(
        error.response?.data?.message ||
        '광고 삭제에 실패했습니다.'
      );
    }
  };

  // 목록
  const handleList = () => {
    router.push('/user/mypage/advertiseList');
  };

  if (loading || !advertise) {
    return (
      <div
        style={{
          padding: 40,
          textAlign: 'center',
        }}
      >
        <Spin />
      </div>
    );
  }

  return (
    <div style={{ padding: 24 }}>

      {/* 제목 */}
      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          marginBottom: 20,
        }}
      >
        <h2 style={{ margin: 0 }}>
          광고 상세
        </h2>

        <Space>
          {advertise.approvalStatus === 'APPROVED' &&
             advertise.paymentStatus === 'WAITING' && (
             <Button type="primary">
                결제하기
             </Button>
          )}
          <Button onClick={handleList}>
            목록
          </Button>

          <Button onClick={handleEdit}>
            수정
          </Button>

          <Button
            danger
            onClick={handleDelete}
          >
            삭제
          </Button>
        </Space>
      </div>

      {/* 기본 정보 */}
      <Card
        title="광고 정보"
        style={{ marginBottom: 20 }}
      >
        <Descriptions
          bordered
          column={2}
        >
          <Descriptions.Item label="광고명" span={2}>
            {advertise.title}
          </Descriptions.Item>

          <Descriptions.Item label="승인 상태">
            {advertise.approvalStatus === 'APPROVED' ? (
              <Tag color="green">승인완료</Tag>
            ) : advertise.approvalStatus === 'REJECTED' ? (
              <Tag color="red">반려</Tag>
            ) : (
              <Tag color="orange">승인대기</Tag>
            )}
          </Descriptions.Item>

          <Descriptions.Item label="운영 상태">
            {advertise.status === 'OPEN' ? (
              <Tag color="blue">진행중</Tag>
            ) : advertise.status === 'CLOSED' ? (
              <Tag>종료</Tag>
            ) : (
              <Tag color="orange">대기</Tag>
            )}
          </Descriptions.Item>

          <Descriptions.Item label="광고 등급">
            {advertise.adGrade === 'PREMIUM' ? (
              <Tag color="gold">PREMIUM</Tag>
            ) : (
              <Tag>GENERAL</Tag>
            )}
          </Descriptions.Item>

          <Descriptions.Item label="결제 상태">
            {advertise.approvalStatus !== 'APPROVED' ? (
                <Tag color="default">
                승인 후 결제 가능
                </Tag>
            ) : advertise.paymentStatus === 'PAID' ? (
                <Tag color="green">
                결제완료
                </Tag>
            ) : advertise.paymentStatus === 'WAITING' ? (
                <Tag color="orange">
                결제대기
                </Tag>
            ) : advertise.paymentStatus === 'FAILED' ? (
                <Tag color="red">
                결제실패
                </Tag>
            ) : (
                <Tag>
                -
                </Tag>
            )}
          </Descriptions.Item>

          <Descriptions.Item label="광고 기간">
            {formatDateTime(advertise.startDatetime)}
            {' ~ '}
            {formatDateTime(advertise.endDatetime)}
          </Descriptions.Item>

          <Descriptions.Item label="총 예산">
            {formatMoney(advertise.totalBudget)}
          </Descriptions.Item>
        </Descriptions>
      </Card>

      {/* 타겟 정보 */}
      <Card
        title="타겟 설정"
        style={{ marginBottom: 20 }}
      >
        <Descriptions bordered column={2}>
          <Descriptions.Item label="최소 연령">
            {advertise.targetAgeMin
              ? `${advertise.targetAgeMin}세`
              : '-'}
          </Descriptions.Item>

          <Descriptions.Item label="최대 연령">
            {advertise.targetAgeMax
              ? `${advertise.targetAgeMax}세`
              : '-'}
          </Descriptions.Item>

          <Descriptions.Item label="성별">
            {advertise.targetGender || '-'}
          </Descriptions.Item>
        </Descriptions>
      </Card>

      {/* 광고 내용 */}
      <Card
        title="광고 내용"
        style={{ marginBottom: 20 }}
      >
        <Descriptions bordered column={1}>
          <Descriptions.Item label="랜딩 URL">
            {advertise.landingUrl || '-'}
          </Descriptions.Item>

          <Descriptions.Item label="내용">
            <div
              style={{
                whiteSpace: 'pre-wrap',
                minHeight: 100,
              }}
            >
              {advertise.content}
            </div>
          </Descriptions.Item>
        </Descriptions>
      </Card>

      {/* 이미지 */}
      <Card title="광고 이미지">
        {advertise.imageList &&
        advertise.imageList.length > 0 ? (
          <Space wrap>
            {advertise.imageList.map((image) => (
              <Image
                key={image.imageId}
                src={image.imageUrl}
                alt={advertise.title}
                width={200}
                height={120}
                style={{
                  objectFit: 'cover',
                }}
              />
            ))}
          </Space>
        ) : (
          <div>등록된 이미지가 없습니다.</div>
        )}
      </Card>

    </div>
  );
}


// 날짜
function formatDateTime(value) {
  if (!value) {
    return '-';
  }

  return String(value)
    .replace('T', ' ')
    .substring(0, 16);
}


// 금액
function formatMoney(value) {
  if (value == null) {
    return '-';
  }

  return `${Number(value).toLocaleString()}원`;
}


export default AdvertiseDetailPage;