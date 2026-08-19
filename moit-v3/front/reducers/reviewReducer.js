import {createSlice} from "@reduxjs/toolkit";


//초기화 상태
const initialState={
    //백엔드 응답 데이터
    reviews:[],
    reviewDetail:null,
    totalCount:0,
    totalPage: 0,
    analysisResult:"",


    loading:false,
    error:null,
    success:false,
};

//상태변화
const reviewReducer=createSlice({
    name:"review",
    initialState,
    reducers:{
        //--상태 초기화--
        resetReviewState:(state)=>{
            state.loading=false;
            state.error=null;
            state.success=false;
        },

        //--리뷰 작성--
        createReviewRequest:(state)=>{
            state.loading=true;
            state.error=null;
            state.success=false;
        },
        createReviewSuccess:(state)=>{
            state.loading=false;
            state.success=true;
        },
        createReviewFailure:(state,action)=>{
            state.loading=false;
            state.error=action.payload;
            state.success=false;
        },

        //--리뷰 상세 조회--
        getReviewDetailRequest:(state)=>{
            state.loading=true;
            state.error=null;
            state.reviewDetail=null;
        },
        getReviewDetailSuccess:(state,action)=>{
            state.loading=false;
            state.reviewDetail=action.payload;
        },
        getReviewDetailFailure:(state,action)=>{
            state.loading=false;
            state.error=action.payload;
        },

        //--리뷰 수정--
        updateReviewRequest:(state)=>{
            state.loading=true;
            state.error=null;
            state.success=false;
        },
        updateReviewSuccess:(state)=>{
            state.loading=false;
            state.success=true;
        },
        updateReviewFailure:(state,action)=>{
            state.loading=false;
            state.error=action.payload;
            state.success=false;
        },
        //리뷰 삭제(사용자, 관리자)
        deleteReviewRequest:(state)=>{
            state.loading=true;
            state.error=null;
            state.success=false;
        },
        deleteReviewSuccess:(state,action)=>{
            state.loading=false;
            state.success=true;
            const deletedId=action.payload;

            // 서버 재요청 없이 현재 목록에서 바로 제거하여 화면 갱신
            state.reviews = state.reviews.filter(review => review.id !== deletedId);
            state.totalCount = Math.max(0, state.totalCount - 1);
        },
        deleteReviewFailure:(state,action)=>{
            state.loading=false;
            state.error=action.payload;
            state.success=false;
        },
        //--리뷰 목록 조회--
        getReviewListRequest:(state)=>{
            state.loading=true;
            state.error=null;
        },
        getReviewListSuccess:(state,action)=>{
            state.loading=false;

            state.reviews = action.payload.reviews || [];
            state.totalCount = action.payload.totalCount || 0;
            state.totalPage = action.payload.totalPage || 0;
        },
        getReviewListFailure:(state,action)=>{
            state.loading=false;
            state.error=action.payload;
        },

        // --리뷰 좋아요 토글--
        toggleReviewLikeRequest: (state) => {
            // 좋아요는 화면 깜빡임 방지를 위해 loading 상태를 건드리지 않음
            state.error = null;
        },
        toggleReviewLikeSuccess: (state, action) => {
            const targetId = action.payload; // reviewId

            // 1) 목록(reviews) 데이터 업데이트
            const review = state.reviews.find(r => r.id === targetId);
            if (review) {
                if (review.isLiked === undefined) review.isLiked = false;
                
                review.likesCount = review.isLiked 
                    ? Math.max(0, review.likesCount - 1) 
                    : review.likesCount + 1;
                review.isLiked = !review.isLiked;
            }

            // 2) 상세화면(reviewDetail) 데이터 업데이트
            if (state.reviewDetail && state.reviewDetail.id === targetId) {
                if (state.reviewDetail.isLiked === undefined) state.reviewDetail.isLiked = false;
                
                state.reviewDetail.likesCount = state.reviewDetail.isLiked 
                    ? Math.max(0, state.reviewDetail.likesCount - 1) 
                    : state.reviewDetail.likesCount + 1;
                state.reviewDetail.isLiked = !state.reviewDetail.isLiked;
            }
        },
        toggleReviewLikeFailure: (state, action) => {
            state.error = action.payload;
        },
        //--ai 리뷰 분석--
        analyzeReviewsRequest:(state)=>{
            state.loading=true;
            state.error=null;
            state.analysisResult = "";
        },
        analyzeReviewsSuccess:(state,action)=>{
            state.loading=false;
            state.analysisResult=action.payload;
        },
        analyzeReviewsFailure:(state,action)=>{
            state.loading=false;
            state.error=action.payload;
        },
        //--관리자 리뷰 공개 여부 변경--
        changeVisibilitySuccess:(state,action)=>{
            const targetId=action.payload;

            const review = state.reviews.find(r => r.id === targetId);
            if (review) {
                // "Y" <-> "N" 토글
                review.isPublic = review.isPublic === "Y" ? "N" : "Y";
            }

            if (state.reviewDetail && state.reviewDetail.id === targetId) {
                state.reviewDetail.isPublic = state.reviewDetail.isPublic === "Y" ? "N" : "Y";
            }
        },

    },
});

// 3. Action 내보내기
export const {
    resetReviewState,
    
    createReviewRequest,
    createReviewSuccess,
    createReviewFailure,

    getReviewDetailRequest,
    getReviewDetailSuccess,
    getReviewDetailFailure,

    updateReviewRequest,
    updateReviewSuccess,
    updateReviewFailure,

    deleteReviewRequest,
    deleteReviewSuccess,
    deleteReviewFailure,

    getReviewListRequest,
    getReviewListSuccess,
    getReviewListFailure,

    toggleReviewLikeRequest,
    toggleReviewLikeSuccess,
    toggleReviewLikeFailure,

    analyzeReviewsRequest,
    analyzeReviewsSuccess,
    analyzeReviewsFailure,

    changeVisibilitySuccess,
} = reviewReducer.actions;

// 4. Export Default
export default reviewReducer.reducer;