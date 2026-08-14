import axios from 'axios';

import {
    getAdvertiseList,
    getAdvertiseDetail,
    createAdvertise,
    updateAdvertise,
    deleteAdvertise
} from '../advertiseApi';


// axios mock
jest.mock('axios');


describe('advertiseApi', () => {

    // 광고 목록 조회
    test('광고 목록을 조회한다.', async () => {

        const params = {
            page: 1,
            size: 10
        };

        axios.get.mockResolvedValue({
            data: {
                list: [],
                totalCnt: 0,
                page: 1,
                size: 10
            }
        });

        await getAdvertiseList(params);

        expect(axios.get).toHaveBeenCalledWith(
            '/api/advertisement',
            {
                params
            }
        );
    });


    // 광고 상세 조회
    test('광고 상세 정보를 조회한다.', async () => {

        const adId = 1;

        axios.get.mockResolvedValue({
            data: {
                adId: 1
            }
        });

        await getAdvertiseDetail(adId);

        expect(axios.get).toHaveBeenCalledWith(
            '/api/advertisement/1'
        );
    });


    // 광고 등록
    test('광고를 등록한다.', async () => {

        const formData = new FormData();

        axios.post.mockResolvedValue({
            data: {
                adId: 1
            }
        });

        await createAdvertise(formData);

        expect(axios.post).toHaveBeenCalledWith(
            '/api/advertisement',
            formData
        );
    });


    // 광고 수정
    test('광고를 수정한다.', async () => {

        const adId = 1;
        const formData = new FormData();

        axios.put.mockResolvedValue({
            data: {
                adId: 1
            }
        });

        await updateAdvertise(adId, formData);

        expect(axios.put).toHaveBeenCalledWith(
            '/api/advertisement/1',
            formData
        );
    });


    // 광고 삭제
    test('광고를 삭제한다.', async () => {

        const adId = 1;

        axios.delete.mockResolvedValue({
            status: 204
        });

        await deleteAdvertise(adId);

        expect(axios.delete).toHaveBeenCalledWith(
            '/api/advertisement/1'
        );
    });

});