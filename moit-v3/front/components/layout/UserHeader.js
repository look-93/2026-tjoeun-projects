import React, { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { logoutRequest } from '../../reducers/userReducer';
import {
  BellOutlined,
  MessageOutlined,
  UserOutlined,
  MenuOutlined,
} from '@ant-design/icons';

import {
  Layout,
  Row,
  Col,
  Avatar,
  Badge,
  Button,
  Divider,
  Drawer,
  message,
  Grid,
} from 'antd';

import Link from 'next/link';
import { useRouter } from 'next/router';

import api from '../../api/axios';

const { Header } = Layout;
const { useBreakpoint } = Grid;

function UserHeader() {
  const screens = useBreakpoint();
  const router = useRouter();
  const dispatch = useDispatch();

  const [drawerOpen, setDrawerOpen] = useState(false);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // =========================================================
  // 로그인 사용자 조회
  // =========================================================
  const loadUser = async () => {
    try {
      // SSR 방지
      if (typeof window === 'undefined') {
        return;
      }

      const accessToken = localStorage.getItem('accessToken');

      console.log('===== HEADER USER CHECK =====');
      console.log('accessToken 존재:', !!accessToken);

      // 토큰이 없으면 로그인 전
      if (!accessToken) {
        setUser(null);
        return;
      }

      // 현재 로그인 사용자 조회
      const response = await api.get('/api/members/me');

      console.log('===== HEADER USER =====');
      console.log(response.data);

      setUser(response.data);

    } catch (error) {
      console.error('회원정보 조회 실패:', error);

      setUser(null);

      // 401이면 토큰 삭제
      if (error.response?.status === 401) {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
      }

    } finally {
      setLoading(false);
    }
  };

  // =========================================================
  // 최초 실행 + 페이지 이동할 때마다 사용자 정보 다시 조회
  // =========================================================
  useEffect(() => {
    setLoading(true);
    loadUser();
  }, [router.asPath]);

  // =========================================================
  // 회원 유형
  // =========================================================
  const getMemberTypeName = (memberTypeId) => {
    switch (Number(memberTypeId)) {
      case 1:
        return '일반회원';

      case 2:
        return '제휴업체';

      case 3:
        return '관리자';

      case 4:
        return '최고관리자';

      default:
        return '회원';
    }
  };

  // =========================================================
  // 로그아웃
  // =========================================================
  const handleLogout = () => {
    // 1. 프론트 토큰 즉시 삭제
    if (typeof window !== 'undefined') {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
    }

    // 2. 헤더 사용자 상태 즉시 제거
    setUser(null);

    // 3. 모바일 Drawer 닫기
    setDrawerOpen(false);

    // 4. Redux 로그아웃 요청
    dispatch(logoutRequest());

    // 5. 메시지
    message.success('로그아웃되었습니다.');

    // 6. 로그인 페이지 이동
    router.push('/user/member/login');
  };

  // =========================================================
  // 로딩 중
  // =========================================================
  if (loading) {
    return (
      <Header className="moit-header">
        <Row
          align="middle"
          justify="space-between"
          wrap={false}
          className="moit-header-row"
        >
          <Col flex="none">
            <Link href="/">
              <a className="moit-logo">
                MOIT
              </a>
            </Link>
          </Col>
        </Row>
      </Header>
    );
  }

  return (
    <>
      {/* =====================================================
          HEADER
      ===================================================== */}
      <Header className="moit-header">

        <Row
          align="middle"
          justify="space-between"
          wrap={false}
          className="moit-header-row"
        >

          {/* =================================================
              로고 + 슬로건
          ================================================= */}
          <Col flex="none">

            <Row
              align="middle"
              gutter={32}
              wrap={false}
            >

              {/* 로고 */}
              <Col flex="none">
                <Link href="/">
                  <a className="moit-logo">
                    MOIT
                  </a>
                </Link>
              </Col>

              {/* 슬로건 */}
              {screens.md && (
                <Col flex="none">
                  <span className="moit-slogan">
                    우리들의 취향 맞춤 소모임 플랫폼
                  </span>
                </Col>
              )}

            </Row>

          </Col>


          {/* =================================================
              PC MENU
          ================================================= */}
          {screens.md && (

            <Col flex="none">

              <Row
                align="middle"
                gutter={24}
                wrap={false}
              >

                {/* 모집찾기 */}
                <Col flex="none">
                  <Link href="/user/meetup">
                    <a
                      className="moit-header-link"
                      style={{ textDecoration: 'none' }}
                    >
                      모집찾기
                    </a>
                  </Link>
                </Col>


                {/* 관리자 문의 */}
                <Col flex="none">
                  <Link href="/user/qna/write?type=ADMIN">
                    <a
                      className="moit-header-link moit-inquiry"
                      style={{ textDecoration: 'none' }}
                    >
                      <MessageOutlined />
                      &nbsp;관리자 1:1 문의
                    </a>
                  </Link>
                </Col>


                {/* 알림 */}
                <Col flex="none">
                  <Badge
                    count={3}
                    size="small"
                  >
                    <BellOutlined className="moit-alarm-icon" />
                  </Badge>
                </Col>


                {/* =================================================
                    로그인 상태
                ================================================= */}
                {user ? (

                  <>
                    {/* 프로필 */}
                    <Col flex="none">
                      <div
                        onClick={() => router.push('/user/member/mypage')}
                        style={{
                          cursor: 'pointer',
                        }}
                      >
                        <Row
                          align="middle"
                          gutter={8}
                          wrap={false}
                        >
                          {/* 프로필 이미지 */}
                          <Col flex="none">
                            <Avatar
                              size={38}
                              src={user.profileUrl || undefined}
                              icon={
                                !user.profileUrl && (
                                  <UserOutlined />
                                )
                              }
                            />
                          </Col>

                          {/* 이름 + 회원유형 */}
                          <Col flex="none">
                            <div className="moit-profile-info">
                              <span className="moit-profile-name">
                                {user.nickname}님
                              </span>

                              <span className="moit-profile-type">
                                {getMemberTypeName(user.memberTypeId)}
                              </span>
                            </div>
                          </Col>
                        </Row>
                      </div>
                    </Col>


                    {/* 로그아웃 */}
                    <Col flex="none">

                      <Button
                        type="text"
                        className="moit-logout-btn"
                        onClick={handleLogout}
                      >
                        로그아웃
                      </Button>

                    </Col>

                  </>

                ) : (

                  <>
                    {/* =================================================
                        로그인 전
                    ================================================= */}

                    {/* 로그인 */}
                    <Col flex="none">
                      <Link href="/user/member/login">
                        <a
                          className="moit-header-link"
                          style={{ textDecoration: 'none' }}
                        >
                          로그인
                        </a>
                      </Link>
                    </Col>


                    {/* 회원가입 */}
                    <Col flex="none">
                      <Link href="/user/member/signup">
                        <a
                          className="moit-header-link"
                          style={{ textDecoration: 'none' }}
                        >
                          회원가입
                        </a>
                      </Link>
                    </Col>
                  </>

                )}

              </Row>

            </Col>

          )}


          {/* =================================================
              MOBILE MENU
          ================================================= */}
          {!screens.md && (

            <Col flex="none">

              <Button
                type="text"
                className="moit-mobile-menu-btn"
                icon={<MenuOutlined />}
                onClick={() => setDrawerOpen(true)}
              />

            </Col>

          )}

        </Row>

      </Header>


      {/* =====================================================
          MOBILE DRAWER
      ===================================================== */}
      <Drawer
        title="MOIT"
        placement="right"
        open={drawerOpen}
        onClose={() => setDrawerOpen(false)}
      >

        <div className="moit-mobile-menu">

          {/* 모집찾기 */}
          <Link href="/user/meetup">
            <a>
              모집찾기
            </a>
          </Link>


          {/* 관리자 문의 */}
          <Link href="/user/qna/write?type=ADMIN">
            <a>
              <MessageOutlined />
              &nbsp;관리자 1:1 문의
            </a>
          </Link>


          <Divider />


          {user ? (

            <>
              {/* 마이페이지 */}
              <Link href="/user/mypage">
                <a>
                  <UserOutlined />
                  &nbsp;마이페이지
                </a>
              </Link>


              {/* 알림 */}
              <Button
                type="text"
                className="moit-mobile-menu-item"
              >
                <BellOutlined />
                &nbsp;알림
              </Button>


              {/* 로그아웃 */}
              <Button
                type="text"
                danger
                className="moit-mobile-menu-item"
                onClick={handleLogout}
              >
                로그아웃
              </Button>
            </>

          ) : (

            <>
              {/* 로그인 */}
              <Link href="/user/member/login">
                <a className="moit-mobile-menu-item">
                  로그인
                </a>
              </Link>


              {/* 회원가입 */}
              <Link href="/user/member/signup">
                <a className="moit-mobile-menu-item">
                  회원가입
                </a>
              </Link>
            </>

          )}

        </div>

      </Drawer>
    </>
  );
}

export default UserHeader;