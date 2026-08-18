import axios from "axios";

// 광고
const API_URL = "http://localhost:8080/api/admin/advertisement";
// 가격
const PRICE_API_URL = `${API_URL}/price`;

// 관리자 광고 가격 목록
export const getAdvertiseAdminPriceList = () => {
    return axios.get(`${API_URL}/price`);
};

// 관리자 광고 가격 상세
export const getAdvertiseAdminPrice = (priceId) => {
    return axios.get(`${PRICE_API_URL}/price/${priceId}`);
};

// 관리자 광고 가격 등록
export const createAdvertiseAdminPrice = (data) => {
    return axios.post(`${PRICE_API_URL}/price`, data);
};

// 관리자 광고 가격 수정
export const updateAdvertiseAdminPrice = (priceId, data) => {
    return axios.put(`${PRICE_API_URL}/price/${priceId}`, data);
};

// 관리자 광고 가격 삭제
export const deleteAdvertiseAdminPrice = (priceId) => {
    return axios.delete(`${PRICE_API_URL}/price/${priceId}`);
};

////////////////////////////////////////////

// 광고 목록
export const getAdvertiseAdminList = (params) => {

    return axios.get(API_URL, {
        params
    });
};


// 광고 목록 개수
export const getAdvertiseAdminCount = (params) => {

    return axios.get(`${API_URL}/count`, {
        params
    });
};


// 광고 상세
export const getAdvertiseAdminDetail = (adId) => {

    return axios.get(`${API_URL}/${adId}`);
};


// 광고 승인
export const approveAdvertise = (adId) => {

    return axios.patch(
        `${API_URL}/${adId}/approve`
    );
};


// 광고 반려
export const rejectAdvertise = (
    adId,
    rejectReason
) => {

    return axios.patch(
        `${API_URL}/${adId}/reject`,
        null,
        {
            params: {
                rejectReason
            }
        }
    );
};


// 광고 상태 변경
export const updateAdvertiseStatus = (
    adId,
    status
) => {

    return axios.patch(
        `${API_URL}/${adId}/status`,
        null,
        {
            params: {
                status
            }
        }
    );
};