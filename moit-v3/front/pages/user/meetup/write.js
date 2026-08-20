import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import dayjs from "dayjs";
import {
    Button,
    Card,
    Col,
    DatePicker,
    Form,
    Input,
    InputNumber,
    Modal,
    Row,
    Select,
    Space,
    Table,
    Typography,
    message,
} from "antd";
import { EnvironmentOutlined } from "@ant-design/icons";
import MeetupImageUpload from "../../../components/MeetupImageUpload";
import AddressSearchModal from "../../../components/AddressSearchModal";
import MeetupRecruitSettings from "../../../components/MeetupRecruitSettings";
import MeetupLocationCard from "../../../components/MeetupLocationCard";
import MeetupInfoForm from "../../../components/MeetupInfoForm";

import { useSelector, useDispatch } from "react-redux";
import {
    fetchCategoriesRequest,
    fetchSigungusRequest,
    createMeetupRequest,
    updateMeetupRequest,
    fetchMeetupDetailRequest,
    resetMeetupState,
    recommendMeetupRequest,
} from "../../../reducers/meetupReducer";
import { searchAddressRequest } from "../../../reducers/commonReducer";

const { Title, Text } = Typography;
const { TextArea } = Input;

function write() {
    const dispatch = useDispatch();
    const router = useRouter();
    const { meetupId } = router.query;

    const [form] = Form.useForm();

    const isEdit = Boolean(meetupId);

    const [fileList, setFileList] = useState([]);
    const [previewImages, setPreviewImages] = useState([]);
    const [currentImage, setCurrentImage] = useState(0);

    //ai
    const [aiLoading, setAiLoading] = useState(false);
    const titleValue = Form.useWatch("title", form);
    const [showAiGuide, setShowAiGuide] = useState(false);

    //주소
    const [addressModalOpen, setAddressModalOpen] = useState(false);
    const [addressKeyword, setAddressKeyword] = useState("");
    const [addressPage, setAddressPage] = useState(1);

    const [selectedAddress, setSelectedAddress] = useState(null);

    const { addressList, addressLoading, addressTotal } = useSelector(
        (state) => state.common,
    );

    const {
        categories,
        sigungus,
        meetup,
        createSuccess,
        updateSuccess,
        error,
    } = useSelector((state) => state.meetup);

    useEffect(() => {
        if (!isEdit || !meetup) {
            return;
        }
        //console.log("🔥 수정할 meetup:", meetup);
        // 기존 이미지
        if (meetup.imagePaths?.length > 0) {
            const existingImages = meetup.imagePaths.map(
                (imagePath, index) => ({
                    uid: `existing-${index}`,
                    name: imagePath,
                    status: "done",
                    url: `http://localhost:8080/upload/meetup/${imagePath}`,
                }),
            );

            setFileList(existingImages);
            setPreviewImages(existingImages.map((image) => image.url));
        }

        form.setFieldsValue({
            title: meetup.title,
            content: meetup.content,
            minParticipants: meetup.minParticipants,
            maxParticipants: meetup.maxParticipants,
            meetupStatus: meetup.meetupStatus,
            address: meetup.address,
            addressDetail: meetup.addressDetail,
            sigunguId: meetup.sigunguId,
            categoryId: meetup.categoryId,
            nx: meetup.nx,
            ny: meetup.ny,
            latitude: meetup.latitude,
            longitude: meetup.longitude,
            meetupAt: meetup.meetupAt ? dayjs(meetup.meetupAt) : null,
        });

        setSelectedAddress({
            address: meetup.address,
            addressDetail: meetup.addressDetail,
            sigunguId: meetup.sigunguId,
            nx: meetup.nx,
            ny: meetup.ny,
            latitude: meetup.latitude,
            longitude: meetup.longitude,
        });
    }, [isEdit, meetup, form]);

    //ai추천
    useEffect(() => {
        // 수정 페이지에서는 AI 안내 X
        if (isEdit) return;

        // 페이지 진입 후 10초
        const timer = setTimeout(() => {
            setShowAiGuide(true);
        }, 10000);

        return () => clearTimeout(timer);
    }, [isEdit]);

    useEffect(() => {
        if (isEdit) return;

        // 아직 10초가 안 지났으면 실행 X
        if (!showAiGuide) return;

        // 제목이 없으면 실행 X
        if (!titleValue?.trim()) return;

        dispatch(
            recommendMeetupRequest({
                keyword: titleValue,
            }),
        );
    }, [titleValue, showAiGuide, isEdit, dispatch]);

    //카테고리
    const categoriesOptions = categories
        .filter((cate) => cate.parentId != null)
        .map((cate) => ({ value: cate.id, label: cate.categoryName }));

    //이미지
    const handleImageChange = ({ fileList: newFileList }) => {
        const limitedList = newFileList.slice(0, 5);

        setFileList(limitedList);

        const previews = limitedList.map((file) => {
            if (file.originFileObj) {
                return URL.createObjectURL(file.originFileObj);
            }

            return file.url;
        });

        setPreviewImages(previews);
        setCurrentImage(0);
    };

    const moveImage = (direction) => {
        if (previewImages.length === 0) {
            return;
        }

        setCurrentImage((prev) => {
            const next = prev + direction;

            if (next < 0) {
                return previewImages.length - 1;
            }

            if (next >= previewImages.length) {
                return 0;
            }

            return next;
        });
    };

    useEffect(() => {
        dispatch(fetchCategoriesRequest());
        dispatch(fetchSigungusRequest());

        if (router.isReady && meetupId) {
            dispatch(fetchMeetupDetailRequest(meetupId));
        }
    }, [router.isReady, meetupId, dispatch]);

    useEffect(() => {
        return () => {
            previewImages.forEach((url) => URL.revokeObjectURL(url));
        };
    }, [previewImages]);

    const searchAddress = () => {
        if (addressKeyword.trim().length < 2) {
            message.warning("주소를 두 글자 이상 입력해주세요.");
            return;
        }

        setAddressPage(1);

        dispatch(
            searchAddressRequest({
                searchAddress: addressKeyword,
                page: 0,
                size: 10,
            }),
        );
    };

    const handleAddressPageChange = (page) => {
        setAddressPage(page);

        dispatch(
            searchAddressRequest({
                searchAddress: addressKeyword.trim(),
                page: page - 1,
                size: 10,
            }),
        );
    };

    const handleAddressSelect = (record) => {
        setSelectedAddress(record);

        form.setFieldsValue({
            address: record.address,
            sigunguId: record.sigunguId,
            nx: record.nx,
            ny: record.ny,
            longitude: record.longitude,
            latitude: record.latitude,
        });

        setAddressModalOpen(false);

        message.success("주소가 선택되었습니다.");
    };

    const handleSubmit = (values) => {
        const data = {
            ...values,
            meetupAt: values.meetupAt?.format("YYYY-MM-DDTHH:mm:ss"),
        };

        const existingImagePaths = fileList
            .filter((file) => !file.originFileObj)
            .map((file) => file.name);

        const files = fileList
            .map((file) => file.originFileObj)
            .filter(Boolean);
        console.log("🔥 fileList:", fileList);
        console.log("🔥 existingImagePaths:", existingImagePaths);
        console.log("🔥 files:", files);
        if (isEdit) {
            dispatch(
                updateMeetupRequest({
                    meetupId,
                    data,
                    files,
                    existingImagePaths,
                }),
            );
        } else {
            dispatch(
                createMeetupRequest({
                    dto: data,
                    files,
                }),
            );
        }
    };

    useEffect(() => {
        if (createSuccess) {
            message.success("모임이 등록되었습니다.");
            dispatch(resetMeetupState());
            router.push("/user/meetup");
        }

        if (updateSuccess) {
            message.success("모임이 수정되었습니다.");
            dispatch(resetMeetupState());
            router.push("/user/meetup");
        }

        if (error) {
            message.error("저장 중 오류가 발생했습니다.");
        }
    }, [createSuccess, updateSuccess, error, dispatch, router]);

    return (
        <div className="mypage-main-content">
            <Form form={form} layout="vertical" onFinish={handleSubmit}>
                <Row gutter={[20, 20]}>
                    <Col xs={24} lg={16}>
                        <Card className="mypage-user-info">
                            <Title level={3} className="member-edit-title">
                                {isEdit ? "모임 수정하기" : "새 모임 등록"}
                            </Title>

                            <MeetupImageUpload
                                previewImages={previewImages}
                                currentImage={currentImage}
                                fileList={fileList}
                                onImageChange={handleImageChange}
                                onMoveImage={moveImage}
                            />

                            <Text
                                type="secondary"
                                style={{
                                    display: "block",
                                    marginTop: 8,
                                    fontSize: 12,
                                }}
                            >
                                최대 5장까지 등록할 수 있습니다.
                            </Text>
                        </Card>

                        <MeetupInfoForm categoriesOptions={categoriesOptions} />
                    </Col>

                    <Col xs={24} lg={8}>
                        <MeetupRecruitSettings
                            isEdit={isEdit}
                            onAddressClick={() => setAddressModalOpen(true)}
                            sigungus={sigungus}
                        />

                        <MeetupLocationCard selectedAddress={selectedAddress} />

                        <Card
                            className="mypage-user-info"
                            style={{ marginTop: 20 }}
                        >
                            <Title level={4}>작성 가이드</Title>

                            <Space direction="vertical" size={8}>
                                <Text type="secondary">
                                    • 모임 목적을 명확히 작성해주세요.
                                </Text>

                                <Text type="secondary">
                                    • 장소와 시간을 정확히 입력해주세요.
                                </Text>

                                <Text type="secondary">
                                    • 참가 조건이 있다면 소개글에 작성해주세요.
                                </Text>
                            </Space>
                        </Card>
                    </Col>
                </Row>
            </Form>
            <AddressSearchModal
                open={addressModalOpen}
                onCancel={() => setAddressModalOpen(false)}
                keyword={addressKeyword}
                onKeywordChange={setAddressKeyword}
                onSearch={searchAddress}
                addressList={addressList}
                loading={addressLoading}
                currentPage={addressPage}
                total={addressTotal}
                onPageChange={handleAddressPageChange}
                onSelect={handleAddressSelect}
            />
        </div>
    );
}

export default write;
