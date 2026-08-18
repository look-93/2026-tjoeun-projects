import React, { useState, useEffect } from "react";
import { Row, Col } from "antd";
import { useRouter } from "next/router";
import { useSelector, useDispatch } from "react-redux";
import { fetchMeetupsRequest, fetchCategoriesRequest, fetchSigungusRequest } from "../../../reducers/meetupReducer";

import MeetupListAd from "../../../components/MeetupListAd";
import MeetupSearchFilter from "../../../components/MeetupSearchFilter";
import MeetupCategory from "../../../components/MeetupCategory";
import MeetupList from "../../../components/MeetupList";
import CommonPagination from "../../../components/CommonPagination";

function MeetupListPage() {
    const router = useRouter();
    const dispatch = useDispatch();

    const [searchText, setSearchText] = useState("");

    const [sidoId, setSidoId] = useState(0);

    const [orderType, setOrderType] = useState("createAt");

    const [categoryId, setCategoryId] = useState(0);

    const [currentPage, setCurrentPage] = useState(1);

    const { meetups, categories, sigungus } = useSelector((state) => state.meetup);

    useEffect(() => {
        dispatch(fetchMeetupsRequest());
        dispatch(fetchCategoriesRequest());
        dispatch(fetchSigungusRequest());
    }, [dispatch]);

    const sidoList = [
        ...new Map(
            sigungus.map((sigungu) => [
                sigungu.sido.sidoId,
                sigungu.sido,
            ])
        ).values(),
    ];

    const ad = {
        title: "Moit 특별 이벤트",
        image: "/images/ad-banner.png",
    };

    // 검색
    const handleSearch = () => {
        console.log({
            searchText,
            sidoId,
            orderType,
            categoryId,
        });

        setCurrentPage(1);
    };

    // 카테고리
    const handleCategoryChange = (id) => {
        //console.log(id)
        setCategoryId(id);
        setCurrentPage(1);
    };

    // 상세
    const handleMeetupClick = (meetupId) => {
        router.push(`/user/meetup/detail?meetupId=${meetupId}`);
    };

    // 좋아요
    const handleToggleLike = (meetupId) => {
        console.log("좋아요:", meetupId);
    };

    // 모임등록
    const handleCreateMeetup = () => {
        router.push("/user/meetup/write");
    };

    // 페이지
    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    return (
        <div className="meetup-list-page">
            <Row gutter={[24, 24]}>
                {/* =====================
            메인
        ====================== */}
                <Col xs={24} lg={18}>
                    {/* 광고 */}
                    <MeetupListAd ad={ad} />

                    {/* 검색 */}
                    <MeetupSearchFilter
                        searchText={searchText}
                        setSearchText={setSearchText}
                        sidoId={sidoId}
                        setSidoId={setSidoId}
                        orderType={orderType}
                        setOrderType={setOrderType}
                        sidoList={sidoList}
                        onSearch={handleSearch}
                        onCreate={handleCreateMeetup}
                    />

                    {/* 모임 목록 */}
                    <MeetupList
                        meetups={meetups}
                        onClick={handleMeetupClick}
                        onToggleLike={handleToggleLike}
                    />

                    {/* 페이지 */}
                    <CommonPagination
                        current={currentPage}
                        total={30}
                        pageSize={10}
                        onChange={handlePageChange}
                    />
                </Col>

                {/* =====================
            사이드바
        ====================== */}
                <Col xs={24} lg={6}>
                    <MeetupCategory
                        categories={categories.filter((cate)=>cate.parentId === null)}
                        selectedCategoryId={categoryId}
                        onChange={handleCategoryChange}
                    />
                </Col>
            </Row>
        </div>
    );
}

export default MeetupListPage;
