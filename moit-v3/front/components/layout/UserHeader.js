import React, { useState } from 'react';
import { Layout, Row, Col, Avatar, Badge, Button, Divider, Drawer } from 'antd';
import {
  BellOutlined,
  MessageOutlined,
  UserOutlined,
  MenuOutlined,
} from '@ant-design/icons';
import Link from 'next/link';

const { Header } = Layout;
const { useBreakpoint } = require('antd').Grid;

function UserHeader() {
  const screens = useBreakpoint();
  const [drawerOpen, setDrawerOpen] = useState(false);

  return (
    <>
      <Header className="moit-header">
        <Row
          align="middle"
          justify="space-between"
          wrap={false}
          className="moit-header-row"
        >
          {/* 로고 + 슬로건 */}
          <Col flex="none">
            <Row align="middle" gutter={32} wrap={false}>
              {/* 로고 */}
              <Col flex="none">
                <Link href="/">
                  <a className="moit-logo">MOIT</a>
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

          {/* PC MENU */}
          {screens.md && (
            <Col flex="none">
              <Row align="middle" gutter={24} wrap={false}>
                {/* 모집찾기 */}
                <Col flex="none">
                  <Link href="/meetup/list">
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
                  <Link href="/questions/write">
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
                  <Badge count={3} size="small">
                    <BellOutlined className="moit-alarm-icon" />
                  </Badge>
                </Col>

                {/* 프로필 */}
                <Col flex="none">
                  <Row align="middle" gutter={8} wrap={false}>
                    <Col flex="none">
                      <Avatar size={38} icon={<UserOutlined />} />
                    </Col>

                    <Col flex="none">
                      <div className="moit-profile-info">
                        <span className="moit-profile-name">예진님</span>

                        <span className="moit-profile-type">일반회원</span>
                      </div>
                    </Col>
                  </Row>
                </Col>

                {/* 로그아웃 */}
                <Col flex="none">
                  <Button type="text" className="moit-logout-btn">
                    로그아웃
                  </Button>
                </Col>
              </Row>
            </Col>
          )}

          {/* MOBILE MENU */}
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

      {/* MOBILE DRAWER */}
      <Drawer
        title="MOIT"
        placement="right"
        open={drawerOpen}
        onClose={() => setDrawerOpen(false)}
      >
        <div className="moit-mobile-menu">
          <Link href="/meetup/list">
            <a>모집찾기</a>
          </Link>

          <Link href="/questions/write">
            <a>
              <MessageOutlined />
              &nbsp;관리자 1:1 문의
            </a>
          </Link>

          <Divider />

          <Link href="/mypage">
            <a>
              <UserOutlined />
              &nbsp;마이페이지
            </a>
          </Link>

          <Button type="text" className="moit-mobile-menu-item">
            <BellOutlined />
            &nbsp;알림
          </Button>

          <Button type="text" danger className="moit-mobile-menu-item">
            로그아웃
          </Button>
        </div>
      </Drawer>
    </>
  );
}

export default UserHeader;
