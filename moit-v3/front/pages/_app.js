//전체앱의 공통 설정(Redux Provider, 글로벌 스타일 등)
import React, { useEffect } from "react"; // React 불러오기
import Head from "next/head";
import { wrapper } from "../store/configureStore"; // Redux Store를 연결해주는 객체 ,치킨집(전역상태 + 서버연동)
import UserLayout from "../components/layout/UserLayout"; // 공통레이아웃
import AdminLayout from "../components/layout/AdminLayout"; // 공통레이아웃
import "antd/dist/antd.css"; // ant 디자인
import "../styles/global.css"; // 전역 css
import "bootstrap/dist/css/bootstrap.min.css"; // bootstrap css
import { useDispatch, useSelector } from "react-redux";
import { getMyInfoRequest } from "../reducers/userReducer";

//부품
function MyApp({ Component, pageProps, router }) {
    const dispatch = useDispatch();

    // 새로고침 시 유저 정보 초기화 방지
    const isAdminPage = router.pathname.startsWith("/admin");

    // ## 부품, 초기설정값
    return (
        <>
            <Head>
                <script
                    type="text/javascript"
                    src={`https://oapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=${process.env.NEXT_PUBLIC_NAVER_MAP_CLIENT_ID}`}
                />
            </Head>

            {isAdminPage ? (
                <AdminLayout>
                    <Component {...pageProps} />
                </AdminLayout>
            ) : (
                <UserLayout>
                    <Component {...pageProps} />
                </UserLayout>
            )}
        </>
    );
}
export default wrapper.withRedux(MyApp); //스토어 전역사용
