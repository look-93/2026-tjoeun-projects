import React from "react";
import CategoryList from "../components/CategoryList";
import AdBanner from "../components/AdBanner";
import PopularMeetupList from "../components/PopularMeetupList";
import Hero from "../components/Hero";

export default function Home() {
    const categories = [
        { icon: "🏃", name: "운동" },
        { icon: "📚", name: "여행" },
        { icon: "🎮", name: "게임" },
        { icon: "🎨", name: "독서" },
        { icon: "☕", name: "맛집" },
        { icon: "✈️", name: "영화" },
        { icon: "☕", name: "음악" },
        { icon: "🎵", name: "요리" },
    ];

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
            <CategoryList categories={categories} />

            {/* 광고 */}
            <AdBanner />

            {/* 인기 모임 */}
            <PopularMeetupList popularMeetups={popularMeetups} />
        </div>
    );
}
