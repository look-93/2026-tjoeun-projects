import { createSlice } from "@reduxjs/toolkit";

//1. 초기화 상태(공용)
const initialState = {
    user: null,     //단건 조회된 사용자 정보
    loading:false,  //로딩상태
    error: null,    //에러메시지
    success:false   //성공여부
};
//2. 상태변화
const authReducer = createSlice({
    name: "user",
    initialState,
    reducers: {}
});
//3. action
export const {} = authReducer.actions;
//4. export
export default authReducer.reducer;

