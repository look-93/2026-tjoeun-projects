import { useEffect, useRef, useState } from 'react';
import { loadPaymentWidget } from '@tosspayments/payment-widget-sdk';
import { Button, message } from 'antd';

// 발급받은 테스트 클라이언트 키
const clientKey = process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY;

export default function AdvertisePayment({ adId, amount, adTitle }) {
  const paymentWidgetRef = useRef(null);
  const paymentMethodsWidgetRef = useRef(null);
  
  const [price, setPrice] = useState(amount || 50000); // 결제 금액
  const [isReady, setIsReady] = useState(false); // 🌟 결제창 렌더링 완료 상태

  useEffect(() => {
    // 🌟 컴포넌트 마운트 시 고객 ID 고정 (무한 렌더링 및 키 변경 방지)
    const customerKey = "customer_" + new Date().getTime(); 

    const initializeWidget = async () => {
      try {
        // 1. 결제 위젯 초기화
        const paymentWidget = await loadPaymentWidget(clientKey, customerKey);

        // 2. 결제 수단 위젯 렌더링
        const paymentMethodsWidget = paymentWidget.renderPaymentMethods(
          "#payment-method",
          { value: price },
          { variantKey: "DEFAULT" }
        );

        // 3. 이용약관 위젯 렌더링
        paymentWidget.renderAgreement(
          "#agreement", 
          { variantKey: "AGREEMENT" }
        );

        // 🌟 4. 결제 수단 UI 렌더링 완료 시 버튼 활성화
        paymentMethodsWidget.on("ready", () => {
          setIsReady(true);
        });

        paymentWidgetRef.current = paymentWidget;
        paymentMethodsWidgetRef.current = paymentMethodsWidget;

      } catch (error) {
        console.error("결제 위젯 초기화 실패:", error);
        message.error("결제창을 불러오는 데 실패했습니다.");
      }
    };

    initializeWidget();
  }, []); // 의존성 배열을 비워서 최초 1회만 실행

  // 금액 변경 시 결제 위젯 업데이트
  useEffect(() => {
    const paymentMethodsWidget = paymentMethodsWidgetRef.current;
    if (paymentMethodsWidget == null) {
      return;
    }
    paymentMethodsWidget.updateAmount(price);
  }, [price]);

  // 실제 결제 버튼 클릭 시 실행
  const handlePayment = async () => {
    const paymentWidget = paymentWidgetRef.current;

    // 위젯이 없거나 렌더링 전이라면 차단
    if (!paymentWidget || !isReady) {
      message.warning("결제창이 준비 중입니다. 잠시만 기다려주세요.");
      return;
    }

    try {
      // 결제창 띄우기
      await paymentWidget.requestPayment({
        orderId: `AD_${adId}_${new Date().getTime()}`, // 고유한 주문번호 생성
        orderName: adTitle || '광고 결제', // 결제 이름
        customerName: '홍길동', // 실제 로그인 유저 이름으로 대체 필요
        customerEmail: 'customer@email.com',
        // 성공 및 실패 시 리다이렉트 될 URL
        successUrl: `${window.location.origin}/user/mypage/advertiseSuccess`,
        failUrl: `${window.location.origin}/user/mypage/advertiseSuccess`,
      });
    } catch (err) {
      console.error(err);
      
      // 🌟 사용자가 결제창을 X 버튼으로 닫은 경우 에러 방어
      if (err.code === "USER_CANCEL") {
        message.warning('결제가 취소되었습니다.');
      } else {
        message.error('결제 요청 중 오류가 발생했습니다.');
      }
    }
  };

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
      <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>광고 결제</h2>
      
      {/* 결제 수단 UI가 들어갈 자리 */}
      <div id="payment-method" />
      {/* 이용 약관 UI가 들어갈 자리 */}
      <div id="agreement" style={{ marginTop: '10px' }} />

      <Button 
        type="primary" 
        size="large" 
        block 
        onClick={handlePayment}
        style={{ marginTop: '20px' }}
        disabled={!isReady} // 렌더링 전까지 버튼 클릭 금지
        loading={!isReady}  // 버튼에 로딩 스피너 표시
      >
        {isReady ? `${price.toLocaleString()}원 결제하기` : '결제 모듈 로딩 중...'}
      </Button>
    </div>
  );
}