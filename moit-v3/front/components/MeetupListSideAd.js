import React from "react";
import { Card, Typography } from "antd";

const { Text } = Typography;

function MeetupListSideAd({ ad }) {
    return (
        <Card className="meetup-list-side-ad">
            {ad?.image ? (
                <img
                    src={ad.image}
                    alt={ad.title || "광고"}
                    className="meetup-side-ad-image"
                />
            ) : (
                <div className="ad-placeholder">
                    <Text type="secondary">광고 영역</Text>
                </div>
            )}
        </Card>
    );
}

export default MeetupListSideAd;
