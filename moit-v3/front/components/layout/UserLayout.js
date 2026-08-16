import React from 'react';
import { Layout } from 'antd';
import PropTypes from 'prop-types';
import { useRouter } from 'next/router';
import UserHeader from './UserHeader';
import UserFooter from './UserFooter';
import MypageHeader from './MypageHeader';
import MyPageSidebar from './MyPageSidebar';

const { Content } = Layout;

function UserLayout({ children }) {
  const router = useRouter();
  // 현재 주소에 /mypage가 포함되어 있는지 확인
  const isMypage = router.pathname.includes('/mypage');
  return (
    <Layout className="moit-layout">
      <UserHeader />

      {isMypage ? (
        <>
          <MypageHeader />

          <Layout className="mypage-body">
            <MyPageSidebar />

            <Content className="moit-content">{children}</Content>
          </Layout>
        </>
      ) : (
        <Content className="moit-content">{children}</Content>
      )}

      <UserFooter />
    </Layout>
  );
}

UserLayout.propTypes = {
  children: PropTypes.node.isRequired,
};

export default UserLayout;
