import { useState } from 'react';
import { useRouter } from 'next/router';
import {
  Form,  Input,  Select,  DatePicker,
  Button,  Card,  Upload,  Space,  Tag,
  Divider,  message,
} from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';

import {
  createAdvertise,
  updateAdvertise,
} from '../../../api/advertiseApi';

// 가격 따로 빼서 관리자랑 같이 사용
import {
  NEW_PRICES,
  POSITION_PRICES,
} from '../../../constants/advertisePrice';

const { TextArea } = Input;
const { RangePicker } = DatePicker;

function AdvertiseWritePage() {
  const router = useRouter();
  const { adId } = router.query;

  const [loading, setLoading] = useState(false);

  // 메인기본선택용
  const [selectedPositions, setSelectedPositions] = useState(['MAIN']);       

  const [imageFiles, setImageFiles] = useState({
    MAIN: [],
    MEETUP_LIST_BANNER: [],
    MEETUP_LIST_SIDEBAR: [],
    MEETUP_DETAIL_SIDEBAR: [],
  });
  //////// 기간 계산
  const calculateDays = (period) => {
    if (!period || period.length !== 2) {
        return 0;
    }

    const start = period[0];
    const end = period[1];

    const diff = end.diff(start, 'day', true);

    return Math.ceil(diff);
  };

  /////// 가격 계산
  const calculateAdPrice = (days, grade) => {
    if (!days || days <= 0) {
        return {
        total: 0,
        details: [],
        };
    }

    let remainingDays = days;
    let total = 0;
    const details = [];

    const prices = [...NEW_PRICES]
        .sort((a, b) => b.days - a.days);

        for (const price of prices) {
            const count = Math.floor(
            remainingDays / price.days
            );

            if (count === 0) {
            continue;
            }

            const unitPrice =
            grade === 'PREMIUM'
                ? price.premiumPrice
                : price.generalPrice;

            const amount = unitPrice * count;

            total += amount;
            remainingDays -= price.days * count;

            details.push({
            days: price.days,
            count,
            unitPrice,
            amount,
            });
        }

        return {
            total,
            details,
        };
    };

  const isEdit = !!adId;

  // 이미지 타입별 설정
  const imageConfigs = [
    {
      type: 'MAIN',
      label: '메인 광고',
      description: '메인 화면에 노출되는 광고 이미지',
      required: true,
    },
    {
      type: 'MEETUP_LIST_BANNER',
      label: '모임 목록 배너',
      description: '모임 목록 상단 배너 영역에 노출',
      required: false,
    },
    {
      type: 'MEETUP_LIST_SIDEBAR',
      label: '모임 목록 사이드',
      description: '모임 목록 사이드 영역에 노출',
      required: false,
    },
    {
      type: 'MEETUP_DETAIL_SIDEBAR',
      label: '모임 상세 사이드',
      description: '모임 상세 사이드 영역에 노출',
      required: false,
    },
  ];

  // 이미지 변경
  const handleImageChange = (type, { fileList }) => {
    setImageFiles((prev) => ({
      ...prev,
      [type]: fileList.slice(-1),
    }));
  };

  // 등록 / 수정
  const handleSubmit = async (values) => {
    try {
      setLoading(true);

      const formData = new FormData();

      // 기본 정보
      formData.append('title', values.title);
      formData.append('content', values.content);
      formData.append('landingUrl', values.landingUrl);

      // 타겟
      formData.append(
        'targetAgeMin',
        values.targetAgeMin
      );

      formData.append(
        'targetAgeMax',
        values.targetAgeMax
      );

      formData.append(
        'targetGender',
        values.targetGender
      );

      // 광고 등급
      formData.append(
        'adGrade',
        values.adGrade
      );

      // 광고 기간
      formData.append(
        'startDatetime',
        values.period[0].format(
          'YYYY-MM-DD HH:mm:ss'
        )
      );

      formData.append(
        'endDatetime',
        values.period[1].format(
          'YYYY-MM-DD HH:mm:ss'
        )
      );

      // 이미지
      Object.entries(imageFiles).forEach(
        ([imageType, files]) => {
          if (!files || files.length === 0) {
            return;
          }

          const file = files[0];

          if (!file.originFileObj) {
            return;
          }

          formData.append(
            'imageFiles',
            file.originFileObj
          );

          formData.append(
            'imageTypes',
            imageType
          );
        }
      );

      console.log(
        '광고 등록 FormData',
        [...formData.entries()]
      );

      if (isEdit) {
        await updateAdvertise(
          adId,
          formData
        );

        message.success(
          '광고가 수정되었습니다.'
        );
      } else {
        await createAdvertise(
          formData
        );

        message.success(
          '광고가 등록되었습니다.'
        );
      }

      router.push(
        '/user/mypage/advertiseList'
      );

    } catch (error) {
      console.error(
        isEdit
          ? '광고 수정 실패'
          : '광고 등록 실패',
        error
      );

      message.error(
        error.response?.data?.message ||
        `광고 ${
          isEdit ? '수정' : '등록'
        }에 실패했습니다.`
      );

    } finally {
      setLoading(false);
    }
  };

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
          {isEdit
            ? '광고 수정'
            : '광고 등록'}
        </h2>

        <Button
          onClick={() =>
            router.push(
              '/user/mypage/advertiseList'
            )
          }
        >
          목록
        </Button>
      </div>

      <Card>

        <Form
          layout="vertical"
          onFinish={handleSubmit}
          initialValues={{
            targetGender: 'ALL',
            adGrade: 'GENERAL',
          }}
        >

          {/* =========================
              기본 정보
          ========================== */}

          <h3>기본 정보</h3>

          <Form.Item
            label="광고명"
            name="title"
            rules={[
              {
                required: true,
                message:
                  '광고명을 입력해주세요.',
              },
            ]}
          >
            <Input
              placeholder="광고명을 입력해주세요."
              maxLength={100}
            />
          </Form.Item>

          <Form.Item
            label="광고 내용"
            name="content"
            rules={[
              {
                required: true,
                message:
                  '광고 내용을 입력해주세요.',
              },
            ]}
          >
            <TextArea
              rows={6}
              placeholder="광고 내용을 입력해주세요."
              maxLength={1000}
              showCount
            />
          </Form.Item>

          <Form.Item
            label="랜딩 URL"
            name="landingUrl"
            rules={[
              {
                required: true,
                message:
                  '랜딩 URL을 입력해주세요.',
              },
              {
                type: 'url',
                message:
                  '올바른 URL을 입력해주세요.',
              },
            ]}
          >
            <Input
              placeholder="https://example.com"
            />
          </Form.Item>


          {/* =========================
              타겟 설정
          ========================== */}

          <Divider />

          <h3>타겟 설정</h3>

          <Space
            style={{
              display: 'flex',
              width: '100%',
            }}
            align="start"
          >

            <Form.Item
              label="최소 연령"
              name="targetAgeMin"
              rules={[
                {
                  required: true,
                  message:
                    '최소 연령을 입력해주세요.',
                },
              ]}
            >
              <Input
                type="number"
                min={1}
                max={100}
                addonAfter="세"
              />
            </Form.Item>

            <Form.Item
              label="최대 연령"
              name="targetAgeMax"
              rules={[
                {
                  required: true,
                  message:
                    '최대 연령을 입력해주세요.',
                },
              ]}
            >
              <Input
                type="number"
                min={1}
                max={100}
                addonAfter="세"
              />
            </Form.Item>

          </Space>

          <Form.Item
            label="타겟 성별"
            name="targetGender"
            rules={[
              {
                required: true,
                message:
                  '타겟 성별을 선택해주세요.',
              },
            ]}
          >
            <Select
              options={[
                {
                  value: 'ALL',
                  label: '전체',
                },
                {
                  value: 'MALE',
                  label: '남성',
                },
                {
                  value: 'FEMALE',
                  label: '여성',
                },
              ]}
            />
          </Form.Item>

          {/* =========================
              광고 가격표
          ========================== */}
          <Divider />

            <h3>광고 가격표</h3>

            <Card
            size="small"
            style={{
                marginBottom: 20,
            }}
            >
            <div
                style={{
                marginBottom: 12,
                color: '#666',
                fontSize: 13,
                }}
            >
                광고 기간과 등급에 따라 기본 광고비가
                결정됩니다.
            </div>

            <table
                style={{
                width: '100%',
                borderCollapse: 'collapse',
                textAlign: 'center',
                }}
            >
                <thead>
                <tr>
                    <th
                    style={{
                        padding: 10,
                        borderBottom: '1px solid #ddd',
                    }}
                    >
                    기간
                    </th>

                    <th
                    style={{
                        padding: 10,
                        borderBottom: '1px solid #ddd',
                    }}
                    >
                    일반 광고
                    </th>

                    <th
                    style={{
                        padding: 10,
                        borderBottom: '1px solid #ddd',
                    }}
                    >
                    프리미엄 광고
                    </th>
                </tr>
                </thead>

                <tbody>
                {NEW_PRICES.map((price) => (
                    <tr key={price.days}>
                    <td
                        style={{
                        padding: 10,
                        borderBottom: '1px solid #eee',
                        }}
                    >
                        {price.days}일
                    </td>

                    <td
                        style={{
                        padding: 10,
                        borderBottom: '1px solid #eee',
                        }}
                    >
                        {price.generalPrice.toLocaleString()}원
                    </td>

                    <td
                        style={{
                        padding: 10,
                        borderBottom: '1px solid #eee',
                        }}
                    >
                        {price.premiumPrice.toLocaleString()}원
                    </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <Divider />

                <div
                    style={{
                    fontWeight: 600,
                    marginBottom: 10,
                    }}
                >
                    광고 위치 추가금
                </div>

                {POSITION_PRICES.map((position) => (
                    <div
                    key={position.key}
                    style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        padding: '6px 0',
                    }}
                    >
                    <span>
                        {position.label}
                    </span>

                    <span>
                        +{position.price.toLocaleString()}원
                    </span>
                    </div>
                ))}
            </Card>

          {/* =========================
              광고 등급
          ========================== */}

          <Divider />

          <h3>광고 상품</h3>

          <Form.Item
            label="광고 등급"
            name="adGrade"
            rules={[
              {
                required: true,
                message:
                  '광고 등급을 선택해주세요.',
              },
            ]}
          >
            <Select
              options={[
                {
                  value: 'GENERAL',
                  label: '일반 광고',
                },
                {
                  value: 'PREMIUM',
                  label: '프리미엄 광고',
                },
              ]}
            />
          </Form.Item>

          <Card
            size="small"
            style={{
              marginBottom: 20,
              background: '#fafafa',
            }}
          >
            <div>
              <Tag>
                GENERAL
              </Tag>

              <span>
                일반 광고 · 기본 노출
              </span>
            </div>

            <div
              style={{
                marginTop: 10,
              }}
            >
              <Tag color="gold">
                PREMIUM
              </Tag>

              <span>
                프리미엄 광고 · 높은 우선순위
              </span>
            </div>

            <div
              style={{
                marginTop: 15,
                color: '#888',
                fontSize: 13,
              }}
            >
              ※ 실제 결제 금액은 광고 기간과
              노출 위치에 따라 계산됩니다.
            </div>
          </Card>


          {/* =========================
              광고 기간
          ========================== */}

          <Divider />

          <h3>광고 기간</h3>

          <Form.Item
            label="광고 기간"
            name="period"
            rules={[
              {
                required: true,
                message:
                  '광고 기간을 선택해주세요.',
              },
            ]}
          >
            <RangePicker
              showTime
              format="YYYY-MM-DD HH:mm"
              style={{
                width: '100%',
              }}
              disabledDate={(current) =>
                current &&
                current <
                  dayjs().startOf('day')
              }
            />
          </Form.Item>


          {/* =========================
              이미지
          ========================== */}

          <Divider />

          <h3>광고 이미지</h3>

          <p
            style={{
              color: '#888',
              marginBottom: 20,
            }}
          >
            광고가 노출될 위치별 이미지를
            등록할 수 있습니다.
          </p>

          {imageConfigs.map((config) => (

            <Card
              key={config.type}
              size="small"
              style={{
                marginBottom: 16,
              }}
            >

              <div
                style={{
                  display: 'flex',
                  justifyContent:
                    'space-between',
                  alignItems: 'center',
                  marginBottom: 10,
                }}
              >

                <div>
                  <strong>
                    {config.label}
                  </strong>

                  {config.required && (
                    <Tag
                      color="red"
                      style={{
                        marginLeft: 8,
                      }}
                    >
                      필수
                    </Tag>
                  )}

                  {!config.required && (
                    <Tag
                      style={{
                        marginLeft: 8,
                      }}
                    >
                      선택
                    </Tag>
                  )}
                </div>

                <span
                  style={{
                    color: '#888',
                    fontSize: 13,
                  }}
                >
                  {config.type}
                </span>

              </div>

              <div
                style={{
                  color: '#888',
                  fontSize: 13,
                  marginBottom: 12,
                }}
              >
                {config.description}
              </div>

              <Upload
                listType="picture"
                fileList={
                  imageFiles[config.type]
                }
                beforeUpload={() => false}
                onChange={(info) =>
                  handleImageChange(
                    config.type,
                    info
                  )
                }
                maxCount={1}
                accept="image/*"
              >
                {imageFiles[config.type]
                  .length === 0 && (
                  <Button
                    icon={<UploadOutlined />}
                  >
                    이미지 선택
                  </Button>
                )}
              </Upload>

            </Card>

          ))}

          <Form.Item shouldUpdate>
            {({ getFieldValue }) => {
                const period =
                getFieldValue('period');

                const adGrade =
                getFieldValue('adGrade');

                const days =
                calculateDays(period);

                const result =
                calculateAdPrice(
                    days,
                    adGrade
                );

                // 🌟 1. imageFiles 상태를 뒤져서 '실제로 이미지가 업로드된 위치'들만 뽑아냅니다.
                const activePositions = Object.keys(imageFiles).filter(
                  (key) => imageFiles[key] && imageFiles[key].length > 0
                );

                // 🌟 2. 이미지가 들어있는 위치의 가격만 찾아서 합산합니다.
                const positionExtra = activePositions.reduce(
                  (sum, position) => {
                    const item = POSITION_PRICES.find(
                      (p) => p.key === position
                    );

                    return sum + (item?.price || 0);
                  },
                  0
                );

                const finalPrice =
                result.total + positionExtra;

                return (
                <Card
                    style={{
                    marginTop: 20,
                    background: '#fafafa',
                    }}
                >
                    <h3>
                    예상 광고 금액
                    </h3>

                    <div>
                    광고 기간:{' '}
                    <strong>
                        {days > 0
                        ? `${days}일`
                        : '-'}
                    </strong>
                    </div>

                    <div style={{ marginTop: 8 }}>
                    광고 등급:{' '}
                    <strong>
                        {adGrade === 'PREMIUM'
                        ? '프리미엄'
                        : '일반'}
                    </strong>
                    </div>

                    {result.details.length > 0 && (
                    <div
                        style={{
                        marginTop: 15,
                        }}
                    >
                        {result.details.map(
                        (item) => (
                            <div
                            key={item.days}
                            style={{
                                display: 'flex',
                                justifyContent:
                                'space-between',
                                marginBottom: 5,
                            }}
                            >
                            <span>
                                {item.days}일 ×{' '}
                                {item.count}
                            </span>

                            <span>
                                {item.amount.toLocaleString()}
                                원
                            </span>
                            </div>
                        )
                        )}
                    </div>
                    )}

                    <Divider />

                            <div
                            style={{
                                display: 'flex',
                                justifyContent:
                                'space-between',
                            }}
                            >
                            <span>
                                기본 광고비
                            </span>

                            <span>
                                {result.total.toLocaleString()}원
                            </span>
                            </div>

                            <div
                            style={{
                                display: 'flex',
                                justifyContent:
                                'space-between',
                                marginTop: 8,
                            }}
                            >
                            <span>
                                위치 추가금
                            </span>

                            <span>
                                +{positionExtra.toLocaleString()}원
                            </span>
                            </div>

                            <Divider />

                            <div
                            style={{
                                display: 'flex',
                                justifyContent:
                                'space-between',
                                fontSize: 20,
                                fontWeight: 700,
                            }}
                            >
                            <span>
                                예상 광고비
                            </span>

                            <span>
                                {finalPrice.toLocaleString()}원
                            </span>
                            </div>
                        </Card>
                        );
                    }}
                    </Form.Item>

          {/* =========================
              버튼
          ========================== */}

          <Divider />

          <Form.Item
            style={{
              marginBottom: 0,
            }}
          >

            <Space>

              <Button
                onClick={() =>
                  router.push(
                    '/user/mypage/advertiseList'
                  )
                }
              >
                취소
              </Button>

              <Button
                type="primary"
                htmlType="submit"
                loading={loading}
              >
                {isEdit
                  ? '수정하기'
                  : '등록하기'}
              </Button>

            </Space>

          </Form.Item>

        </Form>

      </Card>

    </div>
  );
}

export default AdvertiseWritePage;