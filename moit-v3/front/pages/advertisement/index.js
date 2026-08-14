import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import Header from '../../components/layout/UserHeader';
import Footer from '../../components/layout/UserFooter';
import AdvertisePageHeader from '../../components/layout/AdvertisePageHeader';

import {
    getAdvertiseListRequest
} from '../../reducers/advertiseReducer';


const AdvertisementPage = () => {

    const dispatch = useDispatch();

    // 광고 목록 상태
    const {
        list,
        loading,
        error
    } = useSelector((state) => state.advertise);


    // 페이지가 처음 열릴 때 광고 목록 조회
    useEffect(() => {

        dispatch(
            getAdvertiseListRequest({
                page: 1,
                size: 10
            })
        );

    }, [dispatch]);


    return (
        <>
            {/* 공용 헤더 */}
            <Header />

            {/* 광고 페이지 영역 */}
            <main>

                {/* 광고 페이지 전용 헤더 */}
                <AdvertisePageHeader />

                <section>

                    <h2>내 광고</h2>

                    {/* 조회 중 */}
                    {loading && (
                        <p>광고 목록을 불러오는 중입니다.</p>
                    )}

                    {/* 에러 */}
                    {error && (
                        <p>광고 목록을 불러오지 못했습니다.</p>
                    )}

                    {/* 광고 목록 */}
                    {!loading && !error && (
                        <div>

                            {list && list.length > 0 ? (

                                list.map((advertisement) => (

                                    <div key={advertisement.adId}>

                                        <h3>
                                            {advertisement.title}
                                        </h3>

                                        <p>
                                            상태 : {advertisement.status}
                                        </p>

                                    </div>

                                ))

                            ) : (

                                <p>
                                    등록된 광고가 없습니다.
                                </p>

                            )}

                        </div>
                    )}

                </section>

            </main>

            {/* 공용 푸터 */}
            <Footer />
        </>
    );
};

export default AdvertisementPage;