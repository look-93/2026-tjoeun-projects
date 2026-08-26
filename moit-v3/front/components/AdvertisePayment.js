import { useEffect, useRef, useState } from 'react';
import { loadPaymentWidget } from '@tosspayments/payment-widget-sdk';
import { Button, message, Spin } from 'antd';
import { createInitialPayment, } from '../api/advertiseApi';

const clientKey = process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY;

export default function AdvertisePayment({ adId, amount, adTitle }) {
  const paymentWidgetRef = useRef(null);
  const paymentMethodsWidgetRef = useRef(null);
  
  const [price, setPrice] = useState(amount);
  const [isReady, setIsReady] = useState(false);
  const [loading, setLoading] = useState(true);
  const [realOrderId, setRealOrderId] = useState('');

  useEffect(() => {
    let isMounted = true;
    
    const initializePayment = async () => {
      try {
        setLoading(true);

        /*
         * 1. 백엔드에서 결제 정보 생성
         *
         * 이미 REQUESTED 결제가 있으면
         * 기존 결제 정보를 재사용함.
         */
        const paymentResponse = await createInitialPayment(adId);

        if (!isMounted) {
          return;
        }

        const payment = paymentResponse.data;

        console.log(
          '백엔드에서 생성/조회한 결제 정보:',
          payment
        );

        /*
         * 2. 백엔드에서 받은 실제 orderId 사용
         */
        setRealOrderId(payment.orderId);

        /*
         * 3. 실제 결제 금액도 백엔드 금액 사용
         *
         * 프론트에서 전달받은 amount를 그대로 믿지 않고
         * 서버가 계산한 결제 금액을 사용
         */
        const serverAmount = Number(payment.amount);

        if (!serverAmount || serverAmount <= 0) {
          throw new Error('서버에서 올바른 결제 금액을 받지 못했습니다.');
        }

        setPrice(serverAmount);

        /*
         * 4. Toss 고객 키
         *
         * 같은 광고의 결제창을 다시 열어도
         * 고객 식별이 가능하도록 adId 기반으로 생성
         */
        const customerKey = `customer_${adId}`;

        // 5. Toss Payment Widget 생성
        const paymentWidget =
          await loadPaymentWidget(
            clientKey,
            customerKey
          );

        if (!isMounted) {
          return;
        }

        // 6. 결제수단 렌더링
        const paymentMethodsWidget =
          paymentWidget.renderPaymentMethods(
            '#payment-method',
            {
              value: Number(payment.amount),
            },
            {
              variantKey: 'DEFAULT',
            }
          );

        // 7. 약관 렌더링
        paymentWidget.renderAgreement(
          '#agreement',
          {
            variantKey: 'AGREEMENT',
          }
        );

        // 8. 결제창 준비 완료
        paymentMethodsWidget.on(
          'ready',
          () => {
            if (!isMounted) {
              return;
            }
            setIsReady(true);
            setLoading(false);
          }
        );

        paymentWidgetRef.current = paymentWidget;
        paymentMethodsWidgetRef.current = paymentMethodsWidget;
      } catch (error) {

        if (!isMounted) {
          return;
        }

        console.error(
          '결제 초기화 실패:',
          error
        );

        console.error(
          '응답:',
          error.response?.data
        );

        message.error(
          error.response?.data?.message ||
          '결제 정보를 생성하지 못했습니다.'
        );

        setLoading(false);
      }
    };

    if (adId) {
      initializePayment();
    }

    return () => {
      isMounted = false;
    };

  }, [adId]);

  useEffect(() => {
    const paymentMethodsWidget = paymentMethodsWidgetRef.current;
    if (paymentMethodsWidget == null) return;
    paymentMethodsWidget.updateAmount(Number(price));
  }, [price]);

  const handlePayment = async () => {
    const paymentWidget = paymentWidgetRef.current;

    if (!paymentWidget || !isReady) {
      message.warning("결제창이 준비 중입니다. 잠시만 기다려주세요.");
      return;
    }

    if (!realOrderId) {

      message.error(
        '주문번호를 확인할 수 없습니다.'
      );

      return;
    }

    try {
      await paymentWidget.requestPayment({
        orderId: realOrderId,
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