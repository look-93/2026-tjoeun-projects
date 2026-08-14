import axios from 'axios';

// 광고 목록 조회
export const getAdvertiseList = (params) => {
    return axios.get('/api/advertisement', {
        params
    });
};

// 광고 상세 조회
export const getAdvertiseDetail = (adId) => {
    return axios.get(`/api/advertisement/${adId}`);
};

// 광고 등록
export const createAdvertise = (formData) => {
    return axios.post('/api/advertisement', formData);
};

// 광고 수정
export const updateAdvertise = (adId, formData) => {
    return axios.put(`/api/advertisement/${adId}`, formData);
};

// 광고 삭제
export const deleteAdvertise = (adId) => {
    return axios.delete(`/api/advertisement/${adId}`);
};