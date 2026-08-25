import React , { useEffect }from 'react';
import { Layout } from 'antd';
import PropTypes from 'prop-types';
import { useRouter } from 'next/router';
import { useSelector,useDispatch } from 'react-redux';

import UserHeader from './UserHeader';
import UserFooter from './UserFooter';
import MypageHeader from './MypageHeader';
import MyPageSidebar from './MyPageSidebar';

import {
  getMyPageRequest,
} from '../../reducers/userReducer';

const { Content } = Layout;

function UserLayout({ children }) {
  const router = useRouter();
  const dispatch = useDispatch();

  const isMypage = router.pathname.includes('/mypage');

  // Redux 회원정보
  const user = useSelector((state) => state.user?.user);

  useEffect(() => {
    if (isMypage) {dispatch(getMyPageRequest());}
  }, [isMypage, dispatch]);

  return (
    <Layout className="moit-layout">

      <UserHeader />

      {isMypage ? (
        <>
          {/* 마이페이지 프로필 */}
          <MypageHeader user={user} />

          <Layout className="mypage-body">

            {/* 마이페이지 사이드바 */}
            <MyPageSidebar />

            <Content className="moit-content">
              {children}
            </Content>

          </Layout>
        </>
      ) : (
        <Content className="moit-content">
          {children}
        </Content>
      )}

      <UserFooter />

    </Layout>
  );
}

UserLayout.propTypes = {
  children: PropTypes.node.isRequired,
};

export default UserLayout;