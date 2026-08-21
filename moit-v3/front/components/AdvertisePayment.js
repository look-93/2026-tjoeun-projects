import { useEffect, useRef, useState } from 'react';
import { loadPaymentWidget } from '@tosspayments/payment-widget-sdk';
import { Button, message } from 'antd';

// TODO: 토스페이먼츠 개발자 센터에서 발급받은 테스트 클라이언트 키로 변경하세요!
const clientKey = "test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq"; 

// 고유한 고객 ID (비회원 결제라면 ANONYMOUS)
const customerKey = "customer_" + new Date().getTime(); 

export default function AdvertisePayment({ adId, amount, adTitle }) {
  const paymentWidgetRef = useRef(null);
  const paymentMethodsWidgetRef = useRef(null);
  const [price, setPrice] = useState(amount || 50000); // 결제 금액

  useEffect(() => {
    (async () => {
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

      paymentWidgetRef.current = paymentWidget;
      paymentMethodsWidgetRef.current = paymentMethodsWidget;
    })();
  }, []);

  useEffect(() => {
    const paymentMethodsWidget = paymentMethodsWidgetRef.current;
    if (paymentMethodsWidget == null) {
      return;
    }
    // 금액이 바뀌면 결제 위젯에도 업데이트
    paymentMethodsWidget.updateAmount(price);
  }, [price]);

  const handlePayment = async () => {
    const paymentWidget = paymentWidgetRef.current;

    try {
      // 결제창 띄우기
      await paymentWidget.requestPayment({
        orderId: `AD_${adId}_${new Date().getTime()}`, // 고유한 주문번호 생성
        orderName: adTitle || '광고 결제', // 결제 이름
        customerName: '홍길동', // 로그인한 사용자 이름으로 변경
        customerEmail: 'customer@email.com',
        // 성공 및 실패 시 리다이렉트 될 URL
        successUrl: `${window.location.origin}/advertise/payment/success`,
        failUrl: `${window.location.origin}/advertise/payment/fail`,
      });
    } catch (err) {
      console.error(err);
      message.error('결제창 호출에 실패했습니다.');
    }
  };

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
      <h2>광고 결제 (테스트)</h2>
      
      {/* 결제 UI가 들어갈 자리 */}
      <div id="payment-method" />
      <div id="agreement" />

      <Button 
        type="primary" 
        size="large" 
        block 
        onClick={handlePayment}
        style={{ marginTop: '20px' }}
      >
        {price.toLocaleString()}원 결제하기
      </Button>
    </div>
  );
}