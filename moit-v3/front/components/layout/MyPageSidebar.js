import React from 'react';
import { Layout, Menu } from 'antd';
import { useRouter } from 'next/router';

const { Sider } = Layout;

function MyPageSidebar() {
  const router = useRouter();

  const menuItems = [
    {
      key: '/user/member/mypage',
      label: '내 정보',
    },
    {
      key: '/user/mypage/meetup',
      label: '내 모임',
    },
    {
      key: '/user/mypage/meetup-apply',
      label: '내 신청모임',
    },
    {
      key: '/user/mypage/review',
      label: '내 작성후기',
    },
    {
      key: '/user/mypage/question',
      label: '내 문의내역',
    },
    {
      key: '/user/mypage/report',
      label: '내 신고내역',
    },
    {
      key: '/user/member/mypage/member/edit',
      label: '회원정보 수정',
    },
    {
      key: '/user/mypage/member/password-change',
      label: '비밀번호 변경',
    },
    {
      key: '/user/mypage/member/delete',
      label: '회원 탈퇴',
    },
  ];

  const selectedKey =
    menuItems.find((item) => router.pathname === item.key)?.key ||
    menuItems.find((item) => router.pathname.startsWith(`${item.key}/`))?.key;

  return (
    <Sider width={220} theme="light" className="mypage-sidebar">
      <Menu
        mode="vertical"
        selectedKeys={selectedKey ? [selectedKey] : []}
        items={menuItems}
        onClick={({ key }) => router.push(key)}
      />
    </Sider>
  );
}

export default MyPageSidebar;
