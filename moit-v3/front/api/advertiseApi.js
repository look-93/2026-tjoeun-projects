import axios from 'axios';

// 광고 API 기본 주소
const API_URL = 'http://localhost:8080/api/advertisement';


// 내 광고 목록 조회
export const getAdvertiseList = (params) => {
    return axios.get(API_URL, {
        params,
    });
};


// 광고 상세 조회
export const getAdvertiseDetail = (adId) => {
    return axios.get(`${API_URL}/${adId}`);
};


// 광고 등록
export const createAdvertise = (formData) => {
    return axios.post(API_URL, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};


// 광고 수정
export const updateAdvertise = (adId, formData) => {
    return axios.put(`${API_URL}/${adId}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });
};


// 광고 삭제
export const deleteAdvertise = (adId) => {
    return axios.delete(`${API_URL}/${adId}`);
};