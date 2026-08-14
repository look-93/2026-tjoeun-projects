import React from 'react';
import { Layout, Menu } from 'antd';
import {
  UserOutlined,
  TeamOutlined,
  MessageOutlined,
  StarOutlined,
  WarningOutlined,
  NotificationOutlined,
} from '@ant-design/icons';
import Link from 'next/link';
import { useRouter } from 'next/router';

const { Sider } = Layout;

function AdminSidebar() {
  const router = useRouter();

  const menuItems = [
    {
      key: '/admin/member',
      icon: <UserOutlined />,
      label: <Link href="/admin/member">회원관리</Link>,
    },
    {
      key: '/admin/meetup',
      icon: <TeamOutlined />,
      label: <Link href="/admin/meetup/list">모임관리</Link>,
    },
    {
      key: '/questions/admin',
      icon: <MessageOutlined />,
      label: <Link href="/questions/admin">문의관리</Link>,
    },
    {
      key: '/admin/review',
      icon: <StarOutlined />,
      label: <Link href="/admin/review/list">후기관리</Link>,
    },
    {
      key: '/admin/report',
      icon: <WarningOutlined />,
      label: <Link href="/admin/report/adminList">신고관리</Link>,
    },
    {
      key: '/admin/advertisement',
      icon: <NotificationOutlined />,
      label: (
        <Link href="/admin/advertisement/manageList">
          광고관리
        </Link>
      ),
    },
  ];

  // 현재 URL에 해당하는 메뉴 활성화
  const selectedKey =
    menuItems.find((item) =>
      router.pathname.startsWith(item.key)
    )?.key || '';

  return (
    <Sider
      width={240}
      className="admin-sidebar"
      theme="light"
    >
      {/* LOGO */}
      <div className="admin-logo">
        MOIT
      </div>

      {/* MENU */}
      <Menu
        mode="inline"
        theme="light"
        selectedKeys={[selectedKey]}
        items={menuItems}
        className="admin-menu"
      />
    </Sider>
  );
}

export default AdminSidebar;