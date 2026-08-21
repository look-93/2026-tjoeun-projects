import { useEffect, useState } from 'react';
import { Row, Col, Button, Pagination, message } from 'antd';

import AdminStatCard from '../../../components/AdminStatCard';
import AdminSearchBox from '../../../components/AdminSearchBox';
import AdminListTabs from '../../../components/AdminListTabs';

import AdvertiseApprovalTable from '../../../components/AdvertiseApprovalTable';
import AdvertisePaymentTable from '../../../components/AdvertisePaymentTable';
import AdvertiseStatusTable from '../../../components/AdvertiseStatusTable';
import AdvertisePriceModal from '../../../components/AdvertisePriceModal';

import {
  // 탭별 목록 API 추가
  getAdvertiseApprovalTabList,
  getAdvertisePaymentTabList,
  getAdvertiseStatusTabList,
  
  // 탭별 개수 API 추가
  getAdvertiseApprovalTabCount,
  getAdvertisePaymentTabCount,
  getAdvertiseStatusTabCount,

  approveAdvertise,
  rejectAdvertise,
  updateAdvertiseStatus,
} from '../../../api/advertiseAdminApi';

// http://localhost:3000/admin/advertise

function AdminAdvertisePage() {

  // 현재 탭
  const [tab, setTab] = useState('approval');

  // 광고 목록
  const [advertiseList, setAdvertiseList] = useState([]);

  // 검색 조건
  const [searchText, setSearchText] = useState('');
  const [status, setStatus] = useState('');
  const [sort, setSort] = useState('');

  // 페이징
  const [page, setPage] = useState(1);
  const [totalCount, setTotalCount] = useState(0);

  // 가격표 모달
  const [priceModalOpen, setPriceModalOpen] = useState(false);

  const size = 10;


  // 광고 목록 조회
  const loadAdvertiseList = async () => {
    try {
      const params = {
        searchText,
        status,
        sort,
        page,
        size,
      };

      let response;

      // 탭에 따라 각각 다른 API 호출
      if (tab === 'approval') {
        response = await getAdvertiseApprovalTabList(params);
      } else if (tab === 'payment') {
        response = await getAdvertisePaymentTabList(params);
      } else if (tab === 'status') {
        response = await getAdvertiseStatusTabList(params);
      }

      console.log('API 응답 데이터:', response.data);
      setAdvertiseList(response?.data?.list || []);
    } catch (error) {
      console.error('광고 목록 조회 실패', error);
      message.error('광고 목록을 불러오지 못했습니다.');
    }
  };


  // 광고 개수 조회
  const loadCount = async () => {
    try {
      const params = {
        searchText,
        status,
      };

      let response;

      // 탭에 따라 각각 다른 API 호출
      if (tab === 'approval') {
        response = await getAdvertiseApprovalTabCount(params);
      } else if (tab === 'payment') {
        response = await getAdvertisePaymentTabCount(params);
      } else if (tab === 'status') {
        response = await getAdvertiseStatusTabCount(params);
      }

      setTotalCount(response?.data || 0);
    } catch (error) {
      console.error('광고 개수 조회 실패', error);
    }
  };


  // 목록 조회
  useEffect(() => {

    loadAdvertiseList();
    loadCount();

  }, [
    tab,
    page,
    searchText,
    status,
    sort,
  ]);


  // 검색
  const handleSearch = (values) => {

    const nextSearchText =
      values?.searchText || '';

    const nextStatus =
      values?.status === 'all'
        ? ''
        : values?.status || '';

    const nextSort =
      values?.sort === 'all'
        ? ''
        : values?.sort || '';

    setSearchText(nextSearchText);
    setStatus(nextStatus);
    setSort(nextSort);

    setPage(1);
  };


  // 탭 변경
  const handleTabChange = (nextTab) => {

    setTab(nextTab);

    setPage(1);

    setSearchText('');
    setStatus('');
    setSort('');
  };


  // 승인
  const handleApprove = async (adId) => {

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

      await loadAdvertiseList();
      await loadCount();

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
  const handleReject = async (adId) => {

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

      await loadAdvertiseList();
      await loadCount();

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


  // 광고 상태 변경
  const handleStatusChange = async (
    adId,
    nextStatus
  ) => {

    try {

      await updateAdvertiseStatus(
        adId,
        nextStatus
      );

      message.success(
        '광고 상태가 변경되었습니다.'
      );

      await loadAdvertiseList();

    } catch (error) {

      console.error(
        '광고 상태 변경 실패',
        error
      );

      message.error(
        error.response?.data?.message ||
        '광고 상태 변경에 실패했습니다.'
      );
    }
  };


  // 광고 상세
  const handleDetail = (adId) => {
    window.location.href =
      `/admin/advertise/advertiseDetail?adId=${adId}&tab=${tab}`;
  };


  /*
   * =========================
   * 탭
   * =========================
   */

  const listTabs = [
    {
      key: 'approval',
      label: '승인 관리',
    },
    {
      key: 'payment',
      label: '결제 확인',
    },
    {
      key: 'status',
      label: '운영 관리',
    },
  ];


  /*
   * =========================
   * 검색 조건
   * =========================
   */

  const approvalSearchConditions = [
    {
      key: 'status',
      defaultValue: 'all',
      options: [
        {
          value: 'all',
          label: '전체 상태',
        },
        {
          value: 'WAITING',
          label: '승인 대기',
        },
        {
          value: 'PAYMENT_WAITING',
          label: '결제 대기',
        },
        {
          value: 'REJECTED',
          label: '반려',
        },
      ],
    },
    {
      key: 'sort',
      defaultValue: 'all',
      options: [
        {
          value: 'all',
          label: '최신 등록순',
        },
        {
          value: 'start',
          label: '시작 예정순',
        },
        {
          value: 'end',
          label: '종료 임박순',
        },
        {
          value: 'budget',
          label: '예산 높은순',
        },
        {
          value: 'grade',
          label: '등급순',
        },
      ],
    },
  ];


  const paymentSearchConditions = [
    {
      key: 'status',
      defaultValue: 'all',
      options: [
        {
          value: 'all',
          label: '전체 유형',
        },
        {
          value: 'NEW',
          label: '신규 결제',
        },
        {
          value: 'EXTENSION',
          label: '연장 결제',
        },
        {
          value: 'WAITING',
          label: '결제 대기',
        },
      ],
    },
    {
      key: 'sort',
      defaultValue: 'all',
      options: [
        {
          value: 'all',
          label: '최신 결제순',
        },
        {
          value: 'amount',
          label: '결제 금액순',
        },
        {
          value: 'date',
          label: '결제 예정순',
        },
      ],
    },
  ];


  const statusSearchConditions = [
    {
      key: 'searchText',
      defaultValue: '',
      options: [],
    },
    {
      key: 'status',
      defaultValue: 'all',
      options: [
        {
          value: 'all',
          label: '전체 상태',
        },
        {
          value: 'BEFORE_OPEN',
          label: '게시 전',
        },
        {
          value: 'OPEN',
          label: '게시 중',
        },
        {
          value: 'CLOSED',
          label: '종료',
        },
      ],
    },
    {
      key: 'sort',
      defaultValue: 'all',
      options: [
        {
          value: 'all',
          label: '최신 등록순',
        },
        {
          value: 'start',
          label: '게시 시작순',
        },
        {
          value: 'end',
          label: '종료 임박순',
        },
        {
          value: 'impressions',
          label: '노출수순',
        },
        {
          value: 'clicks',
          label: '클릭수순',
        },
      ],
    },
  ];


  const currentSearchConditions =
    tab === 'approval'
      ? approvalSearchConditions
      : tab === 'payment'
        ? paymentSearchConditions
        : statusSearchConditions;


  /*
   * =========================
   * 통계
   * =========================
   *
   * 현재 통계 API가 없으므로
   * 임시로 목록 데이터를 기준으로 계산
   */

  const approvalWaitingCount =
    advertiseList.filter(
      (item) =>
        item.approvalStatus === 'WAITING'
    ).length;

  const paymentWaitingCount =
    advertiseList.filter(
      (item) =>
        item.paymentStatus === 'WAITING' ||
        item.approvalStatus === 'PAYMENT_WAITING'
    ).length;

  const rejectedCount =
    advertiseList.filter(
      (item) =>
        item.approvalStatus === 'REJECTED'
    ).length;

  const newPaymentCount =
    advertiseList.filter(
      (item) =>
        item.paymentType === 'NEW'
    ).length;

  const extensionPaymentCount =
    advertiseList.filter(
      (item) =>
        item.paymentType === 'EXTENSION'
    ).length;

  const beforeOpenCount =
    advertiseList.filter(
      (item) =>
        item.status === 'PENDING'
    ).length;

  const openCount =
    advertiseList.filter(
      (item) =>
        item.status === 'OPEN'
    ).length;

  const closedCount =
    advertiseList.filter(
      (item) =>
        item.status === 'CLOSED'
    ).length;


  const approvalStats = [
    {
      title: '전체 광고',
      value: totalCount,
      suffix: '개',
    },
    {
      title: '승인 대기',
      value: approvalWaitingCount,
      suffix: '개',
    },
    {
      title: '결제 대기',
      value: paymentWaitingCount,
      suffix: '개',
    },
    {
      title: '반려',
      value: rejectedCount,
      suffix: '개',
    },
  ];


  const paymentStats = [
    {
      title: '전체 결제',
      value: totalCount,
      suffix: '건',
    },
    {
      title: '신규 결제',
      value: newPaymentCount,
      suffix: '건',
    },
    {
      title: '연장 결제',
      value: extensionPaymentCount,
      suffix: '건',
    },
    {
      title: '결제 대기',
      value: paymentWaitingCount,
      suffix: '건',
    },
  ];


  const statusStats = [
    {
      title: '전체 광고',
      value: totalCount,
      suffix: '개',
    },
    {
      title: '게시 전',
      value: beforeOpenCount,
      suffix: '개',
    },
    {
      title: '게시 중',
      value: openCount,
      suffix: '개',
    },
    {
      title: '종료',
      value: closedCount,
      suffix: '개',
    },
  ];


  const stats =
    tab === 'approval'
      ? approvalStats
      : tab === 'payment'
        ? paymentStats
        : statusStats;


  /*
   * =========================
   * 화면
   * =========================
   */

  return (
    <>

      {/* 통계 */}

      <Row gutter={[16, 16]}>

        {stats.map((stat) => (

          <Col
            xs={24}
            sm={12}
            md={12}
            lg={6}
            key={stat.title}
          >
            <AdminStatCard
              {...stat}
            />
          </Col>

        ))}

      </Row>


      {/* 탭 + 가격표 */}

      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          margin: '24px 0 16px',
        }}
      >

        <AdminListTabs
          tabs={listTabs}
          activeTab={tab}
          onChange={handleTabChange}
        />

        <Button
          type="primary"
          onClick={() =>
            setPriceModalOpen(true)
          }
        >
          광고 가격표
        </Button>

      </div>


      {/* 검색 */}

      <AdminSearchBox
        conditions={currentSearchConditions}
        onSearch={handleSearch}
      />


      {/* 목록 */}

      {tab === 'approval' && (

        <AdvertiseApprovalTable
          dataSource={advertiseList}
          page={page}
          size={size}
          onDetail={handleDetail}
          onApprove={handleApprove}
          onReject={handleReject}
        />

      )}


      {tab === 'payment' && (

        <AdvertisePaymentTable
          dataSource={advertiseList}
          page={page}
          size={size}
          onDetail={handleDetail}
        />

      )}


      {tab === 'status' && (

        <AdvertiseStatusTable
          dataSource={advertiseList}
          page={page}
          size={size}
          onDetail={handleDetail}
          onStatusChange={handleStatusChange}
        />

      )}


      {/* 페이징 */}

      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          marginTop: 16,
        }}
      >

        <Pagination
          current={page}
          pageSize={size}
          total={totalCount}
          showSizeChanger={false}
          onChange={(nextPage) =>
            setPage(nextPage)
          }
        />

      </div>


      {/* 가격표 */}

      <AdvertisePriceModal
        open={priceModalOpen}
        onClose={() =>
          setPriceModalOpen(false)
        }
      />

    </>
  );
}


export default AdminAdvertisePage;