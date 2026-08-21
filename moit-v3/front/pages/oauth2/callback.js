import { useEffect } from "react";
import { useRouter } from "next/router";
import { useDispatch } from "react-redux";

import { loginSuccess } from "../../reducers/userReducer";

function OAuth2Callback() {

    const router = useRouter();
    const dispatch = useDispatch();

    useEffect(() => {

        if (!router.isReady) {
            return;
        }

        const {
            accessToken,
            refreshToken
        } = router.query;

        if (!accessToken || !refreshToken) {
            return;
        }

        // JWT 저장
        localStorage.setItem(
            "accessToken",
            accessToken
        );

        localStorage.setItem(
            "refreshToken",
            refreshToken
        );

        // Redux 로그인 상태
        dispatch(
            loginSuccess({
                accessToken,
                refreshToken
            })
        );

        router.replace("/");

    }, [router.isReady, router.query, dispatch, router]);

    return (
        <div
            style={{
                textAlign: "center",
                marginTop: "100px"
            }}
        >
            소셜 로그인 처리 중입니다...
        </div>
    );
}

export default OAuth2Callback;