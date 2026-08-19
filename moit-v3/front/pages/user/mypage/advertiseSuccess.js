import { useEffect } from 'react';
import { useRouter } from 'next/router';
import { Result, Button } from 'antd';
import axios from '../../../api/axios'; // 백엔드 API 호출용

export default function PaymentSuccessPage() {
  const router = useRouter();
  const { paymentKey, orderId, amount } = router.query;

  useEffect(() => {
    if (!paymentKey || !orderId || !amount) return;

    // TODO: 백엔드로 결제 승인 요청 보내기
    // 토스는 프론트엔드에서 결제가 성공해도, 
    // 백엔드에서 최종 승인(confirm) API를 호출해야 결제가 확정됩니다!
    console.log("백엔드로 보낼 데이터:", { paymentKey, orderId, amount });
    
    // axios.post('/api/payment/confirm', { paymentKey, orderId, amount })
    //   .then(() => message.success("결제 최종 완료!"))
    //   .catch(() => message.error("결제 검증 실패"));

  }, [paymentKey, orderId, amount]);

  return (
    <div style={{ padding: '50px' }}>
      <Result
        status="success"
        title="결제가 성공적으로 완료되었습니다!"
        subTitle={`주문번호: ${orderId} / 결제금액: ${amount}원`}
        extra={[
          <Button type="primary" key="console" onClick={() => router.push('/')}>
            홈으로 가기
          </Button>,
        ]}
      />
    </div>
  );
}