import React from 'react';
import { Layout, Row, Col, Divider } from 'antd';

const { Footer } = Layout;

function UserFooter() {
  return (
    <Footer className="moit-footer">
      <div className="moit-footer-inner">
        <Divider className="moit-footer-divider" />

        <Row align="center" gutter={[24, 12]}>
          <Col>
            <a href="#">회사소개</a>
          </Col>

          <Col>
            <a href="#">인재채용</a>
          </Col>

          <Col>
            <a href="#">제휴제안</a>
          </Col>

          <Col>
            <a href="#">이용약관</a>
          </Col>

          <Col>
            <a href="#" className="moit-policy">
              개인정보처리방침
            </a>
          </Col>

          <Col>
            <a href="#">청소년보호정책</a>
          </Col>

          <Col>
            <a href="#">모잇 정책</a>
          </Col>

          <Col>
            <a href="#">고객센터</a>
          </Col>
        </Row>

        <Row align="center" className="moit-footer-copyright">
          <span>© MOIT Corp.</span>
        </Row>
      </div>
    </Footer>
  );
}

export default UserFooter;
