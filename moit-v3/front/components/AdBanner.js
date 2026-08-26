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

    const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080";

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

                if (!response?.data) {
                    return;
                }

                setAd(response.data);

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

        // 광고 자체가 없는 경우
        if (!ad) {
            return null;
        }

    return (
        <Row>
            <Col span={24}>

                <Card
                    hoverable
                    className="main-ad-card"
                    onClick={handleClick}
                    styles={{
                        body: { padding: 0 }
                    }}
                >
                    {/* 광고 이미지 */}
                    {mainImage?.imageUrl ? (

                        <img
                            src={`${API_BASE_URL}${mainImage.imageUrl}`}
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

                    {/* PREMIUM 광고 */}
                    {ad.adGrade === "PREMIUM" && (

                        <div
                            className="premium-ad-overlay"
                            onClick={handleClick}
                        >

                            <div className="premium-ad-title">
                                {ad.title}
                            </div>

                            <div className="premium-ad-content">

                                {ad.content}

                            </div>

                        </div>

                    )}

                </Card>

            </Col>
        </Row>
    );
}

export default AdBanner;