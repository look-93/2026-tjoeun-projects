import { useEffect, useRef, useState } from 'react';
import { loadPaymentWidget } from '@tosspayments/payment-widget-sdk';
import { Button, message, Spin } from 'antd';

const clientKey = process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY;

export default function AdvertisePayment({ adId, amount, adTitle }) {
  const paymentWidgetRef = useRef(null);
  const paymentMethodsWidgetRef = useRef(null);
  
  const [price, setPrice] = useState(amount || 50000);
  const [isReady, setIsReady] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let isMounted = true;
    
    // 🌟 괄호 위치 수정 완료!
    const customerKey = `customer_${adId}_${Math.random().toString(36).substring(2, 9)}`;

    const initializeWidget = async () => {
      try {
        setLoading(true);

        const paymentWidget = await loadPaymentWidget(clientKey, customerKey);

        if (!isMounted) return;

        const paymentMethodsWidget = paymentWidget.renderPaymentMethods(
          "#payment-method",
          { value: price },
          { variantKey: "DEFAULT" }
        );

        paymentWidget.renderAgreement(
          "#agreement", 
          { variantKey: "AGREEMENT" }
        );

        paymentMethodsWidget.on("ready", () => {
          if (isMounted) {
            setIsReady(true);
            setLoading(false);
          }
        });

        paymentWidgetRef.current = paymentWidget;
        paymentMethodsWidgetRef.current = paymentMethodsWidget;

      } catch (error) {
        if (isMounted) {
          console.error("결제 위젯 초기화 실패:", error);
          message.error("결제창을 불러오는 데 실패했습니다.");
          setLoading(false);
        }
      }
    };

    const timer = setTimeout(() => {
      initializeWidget();
    }, 100);

    return () => {
      isMounted = false;
      clearTimeout(timer);
    };
  }, [adId]);

  useEffect(() => {
    const paymentMethodsWidget = paymentMethodsWidgetRef.current;
    if (paymentMethodsWidget == null) return;
    paymentMethodsWidget.updateAmount(price);
  }, [price]);

  const handlePayment = async () => {
    const paymentWidget = paymentWidgetRef.current;

    if (!paymentWidget || !isReady) {
      message.warning("결제창이 준비 중입니다. 잠시만 기다려주세요.");
      return;
    }

    try {
      await paymentWidget.requestPayment({
        orderId: `AD_${adId}_${new Date().getTime()}`,
        orderName: adTitle || '광고 결제',
        customerName: '광고주',
        customerEmail: 'advertiser@moit.com',
        successUrl: `${window.location.origin}/user/mypage/advertiseSuccess`,
        failUrl: `${window.location.origin}/user/mypage/advertiseSuccess`,
      });
    } catch (err) {
      console.error(err);
      if (err.code === "USER_CANCEL") {
        message.warning('결제가 취소되었습니다.');
      } else {
        message.error('결제 요청 중 오류가 발생했습니다.');
      }
    }
  };

  return (
    <div style={{ maxWidth: '600px', margin: '0 auto', padding: '20px', minHeight: '350px' }}>
      <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>광고 결제</h2>
      
      {loading && (
        <div style={{ textAlign: 'center', padding: '50px 0' }}>
          <Spin size="large" tip="결제창을 불러오는 중입니다..." />
        </div>
      )}

      <div id="payment-method" style={{ display: loading ? 'none' : 'block' }} />
      <div id="agreement" style={{ marginTop: '10px', display: loading ? 'none' : 'block' }} />

      {!loading && (
        <Button 
          type="primary" 
          size="large" 
          block 
          onClick={handlePayment}
          style={{ marginTop: '20px' }}
          disabled={!isReady}
        >
          {`${price.toLocaleString()}원 결제하기`}
        </Button>
      )}
    </div>
  );
}