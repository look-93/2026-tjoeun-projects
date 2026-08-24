import React, { useState, useEffect } from "react";
import CategoryList from "../components/CategoryList";
import AdBanner from "../components/AdBanner";
import PopularMeetupList from "../components/PopularMeetupList";
import Hero from "../components/Hero";
import { useRouter } from "next/router";
import { useSelector, useDispatch } from "react-redux";
import {
    fetchCategoriesRequest,
    fetchPopularMeetupsRequest,
} from "../reducers/meetupReducer";

export default function Home() {
    const dispatch = useDispatch();
    const { categories } = useSelector((state) => state.meetup);
    const router = useRouter();

    useEffect(() => {
        dispatch(fetchCategoriesRequest());
    }, [dispatch]);

    const handleCategoryClick = (category) => {
        router.push(`/user/meetup?categoryId=${category.id}`);
    };

    const popularMeetups = [
        {
            id: 1,
            title: "러닝 크루 모집",
            participants: "8 / 10",
            location: "서울",
        },
        {
            id: 2,
            title: "독서 스터디",
            participants: "6 / 8",
            location: "인천",
        },
        {
            id: 3,
            title: "보드게임 모임",
            participants: "7 / 10",
            location: "경기",
        },
        {
            id: 4,
            title: "영화 같이 볼 사람",
            participants: "5 / 10",
            location: "서울",
        },
    ];

    return (
        <div className="main-page">
            {/* Hero */}
            <Hero />

            {/* Category */}
            <CategoryList
                categories={categories.filter((cate) => cate.parentId === null)}
                onCategoryClick={handleCategoryClick}
            />

            {/* 광고 */}
            <AdBanner />

            {/* 인기 모임 */}
            <PopularMeetupList popularMeetups={popularMeetups} />
        </div>
    );
}
