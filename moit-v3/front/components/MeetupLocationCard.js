import React from "react";
import { Card, Typography } from "antd";
import { EnvironmentOutlined } from "@ant-design/icons";

const { Title, Text } = Typography;

function MeetupLocationCard({ selectedAddress }) {
    return (
        <Card className="mypage-user-info" style={{ marginTop: 20 }}>
            <Title level={4}>모임 위치</Title>

            <div className="meetup-map">
                <div className="meetup-map-placeholder">
                    <EnvironmentOutlined style={{ fontSize: 36 }} />

                    <Text type="secondary">
                        {selectedAddress
                            ? selectedAddress.address
                            : "주소를 선택하면 지도가 표시됩니다."}
                    </Text>
                </div>
            </div>
        </Card>
    );
}

export default MeetupLocationCard;
