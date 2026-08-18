import React from 'react';
import {
    Table,
    Tag,
    Button,
    Space,
    Popconfirm
} from 'antd';

const AdvertisementTable = ({
    advertisements,
    loading,
    onDetail,
    onDelete,
    onExtend
}) => {

    const columns = [

        {
            title: '번호',
            key: 'no',
            width: 80,
            render: (_, record, index) => index + 1
        },

        {
            title: '이미지',
            key: 'image',
            width: 120,
            render: (_, record) => {

                const image = record.imageList?.[0];

                if (!image) {
                    return '-';
                }

                return (
                    <img
                        src={`http://localhost:8080/${image.imageUrl}`}
                        alt={record.title}
                        style={{
                            width: 80,
                            height: 60,
                            objectFit: 'cover',
                            borderRadius: 4
                        }}
                    />
                );
            }
        },

        {
            title: '광고명',
            dataIndex: 'title',
            key: 'title'
        },

        {
            title: '승인상태',
            dataIndex: 'approvalStatus',
            key: 'approvalStatus',
            render: (status) => {

                switch (status) {

                    case 'WAITING':
                        return (
                            <Tag color="orange">
                                승인대기
                            </Tag>
                        );

                    case 'APPROVED':
                        return (
                            <Tag color="green">
                                승인완료
                            </Tag>
                        );

                    case 'REJECTED':
                        return (
                            <Tag color="red">
                                반려
                            </Tag>
                        );

                    default:
                        return '-';
                }
            }
        },

        {
            title: '운영상태',
            dataIndex: 'status',
            key: 'status',
            render: (status, record) => {

                return (
                    <Space direction="vertical">

                        {status === 'PENDING' && (
                            <Tag>
                                대기
                            </Tag>
                        )}

                        {status === 'OPEN' && (
                            <Tag color="blue">
                                진행중
                            </Tag>
                        )}

                        {status === 'CLOSED' && (
                            <Tag>
                                종료
                            </Tag>
                        )}

                        {record.paymentType === 'EXTENSION' &&
                         record.paymentStatus === 'WAITING' && (
                            <Button
                                type="primary"
                                size="small"
                                onClick={() => onExtend(record)}
                            >
                                연장하기
                            </Button>
                        )}

                    </Space>
                );
            }
        },

        {
            title: '광고기간',
            key: 'period',
            render: (_, record) => {

                const start = record.startDatetime
                    ?.substring(0, 10);

                const end = record.endDatetime
                    ?.substring(0, 10);

                return `${start} ~ ${end}`;
            }
        },

        {
            title: '관리',
            key: 'action',
            render: (_, record) => (

                <Space>

                    <Button
                        type="primary"
                        size="small"
                        onClick={() => onDetail(record.adId)}
                    >
                        상세
                    </Button>

                    <Popconfirm
                        title="정말 삭제하시겠습니까?"
                        onConfirm={() => onDelete(record.adId)}
                        okText="삭제"
                        cancelText="취소"
                    >
                        <Button
                            danger
                            size="small"
                        >
                            삭제
                        </Button>
                    </Popconfirm>

                </Space>
            )
        }

    ];

    return (
        <Table
            rowKey="adId"
            columns={columns}
            dataSource={advertisements}
            loading={loading}
            pagination={false}
            locale={{
                emptyText: '등록된 광고가 없습니다.'
            }}
        />
    );
};

export default AdvertisementTable;