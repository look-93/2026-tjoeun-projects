import React from 'react';
import { Layout } from 'antd';
import PropTypes from 'prop-types';

import UserHeader from './UserHeader';
import UserFooter from './UserFooter';

const { Content } = Layout;

function UserLayout({ children }) {
  return (
    <Layout className="moit-layout">
      <UserHeader />

      <Content className="moit-content">{children}</Content>

      <UserFooter />
    </Layout>
  );
}

UserLayout.propTypes = {
  children: PropTypes.node.isRequired,
};

export default UserLayout;
