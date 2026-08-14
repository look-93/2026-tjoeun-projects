import { Row, Col, Button, Input, Select, Table } from "antd";
import AdminStatCard from "../../components/AdminStatCard";
import { useState } from "react";
// http://localhost:3000/admin/sample

//필독!!!!
/*
1. 샘플페이지의 전체 폭을 그대로 맞출 것
2. 필요한 UI는 Ant Design으로 구현
3. 디자인이 필요한 부분은 요청하면 만들어줄 수 있음
*/
// https://ant.design/components/overview/

export default function AdminSamplePage() {
  //테스트용 테이터
  const stats = [
    { title: "전체 모임", value: 1200, suffix: "개" },
    { title: "모집 중", value: 1100, suffix: "개" },
    { title: "모집 마감", value: 100, suffix: "개" },
    { title: "모집 마감", value: 100, suffix: "개" }
  ];
  const adminColumns = [
    {
      title: "번호",
      dataIndex: "id",
      key: "id",
      width: 80,
      align: "center",
    },
    {
      title: "아이디",
      dataIndex: "loginId",
      key: "loginId",
    },
    {
      title: "닉네임",
      dataIndex: "nickname",
      key: "nickname",
    },
    {
      title: "이름",
      dataIndex: "name",
      key: "name",
    },
    {
      title: "이메일",
      dataIndex: "email",
      key: "email",
    },
    {
      title: "가입일",
      dataIndex: "createdAt",
      key: "createdAt",
      align: "center",
    },
    {
      title: "상태",
      dataIndex: "status",
      key: "status",
      align: "center",
      render: (_, record) => (
      <div style={{ display: "flex", gap: 8, justifyContent: "center" }}>
        <Button size="small">
          수정
        </Button>

        <Button
          size="small"
          danger
        >
          삭제
        </Button>
      </div>
    ),
    },
  ];
  const adminData = [
    {
      key: 1,
      id: 1,
      loginId: "admin01",
      nickname: "관리자1",
      name: "김관리",
      email: "admin01@moit.com",
      createdAt: "2026-08-01",
      status: "정상",
    },
    {
      key: 2,
      id: 2,
      loginId: "admin02",
      nickname: "관리자2",
      name: "이관리",
      email: "admin02@moit.com",
      createdAt: "2026-08-03",
      status: "정상",
    },
  ];
  const [listType, setListType] = useState("admin");

  const [checkStrictly, setCheckStrictly] = useState(false);

  const rowSelection = {
    checkStrictly,
    onChange: (selectedRowKeys, selectedRows) => {
      console.log("선택된 ID:", selectedRowKeys);
      console.log("선택된 데이터:", selectedRows);
    },
  };

  return (
    <>
      <Row gutter={[16, 16]}>
        {stats.map((stat) => (
          <Col xs={24}
               sm={12}
               md={12}
               lg={6}
               key={stat.title}>
            <AdminStatCard {...stat} />
          </Col>
        ))}
      </Row>

      {/* 목록 탭 */}
      <div className="admin-list-tabs">
        <Button
          type="button"
          className={`admin-list-button ${
            listType === "admin" ? "active" : ""
          }`}
          onClick={() => setListType("admin")}
        >
          관리자목록
        </Button>

        <Button
          type="button"
          className={`admin-list-button ${
            listType === "user" ? "active" : ""
          }`}
          onClick={() => setListType("user")}
        >
          사용자목록
        </Button>
      </div>

      {/* 검색 영역 */}
      <div className="admin-search-box">
        <Row gutter={[16,16]} style={{ width: "100%" }}>
        <Col flex="120px">
          <Select
            className="admin-search-condition"
            defaultValue="lucy"
            style={{ width: "100%" }}
            options={[{ value: 'lucy', label: '11' },{ value: '22', label: '22' }]}
          />
        </Col>

          <Col flex="none">
            <Input
              type="text"
              size="medium"
              className="admin-search-input"
              placeholder="검색어를 입력하세요"
            />
          </Col>

          <Col>
            <Button
              className="admin-search-button"
              type="button"
            >
              검색
            </Button>
          </Col>
        </Row>
      </div>

      {/* 모임 목록 */}
      <div className="admin-table-box">
        <Table
          rowSelection={rowSelection}
          columns={listType === "admin" ? adminColumns : userColumns}
          dataSource={listType === "admin" ? adminData : userData}
          pagination={{
            pageSize: 10,
            showSizeChanger: false,
          }}
          rowKey="id"
          scroll={{ x: 800 }}
        />
      </div>
    </>
  );
}