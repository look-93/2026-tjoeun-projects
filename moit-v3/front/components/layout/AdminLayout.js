import React from 'react';
import { Layout } from 'antd';
import PropTypes from 'prop-types';

import AdminSidebar from './AdminSidebar';
import AdminHeader from './AdminHeader';

const { Content } = Layout;

function AdminLayout({ children }) {
  return (
    <Layout className="admin-layout">
      <AdminSidebar />

      <Layout className="admin-main">
        <Content className="admin-content">
          <AdminHeader />

          {children}
        </Content>
      </Layout>
    </Layout>
  );
}

AdminLayout.propTypes = {
  children: PropTypes.node.isRequired,
};

export default AdminLayout;