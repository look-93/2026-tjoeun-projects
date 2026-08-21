// constants/advertisePrice.js

// 🌟 신규 광고 가격 (기본 단가: 일반 1만원/일, 프리미엄 2만원/일 -> 장기 결제시 최대 25% 할인)
export const NEW_PRICES = [
  {
    key: 'new-1',
    days: 1,
    generalPrice: 10000,
    premiumPrice: 20000,
  },
  {
    key: 'new-7',
    days: 7,
    generalPrice: 65000,     // 약 7% 할인
    premiumPrice: 130000,
  },
  {
    key: 'new-14',
    days: 14,
    generalPrice: 125000,    // 약 10% 할인
    premiumPrice: 250000,
  },
  {
    key: 'new-30',
    days: 30,
    generalPrice: 250000,    // 약 16% 할인
    premiumPrice: 500000,
  },
  {
    key: 'new-60',
    days: 60,
    generalPrice: 480000,    // 약 20% 할인
    premiumPrice: 960000,
  },
  {
    key: 'new-90',
    days: 90,
    generalPrice: 670000,    // 약 25% 할인
    premiumPrice: 1350000,
  },
];

// 🌟 연장 광고 가격 (신규 대비 약 10% 추가 할인 적용 -> 광고주 유지 목적)
export const EXTENSION_PRICES = [
  {
    key: 'extension-7',
    days: 7,
    generalPrice: 60000,
    premiumPrice: 120000,
  },
  {
    key: 'extension-14',
    days: 14,
    generalPrice: 110000,
    premiumPrice: 220000,
  },
  {
    key: 'extension-30',
    days: 30,
    generalPrice: 220000,
    premiumPrice: 450000,
  },
  {
    key: 'extension-60',
    days: 60,
    generalPrice: 420000,
    premiumPrice: 850000,
  },
  {
    key: 'extension-90',
    days: 90,
    generalPrice: 600000,
    premiumPrice: 1200000,
  },
];

// 광고 위치별 추가금 (노출도와 클릭률을 고려한 차등 가격)
export const POSITION_PRICES = [
  {
    key: 'MEETUP_LIST_BANNER',
    label: '모집목록 배너',     // (중간)
    price: 30000,
  },
  {
    key: 'MEETUP_LIST_SIDEBAR',
    label: '모집목록 사이드',   // (저렴)
    price: 15000,
  },
  {
    key: 'MEETUP_DETAIL_SIDEBAR',
    label: '모임 상세 사이드',  // (가장 저렴)
    price: 10000,
  },
];