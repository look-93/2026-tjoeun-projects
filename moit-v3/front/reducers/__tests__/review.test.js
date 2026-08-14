import reviewReducer, {
    resetReviewState,
    getReviewListSuccess,
    getReviewDetailSuccess,
    deleteReviewSuccess,
    toggleReviewLikeSuccess,
    changeVisibilitySuccess,
    analyzeReviewsSuccess,
} from '../reviewReducer';

describe('reviewReducer slice',()=>{
    const initialState={
        reviews:[],
        reviewDetail:null,
        totalCount:0,
        totalPage:0,
        analysisResult:"",
        loading:false,
        error:null,
        success:false
    };


    //상태 초기화 테스트
    it('resetReviewState 실행 시 로딩,성공 상태가 초기화',()=>{
        const prevState={...initialState,loading: true,error:'에러발생',success:true};
        const state=reviewReducer(prevState,resetReviewState());

        expect(state.loading).toBe(false);
        expect(state.error).toBeNull();
        expect(state.success).toBe(false);
    });

    it('getReveiwListSuccess 실행 시 리뷰 목록과 페이지 정보가 업데이트',()=>{
        const mockPayload={
            reviews:[{id:1,title:'리뷰 1'},{ id: 2, title: '리뷰 2' }],
            totalCount: 2,
            totalPage: 1,
        };

        const state=reviewReducer(initialState,getReviewListSuccess(mockPayload))

        expect(state.loading).toBe(false);
        expect(state.reviews).toHaveLength(2);
        expect(state.reviews).toEqual(mockPayload.reviews);
        expect(state.totalCount).toBe(2);
        expect(state.totalPage).toBe(1);
    });
    //리뷰 삭제
    it('deleteReviewSuccess 실행 시 목록에서  해당 리뷰가  삭제되고 totalCount가 1 감소',()=>{

        const prevState={
            ...initialState,
            reviews:[
                { id: 1, title: '삭제할 리뷰' },
                { id: 2, title: '남아있는 리뷰' },
            ],
            totalCount:2,

        };
        const state=reviewReducer(prevState,deleteReviewSuccess(1));

        expect(state.success).toBe(true);
        expect(state.reviews).toHaveLength(1);
        expect(state.reviews.find(r=>r.id===1)).toBeUndefined();
        expect(state.totalCount).toBe(1);
    });

})