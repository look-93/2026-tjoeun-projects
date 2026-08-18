import { useEffect, useState } from "react";

import {
    getAdvertiseAdminList,
    getAdvertiseAdminCount,
    approveAdvertise,
    rejectAdvertise,
    updateAdvertiseStatus
} from "../../api/advertiseAdminApi";

export default function AdvertiseAdminList() {

    const [tab, setTab] = useState("approval");

    const [advertiseList, setAdvertiseList] = useState([]);

    const [searchText, setSearchText] = useState("");

    const [status, setStatus] = useState("");

    const [sort, setSort] = useState("");

    const [page, setPage] = useState(1);

    const [totalCount, setTotalCount] = useState(0);

    const size = 10;


    // 광고 목록 조회
    const loadAdvertiseList = async () => {

        try {

            const response =
                await getAdvertiseAdminList({
                    tab,
                    searchText,
                    status,
                    sort,
                    page,
                    size
                });

            setAdvertiseList(response.data);

        } catch (error) {

            console.error(
                "광고 목록 조회 실패",
                error
            );
        }
    };


    // 광고 개수 조회
    const loadCount = async () => {

        try {

            const response =
                await getAdvertiseAdminCount({
                    tab,
                    searchText,
                    status
                });

            setTotalCount(response.data);

        } catch (error) {

            console.error(
                "광고 개수 조회 실패",
                error
            );
        }
    };


    useEffect(() => {

        loadAdvertiseList();
        loadCount();

    }, [tab, page]);


    // 검색
    const handleSearch = (e) => {

        e.preventDefault();

        setPage(1);

        loadAdvertiseList();
        loadCount();
    };


    // 탭 변경
    const handleTabChange = (nextTab) => {

        setTab(nextTab);

        setPage(1);

        setSearchText("");
        setStatus("");
        setSort("");
    };


    // 승인
    const handleApprove = async (adId) => {

        if (!window.confirm("이 광고를 승인하시겠습니까?")) {
            return;
        }

        try {

            await approveAdvertise(adId);

            alert("광고가 승인되었습니다.");

            loadAdvertiseList();
            loadCount();

        } catch (error) {

            console.error(
                "광고 승인 실패",
                error
            );

            alert(
                error.response?.data?.message
                || "광고 승인에 실패했습니다."
            );
        }
    };


    // 반려
    const handleReject = async (adId) => {

        const rejectReason =
            window.prompt("반려 사유를 입력해주세요.");

        if (!rejectReason) {
            return;
        }

        try {

            await rejectAdvertise(
                adId,
                rejectReason
            );

            alert("광고가 반려되었습니다.");

            loadAdvertiseList();
            loadCount();

        } catch (error) {

            console.error(
                "광고 반려 실패",
                error
            );

            alert(
                error.response?.data?.message
                || "광고 반려에 실패했습니다."
            );
        }
    };


    // 광고 상태 변경
    const handleStatusChange = async (
        adId,
        nextStatus
    ) => {

        try {

            await updateAdvertiseStatus(
                adId,
                nextStatus
            );

            alert("광고 상태가 변경되었습니다.");

            loadAdvertiseList();

        } catch (error) {

            console.error(
                "광고 상태 변경 실패",
                error
            );

            alert(
                error.response?.data?.message
                || "광고 상태 변경에 실패했습니다."
            );
        }
    };


    const totalPage =
        Math.ceil(totalCount / size);


    return (
        <div>

            <h1>광고 관리</h1>


            {/* 탭 */}
            <div>

                <button
                    type="button"
                    onClick={() =>
                        handleTabChange("approval")
                    }
                >
                    승인 관리
                </button>


                <button
                    type="button"
                    onClick={() =>
                        handleTabChange("payment")
                    }
                >
                    결제 확인
                </button>


                <button
                    type="button"
                    onClick={() =>
                        handleTabChange("status")
                    }
                >
                    운영 관리
                </button>


                <button
                    type="button"
                    onClick={() =>
                        window.location.href =
                            "/advertisement/admin/dashboard"
                    }
                >
                    📊 통계 대시보드
                </button>

            </div>


            {/* 검색 */}
            <form onSubmit={handleSearch}>

                <input
                    type="text"
                    placeholder="광고명 검색"
                    value={searchText}
                    onChange={(e) =>
                        setSearchText(e.target.value)
                    }
                />


                {tab === "status" && (

                    <select
                        value={status}
                        onChange={(e) =>
                            setStatus(e.target.value)
                        }
                    >

                        <option value="">
                            전체 상태
                        </option>

                        <option value="OPEN">
                            OPEN
                        </option>

                        <option value="PENDING">
                            PENDING
                        </option>

                        <option value="CLOSED">
                            CLOSED
                        </option>

                    </select>

                )}


                <select
                    value={sort}
                    onChange={(e) => {

                        setSort(e.target.value);
                        setPage(1);

                    }}
                >

                    <option value="">
                        최신 등록순
                    </option>

                    <option value="start">
                        시작 예정순
                    </option>

                    <option value="end">
                        종료 임박순
                    </option>

                    <option value="budget">
                        예산 높은순
                    </option>

                    <option value="grade">
                        등급순
                    </option>

                </select>


                <button type="submit">
                    검색
                </button>

            </form>


            {/* 승인 관리 */}
            {tab === "approval" && (

                <div>

                    <h2>광고 승인 관리</h2>


                    <table>

                        <thead>

                            <tr>
                                <th>번호</th>
                                <th>이미지</th>
                                <th>광고명</th>
                                <th>유형</th>
                                <th>상태</th>
                                <th>기간</th>
                                <th>관리</th>
                            </tr>

                        </thead>


                        <tbody>

                            {advertiseList.length === 0 ? (

                                <tr>
                                    <td colSpan="7">
                                        승인 대기 광고가 없습니다.
                                    </td>
                                </tr>

                            ) : (

                                advertiseList.map(
                                    (ad, index) => (

                                        <tr key={ad.adId}>

                                            <td>
                                                {(page - 1) * size +
                                                    index + 1}
                                            </td>


                                            <td>

                                                {ad.imageList?.length > 0 && (

                                                    <img
                                                        src={
                                                            ad.imageList[0]
                                                                .imageUrl
                                                        }
                                                        alt={ad.title}
                                                        style={{
                                                            width: "70px",
                                                            height: "45px",
                                                            objectFit: "cover"
                                                        }}
                                                    />

                                                )}

                                            </td>


                                            <td>
                                                {ad.title}
                                            </td>


                                            <td>
                                                {ad.adChannel}
                                            </td>


                                            <td>
                                                {ad.approvalStatus}
                                            </td>


                                            <td>
                                                {formatDate(
                                                    ad.startDatetime
                                                )}

                                                {" ~ "}

                                                {formatDate(
                                                    ad.endDatetime
                                                )}
                                            </td>


                                            <td>

                                                <button
                                                    type="button"
                                                    onClick={() =>
                                                        handleDetail(
                                                            ad.adId
                                                        )
                                                    }
                                                >
                                                    상세
                                                </button>


                                                <button
                                                    type="button"
                                                    onClick={() =>
                                                        handleApprove(
                                                            ad.adId
                                                        )
                                                    }
                                                >
                                                    승인
                                                </button>


                                                <button
                                                    type="button"
                                                    onClick={() =>
                                                        handleReject(
                                                            ad.adId
                                                        )
                                                    }
                                                >
                                                    반려
                                                </button>

                                            </td>

                                        </tr>

                                    )
                                )

                            )}

                        </tbody>

                    </table>

                </div>

            )}


            {/* 결제 확인 */}
            {tab === "payment" && (

                <div>

                    <h2>광고 결제 확인</h2>


                    <table>

                        <thead>

                            <tr>
                                <th>번호</th>
                                <th>광고명</th>
                                <th>광고주</th>
                                <th>결제 유형</th>
                                <th>광고 등급</th>
                                <th>결제 금액</th>
                                <th>결제 상태</th>
                                <th>관리</th>
                            </tr>

                        </thead>


                        <tbody>

                            <tr>

                                <td
                                    colSpan="8"
                                    style={{
                                        textAlign: "center"
                                    }}
                                >
                                    결제 확인 API 연결 예정
                                </td>

                            </tr>

                        </tbody>

                    </table>

                </div>

            )}


            {/* 운영 관리 */}
            {tab === "status" && (

                <div>

                    <h2>광고 게시 관리</h2>


                    <table>

                        <thead>

                            <tr>
                                <th>번호</th>
                                <th>이미지</th>
                                <th>광고명</th>
                                <th>유형</th>
                                <th>상태</th>
                                <th>기간</th>
                                <th>노출수</th>
                                <th>클릭수</th>
                                <th>관리</th>
                            </tr>

                        </thead>


                        <tbody>

                            {advertiseList.length === 0 ? (

                                <tr>
                                    <td colSpan="9">
                                        광고가 없습니다.
                                    </td>
                                </tr>

                            ) : (

                                advertiseList.map(
                                    (ad, index) => (

                                        <tr key={ad.adId}>

                                            <td>
                                                {(page - 1) * size +
                                                    index + 1}
                                            </td>


                                            <td>

                                                {ad.imageList?.length > 0 && (

                                                    <img
                                                        src={
                                                            ad.imageList[0]
                                                                .imageUrl
                                                        }
                                                        alt={ad.title}
                                                        style={{
                                                            width: "70px",
                                                            height: "45px",
                                                            objectFit: "cover"
                                                        }}
                                                    />

                                                )}

                                            </td>


                                            <td>
                                                {ad.title}
                                            </td>


                                            <td>
                                                {ad.adChannel}
                                            </td>


                                            <td>

                                                <select
                                                    value={ad.status}
                                                    onChange={(e) =>
                                                        handleStatusChange(
                                                            ad.adId,
                                                            e.target.value
                                                        )
                                                    }
                                                >

                                                    <option value="OPEN">
                                                        OPEN
                                                    </option>

                                                    <option value="PENDING">
                                                        PENDING
                                                    </option>

                                                    <option value="CLOSED">
                                                        CLOSED
                                                    </option>

                                                </select>

                                            </td>


                                            <td>
                                                {formatDate(
                                                    ad.startDatetime
                                                )}

                                                {" ~ "}

                                                {formatDate(
                                                    ad.endDatetime
                                                )}
                                            </td>


                                            <td>
                                                {ad.impressions ?? 0}
                                            </td>


                                            <td>
                                                {ad.clicks ?? 0}
                                            </td>


                                            <td>

                                                <button
                                                    type="button"
                                                    onClick={() =>
                                                        handleDetail(
                                                            ad.adId
                                                        )
                                                    }
                                                >
                                                    상세
                                                </button>

                                            </td>

                                        </tr>

                                    )
                                )

                            )}

                        </tbody>

                    </table>

                </div>

            )}


            {/* 페이징 */}
            {totalPage > 0 && (

                <div>

                    <button
                        type="button"
                        disabled={page <= 1}
                        onClick={() =>
                            setPage(page - 1)
                        }
                    >
                        이전
                    </button>


                    <span>
                        {page} / {totalPage}
                    </span>


                    <button
                        type="button"
                        disabled={page >= totalPage}
                        onClick={() =>
                            setPage(page + 1)
                        }
                    >
                        다음
                    </button>

                </div>

            )}

        </div>
    );
}


// 상세 페이지 이동
function handleDetail(adId) {

    window.location.href =
        `/advertisement/admin/${adId}`;
}


// 날짜 표시
function formatDate(value) {

    if (!value) {
        return "-";
    }

    return value.substring(0, 10);
}