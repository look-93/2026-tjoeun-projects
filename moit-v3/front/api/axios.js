import axios from "axios";

const api = axios.create({
  baseURL:
    process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
    Accept: "application/json",
  },
});

// JWT Access Token 자동 첨부
api.interceptors.request.use(
  (config) => {
    if (typeof window !== "undefined") {
      const accessToken = localStorage.getItem("accessToken");

      if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
      }
    }

    return config;
  },
  (error) => Promise.reject(error)
);


// JWT Access Token 만료 처리
api.interceptors.response.use(
  (response) => response,

  async (error) => {
    const originalRequest = error.config;
    const status = error.response?.status;

    // Access Token 만료가 아닌 경우
    if (status !== 401) {
      return Promise.reject(error);
    }

    // refresh 요청 자체가 실패한 경우
    if (originalRequest?.url?.includes("/api/members/refresh")) {
      return Promise.reject(error);
    }

    // 이미 재시도한 요청인 경우
    if (originalRequest?._retry) {
      return Promise.reject(error);
    }

    originalRequest._retry = true;

    try {
      const refreshToken =
        typeof window !== "undefined"
          ? localStorage.getItem("refreshToken")
          : null;

      if (!refreshToken) {
        throw new Error("Refresh Token이 없습니다.");
      }

      // Refresh Token으로 Access Token 재발급
      const response = await axios.post(
        `${process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080"}/api/members/refresh`,
        {
          refreshToken: refreshToken,
        },
        {
          withCredentials: true,
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
        }
      );

      const newAccessToken = response.data?.accessToken;
      const newRefreshToken = response.data?.refreshToken;

      if (!newAccessToken) {
        throw new Error("새로운 Access Token이 없습니다.");
      }

      // 새 Access Token 저장
      localStorage.setItem("accessToken", newAccessToken);

      // 백엔드에서 Refresh Token도 교체하므로 같이 저장
      if (newRefreshToken) {
        localStorage.setItem("refreshToken", newRefreshToken);
      }

      // 원래 요청에 새 Access Token 적용
      originalRequest.headers.Authorization =
        `Bearer ${newAccessToken}`;

      // 원래 요청 다시 실행
      return api(originalRequest);

    } catch (refreshError) {

      console.error(
        "Access Token 재발급 실패:",
        refreshError
      );

      if (typeof window !== "undefined") {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");

        window.location.href = "/user/member/login";
      }

      return Promise.reject(refreshError);
    }
  }
);

export default api;
