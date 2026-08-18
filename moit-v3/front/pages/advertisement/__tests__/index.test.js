import React from 'react';
import {
    render,
    screen
} from '@testing-library/react';

import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';

import AdvertisementPage from '../index';
import advertiseReducer from '../../../reducers/advertiseReducer';

// Ant Design의 반응형 컴포넌트(Row, Col 등)가 사용하는 API
beforeAll(() => {
    window.matchMedia = window.matchMedia || function () {
        return {
            matches: false,
            addListener: jest.fn(),
            removeListener: jest.fn(),
            addEventListener: jest.fn(),
            removeEventListener: jest.fn(),
            dispatchEvent: jest.fn(),
        };
    };
});

describe('AdvertisementPage', () => {

    test('내 광고 페이지가 화면에 표시된다.', () => {

        const store = configureStore({
            reducer: {
                advertise: advertiseReducer,
            },
        });

        render(
            <Provider store={store}>
                <AdvertisementPage />
            </Provider>
        );

        expect(
            screen.getByText('내 광고')
        ).toBeInTheDocument();
    });
});