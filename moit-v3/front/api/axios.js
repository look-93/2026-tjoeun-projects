import axios from "axios";

const api = axios.create({
  baseURL:
    process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080",
  withCredentials: true,
});

// =========================================================
// Device ID 가져오기
// =========================================================
function getDeviceId() {
  if (typeof window === "undefined") {
    return null;
  }

  let deviceId = localStorage.getItem("deviceId");

  // 최초 접속이면 Device ID 생성
  if (!deviceId) {
    deviceId = crypto.randomUUID();
    localStorage.setItem("deviceId", deviceId);
  }

  return deviceId;
}


// =========================================================
// JWT Access Token + Device ID 자동 첨부
// =========================================================
api.interceptors.request.use(
  (config) => {

    if (typeof window !== "undefined") {

      // =========================
      // Access Token
      // =========================
      const accessToken =
        localStorage.getItem("accessToken");

      // 로그인/회원가입 등 인증 불필요 요청에는
      // Access Token을 굳이 붙이지 않음
      const isLoginRequest =
        config.url === "/api/members/login";

      const isSignupRequest =
        config.url === "/api/members/signup";

      if (
        accessToken &&
        !isLoginRequest &&
        !isSignupRequest
      ) {
        config.headers.Authorization =
          `Bearer ${accessToken}`;
      }

      // =========================
      // Device ID
      // =========================
      const deviceId =
        localStorage.getItem("deviceId");

      if (deviceId) {
        config.headers["X-Device-Id"] = deviceId;
      }
    }

    return config;
  },

  (error) => Promise.reject(error)
);


// =========================================================
// JWT Access Token 만료 처리
// =========================================================
api.interceptors.response.use(

  (response) => response,

  async (error) => {

    const originalRequest = error.config;
    const status = error.response?.status;


    // =====================================================
    // Access Token 만료가 아닌 경우
    // =====================================================
    if (status !== 401) {
      return Promise.reject(error);
    }

    // =====================================================
    // 로그인 / 로그아웃 요청은 Refresh 대상이 아님
    // =====================================================
    if (
      originalRequest?.url === "/api/members/login" ||
      originalRequest?.url === "/api/members/logout"
    ) {
      return Promise.reject(error);
    }


    // =====================================================
    // refresh 요청 자체가 실패한 경우
    // =====================================================
    if (
      originalRequest?.url?.includes(
        "/api/members/refresh"
      )
    ) {
      return Promise.reject(error);
    }


    // =====================================================
    // 이미 재시도한 요청
    // =====================================================
    if (originalRequest?._retry) {
      return Promise.reject(error);
    }

    originalRequest._retry = true;


    try {

      // ===================================================
      // Refresh Token
      // ===================================================
      const refreshToken =
        typeof window !== "undefined"
          ? localStorage.getItem("refreshToken")
          : null;


      if (!refreshToken) {
        throw new Error(
          "Refresh Token이 없습니다."
        );
      }


      // ===================================================
      // Device ID
      // ===================================================
      const deviceId = getDeviceId();


      // ===================================================
      // Refresh Token으로 Access Token 재발급
      // ===================================================
      const response = await axios.post(

        `${
          process.env.NEXT_PUBLIC_API_BASE_URL ||
          "http://localhost:8080"
        }/api/members/refresh`,

        {
          refreshToken: refreshToken,
          deviceId: deviceId,
        },

        {
          withCredentials: true,

          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",

            // Refresh 요청에도 Device ID 전달
            ...(deviceId && {
              "X-Device-Id": deviceId,
            }),
          },
        }
      );


      // ===================================================
      // 새 Token
      // ===================================================
      const newAccessToken =
        response.data?.accessToken;

      const newRefreshToken =
        response.data?.refreshToken;


      if (!newAccessToken) {
        throw new Error(
          "새로운 Access Token이 없습니다."
        );
      }


      // ===================================================
      // Access Token 저장
      // ===================================================
      localStorage.setItem(
        "accessToken",
        newAccessToken
      );


      // ===================================================
      // Refresh Token Rotation
      // ===================================================
      if (newRefreshToken) {

        localStorage.setItem(
          "refreshToken",
          newRefreshToken
        );

      }


      // ===================================================
      // 원래 요청에 새 Access Token 적용
      // ===================================================
      originalRequest.headers.Authorization =
        `Bearer ${newAccessToken}`;


      // ===================================================
      // 원래 요청에 Device ID 적용
      // ===================================================
      if (deviceId) {

        originalRequest.headers["X-Device-Id"] =
          deviceId;

      }


      // ===================================================
      // 원래 요청 재실행
      // ===================================================
      return api(originalRequest);


    } catch (refreshError) {

      console.error(
        "Access Token 재발급 실패:",
        refreshError
      );


      if (typeof window !== "undefined") {

        localStorage.removeItem(
          "accessToken"
        );

        localStorage.removeItem(
          "refreshToken"
        );

        window.location.href =
          "/user/member/login";

      }


      return Promise.reject(
        refreshError
      );
    }

  }
);

export default api;