import axios from "axios";

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080",  
  withCredentials: true,  
  headers: {
    "Content-Type": "application/json",   
    Accept: "application/json",          
  },
}); 

// //JWT Access Token 자동 첨부
// api.interceptors.request.use(
//   (config) => {
//     if (typeof window !== "undefined") { 
//       const accessToken = localStorage.getItem("accessToken");  
//       if (accessToken) {
//         config.headers.Authorization = `Bearer ${accessToken}`;  
//       }
//     }
//     return config;  
//   },
//   (error) => Promise.reject(error)  
// ); 

// //JWT Access Token 만료 처리
// api.interceptors.response.use(
//   (res) => res, 
//   async (error) => {
//     const original = error.config; 
//     const status = error.response?.status;   
//     if (status === 401 && !original._retry) {
//       original._retry = true; 
//       try {
//         const { data } = await api.post("/auth/refresh"); 
//         const newAccessToken = data?.accessToken;  

//         if (typeof window !== "undefined" && newAccessToken) {
//           localStorage.setItem("accessToken", newAccessToken);  
//         }

//         original.headers.Authorization = `Bearer ${newAccessToken}`;   
//         return api(original); 
//       } catch (refreshErr) {
//         if (typeof window !== "undefined") {
//           localStorage.removeItem("accessToken");  
//           window.location.href = "/login";          
//         }
//         return Promise.reject(refreshErr);  
//       }
//     }

//     return Promise.reject(error);  
//   }
// );
 
export default api;
