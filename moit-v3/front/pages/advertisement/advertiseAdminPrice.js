import { useEffect, useState } from "react";
import {
    getAdvertiseAdminPriceList,
    createAdvertiseAdminPrice,
    updateAdvertiseAdminPrice,
    deleteAdvertiseAdminPrice
} from "../../api/advertiseAdminApi";

export default function AdvertiseAdminPrice() {

    const [priceList, setPriceList] = useState([]);

    const [form, setForm] = useState({
        paymentType: "INITIAL",
        adGrade: "GENERAL",
        periodDays: 1,
        basePrice: 10000
    });

    const [editId, setEditId] = useState(null);


    // 가격 목록 조회
    const loadPriceList = async () => {

        try {

            const response =
                await getAdvertiseAdminPriceList();

            setPriceList(response.data);

        } catch (error) {

            console.error(
                "가격 목록 조회 실패",
                error
            );
        }
    };


    useEffect(() => {

        loadPriceList();

    }, []);


    // 입력값 변경
    const handleChange = (e) => {

        const { name, value } = e.target;

        setForm({
            ...form,
            [name]:
                name === "periodDays" || name === "basePrice"
                    ? Number(value)
                    : value
        });
    };


    // 등록 / 수정
    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            if (editId) {

                await updateAdvertiseAdminPrice(
                    editId,
                    form
                );

                alert("가격이 수정되었습니다.");

            } else {

                await createAdvertiseAdminPrice(form);

                alert("가격이 등록되었습니다.");
            }

            setForm({
                paymentType: "INITIAL",
                adGrade: "GENERAL",
                periodDays: 1,
                basePrice: 10000
            });

            setEditId(null);

            loadPriceList();

        } catch (error) {

            console.error(
                "가격 저장 실패",
                error
            );

            alert(
                error.response?.data?.message
                || "가격 저장에 실패했습니다."
            );
        }
    };


    // 수정 버튼
    const handleEdit = (price) => {

        setEditId(price.priceId);

        setForm({
            paymentType: price.paymentType,
            adGrade: price.adGrade,
            periodDays: price.periodDays,
            basePrice: price.basePrice
        });
    };


    // 삭제
    const handleDelete = async (priceId) => {

        if (!confirm("정말 삭제하시겠습니까?")) {
            return;
        }

        try {

            await deleteAdvertiseAdminPrice(priceId);

            alert("가격이 삭제되었습니다.");

            loadPriceList();

        } catch (error) {

            console.error(
                "가격 삭제 실패",
                error
            );

            alert("가격 삭제에 실패했습니다.");
        }
    };


    // 수정 취소
    const handleCancel = () => {

        setEditId(null);

        setForm({
            paymentType: "INITIAL",
            adGrade: "GENERAL",
            periodDays: 1,
            basePrice: 10000
        });
    };


    return (
        <div>

            <h1>광고 가격 관리</h1>


            {/* 가격 등록 / 수정 */}
            <form onSubmit={handleSubmit}>

                <div>

                    <label>결제 유형</label>

                    <select
                        name="paymentType"
                        value={form.paymentType}
                        onChange={handleChange}
                    >
                        <option value="INITIAL">
                            최초 결제
                        </option>

                        <option value="EXTENSION">
                            연장 결제
                        </option>
                    </select>

                </div>


                <div>

                    <label>광고 등급</label>

                    <select
                        name="adGrade"
                        value={form.adGrade}
                        onChange={handleChange}
                    >
                        <option value="GENERAL">
                            일반
                        </option>

                        <option value="PREMIUM">
                            프리미엄
                        </option>
                    </select>

                </div>


                <div>

                    <label>게시 기간</label>

                    <input
                        type="number"
                        name="periodDays"
                        min="1"
                        value={form.periodDays}
                        onChange={handleChange}
                    />

                    일

                </div>


                <div>

                    <label>가격</label>

                    <input
                        type="number"
                        name="basePrice"
                        min="0"
                        value={form.basePrice}
                        onChange={handleChange}
                    />

                    원

                </div>


                <button type="submit">

                    {editId ? "수정" : "등록"}

                </button>


                {editId && (

                    <button
                        type="button"
                        onClick={handleCancel}
                    >
                        취소
                    </button>

                )}

            </form>


            {/* 가격 목록 */}
            <table>

                <thead>

                    <tr>

                        <th>ID</th>
                        <th>결제 유형</th>
                        <th>광고 등급</th>
                        <th>기간</th>
                        <th>가격</th>
                        <th>관리</th>

                    </tr>

                </thead>


                <tbody>

                    {priceList.length === 0 ? (

                        <tr>

                            <td colSpan="6">
                                등록된 가격이 없습니다.
                            </td>

                        </tr>

                    ) : (

                        priceList.map((price) => (

                            <tr key={price.priceId}>

                                <td>
                                    {price.priceId}
                                </td>

                                <td>
                                    {price.paymentType}
                                </td>

                                <td>
                                    {price.adGrade}
                                </td>

                                <td>
                                    {price.periodDays}일
                                </td>

                                <td>
                                    {Number(
                                        price.basePrice
                                    ).toLocaleString()}원
                                </td>

                                <td>

                                    <button
                                        onClick={() =>
                                            handleEdit(price)
                                        }
                                    >
                                        수정
                                    </button>

                                    <button
                                        onClick={() =>
                                            handleDelete(
                                                price.priceId
                                            )
                                        }
                                    >
                                        삭제
                                    </button>

                                </td>

                            </tr>

                        ))

                    )}

                </tbody>

            </table>

        </div>
    );
}