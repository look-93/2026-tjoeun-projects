import React, { useEffect, useRef, useState } from "react";
import { Row, Col, Card, Typography } from "antd";

import {
    getTopAdvertisement,
    increaseAdvertisementImpression,
    increaseAdvertisementClick,
} from "../api/advertiseApi";

const { Text } = Typography;

function AdBanner() {

    const [ad, setAd] = useState(null);

    // 한 번의 화면 렌더링에서 노출 API 중복 호출 방지
    const impressionSent = useRef(false);

    // =========================================================
    // 광고 조회
    // =========================================================

    useEffect(() => {

        const fetchAdvertisement = async () => {

            try {

                const response =
                    await getTopAdvertisement("MAIN");

                if (!response) {
                    return;
                }

                setAd(response);

            } catch (error) {

                console.error(
                    "메인 광고 조회 실패:",
                    error
                );

            }

        };

        fetchAdvertisement();

    }, []);


    // =========================================================
    // 광고 노출
    // =========================================================

    useEffect(() => {

        if (!ad?.adId) {
            return;
        }

        if (impressionSent.current) {
            return;
        }

        const sendImpression = async () => {

            try {

                await increaseAdvertisementImpression(
                    ad.adId,
                    "MAIN"
                );

                impressionSent.current = true;

                console.log(
                    "광고 노출 처리:",
                    ad.adId
                );

            } catch (error) {

                console.error(
                    "광고 노출 처리 실패:",
                    error
                );

            }

        };

        sendImpression();

    }, [ad]);


    // =========================================================
    // 광고 클릭
    // =========================================================

    const handleClick = async () => {

        if (!ad?.adId) {
            return;
        }

        try {

            await increaseAdvertisementClick(
                ad.adId,
                "MAIN"
            );

            console.log(
                "광고 클릭 처리:",
                ad.adId
            );

        } catch (error) {

            console.error(
                "광고 클릭 처리 실패:",
                error
            );

        } finally {

            if (ad.landingUrl) {
                window.location.href = ad.landingUrl;
            }

        }

    };


    // =========================================================
    // MAIN 이미지
    // =========================================================

    const mainImage =
        ad?.imageList?.find(
            (image) =>
                image.imageType === "MAIN"
        );


    return (
        <Row>
            <Col span={24}>

                <Card
                    hoverable
                    className="main-ad-card"
                    onClick={handleClick}
                >

                    {mainImage?.imageUrl ? (

                        <img
                            src={mainImage.imageUrl}
                            alt={ad.title || "광고"}
                            className="ad-image"
                        />

                    ) : (

                        <div className="ad-placeholder">
                            <Text type="secondary">
                                현재 진행 중인 광고가 없습니다.
                            </Text>
                        </div>

                    )}

                </Card>

            </Col>
        </Row>
    );
}

export default AdBanner;