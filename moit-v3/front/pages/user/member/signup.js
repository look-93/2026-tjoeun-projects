import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useRouter} from "next/router";
import {Form,Input, Button, Card, Row, Col, Radio, 
        DatePicker, Checkbox, Typography, message, Divider, Space, } from "antd";
import { CheckOutlined, MailOutlined, LockOutlined, 
         UserOutlined, PhoneOutlined, } from "@ant-design/icons";   
import dayjs from "dayjs";

import {signupRequest, emailSendRequest, emailVerifyRequest, checkLoginIdRequest, 
        checkEmailRequest, checkNicknameRequest, checkMobileRequest} from "../../../reducers/userReducer";

const {Title,Text} = Typography;      

// 관심사
const interests=[
    {id: 1, name: "운동"},
    {id: 2, name: "여행"},
    {id: 3, name: "게임"},
    {id: 4, name: "독서"},
    {id: 5, name: "맛집"},
    {id: 6, name: "영화"},
    {id: 7, name: "음악"},
    {id: 8, name: "요리"},
];

function Signup(){
    const dispatch = useDispatch();
    const router = useRouter();

    const [form] = Form.useForm();
}

// rudux 상태
const {loading, error, success, emailVerification} = useSelector((state)=>state.user);

// 아이디 중복확인 완료 여부 
const [loginIdChecked, setLoginIdChecked] = useState(false); 
// 이메일 중복확인 완료 여부 
const [emailChecked, setEmailChecked] = useState(false); 
// 닉네임 중복확인 완료 여부 
const [nicknameChecked, setNicknameChecked] = useState(false); 
// 전화번호 중복확인 완료 여부 
const [mobileChecked, setMobileChecked] = useState(false); 
// 입력된 이메일 
const [email, setEmail] = useState(""); 
// 입력된 아이디 
const [loginId, setLoginId] = useState(""); 
// 입력된 닉네임 
const [nickname, setNickname] = useState(""); 
// 입력된 전화번호 
const [mobile, setMobile] = useState("");

// 회원가입 성공처리
useEffect(()=>{
    if(success){
        message.success("회원가입이 완료되었습니다.");
        router.push("/user/member/login");
    }
},[success,router]);

// 아이디 입력변경 시
const handleLoginIdChange = (e)=>{
    const value = e.target.value;

    setLoginId(value);

    //아이디가 변경되면 기존 중복확인 무효처리
    setLoginIdChecked(false);
};

// 이메일 입력변경 시
const handleEmailChange = (e)=>{
    const value = e.target.value;

    setEmail(value);

    setEmailChecked(false);
};

// 닉네임 입력변경 시
const handleNicknameChange = (e)=>{
    const value = e.target.value;

    setNickname(value);

    setNicknameChecked(false);
};

// 전화번호 입력변경 시
const handleMobileChange = (e)=>{
    const value = e.target.value;

    setMobile(value);

    setMobileChecked(false);
};

// 아이디 중복확인
const handleCheckLoginId = ()=>{
    if(!loginId.trim()){
        message.warning("아이디를 입력해주세요.");
        return;
    }

    dispatch(checkLoginIdRequest(loginId.trim()));
};

// 이메일 중복확인
const handleCheckEmail = ()=>{
    if(!email.trim()){
        message.warning("이메일을 입력해주세요.");
        return;
    }

    dispatch(checkEmailRequest(email.trim()));
};

// 닉네임 중복확인
const handleCheckNickname = ()=>{
    if(!nickname.trim()){
        message.warning("닉네임을 입력해주세요.");
        return;
    }

    dispatch(checkNicknameRequest(nickname.trim()));
};

// 전화번호 중복확인
const handleCheckMobile = ()=>{
    if(!mobile.trim()){
        message.warning("전화번호를 입력해주세요.");
        return;
    }

    dispatch(checkMobileRequest(mobile.trim()));
};

// 이메일 인증번호 발송
const handleSendEmailCode = ()=>{
    if(!email.trim()){
        message.warning("이메일을 입력해주세요.");
        return;
    }
    if(!emailChecked){
        message.warning("이메일 중복확인을 먼저 진행해주세요.");
        return;
    }

    dispatch(emailSendRequest(email.trim()));
};

// 이메일 인증번호 확인
const handleVerifyEmail = ()=>{
    const code = form.getFieldValue("verificationCode");

    if(!email.trim()){
        message.warning("이메일을 입력해주세요.");
        return;
    }
    if(!code || !code.trim()){
        message.warning("인증번호를 입력해주세요.");
        return;
    }

    dispatch(emailVerifyRequest({
        email: email.trim(),
        code: code.trim()
    }));
};

// 회원가입
const handleSignup = (values)=>{
    // 이메일 인증여부
    if(!emailVerification.verified){
        message.error("이메일 인증을 완료해주세요.");
        return;
    }

    // 중복확인 여부
    if(!loginIdChecked){
        message.error("아이디 중복확인을 완료해주세요.");
        return;
    }
    if(!emailChecked){
        message.error("이메일 중복확인을 완료해주세요.");
        return;
    }
    if(!nicknameChecked){
        message.error("닉네임 중복확인을 완료해주세요.");
        return;
    }
    if(!mobileChecked){
        message.error("전화번호 중복확인을 완료해주세요.");
        return;
    }

    // 생년월일
    let birth = null;

    if(valuse.birth){
        birth = values.birth.format("YYYY-MM-DD");
    }

    // 회원가입 데이터
    const signupData = {
        loginId: values.loginId,
        password: values.password,
        nickname: values.nickname,
        email: values.email,
        mobile: values.mobile,
        memberTypeId: Number(values.memberTypeId),
        gender: values.gender,
        birth: birth,
        profileUrl: "",
        interestIds: values.interestIds || [],
    };

    console.log("회원가입 요청 데이터:", signupData);
    
    dispatch(signupRequest(signupData));
};

///////////////////////////////
return (
    <div style={{maxWidth:"900px", margin:"50px auto", padding:"0 20px"}}>
        <Card>
            {/* 제목 */}
            <div style={{textAlign:"center",marginBottom:"40px"}}>
                <Title level={2}>
                    MOIT 회원가입
                </Title>
            </div>
        </Card>
    </div>
);