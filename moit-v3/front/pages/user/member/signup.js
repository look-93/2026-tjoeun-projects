import React, {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useRouter} from "next/router";
import {Form,Input, Button, Card, Row, Col, Radio, 
        DatePicker, Checkbox, Typography, message, Divider, Space, Progress } from "antd";
import { CheckOutlined, MailOutlined, LockOutlined, 
         UserOutlined, PhoneOutlined, } from "@ant-design/icons";   
import dayjs from "dayjs";

import {
    signupRequest,
    emailSendRequest,
    emailVerifyRequest,
    checkLoginIdRequest,
    checkEmailRequest,
    checkNicknameRequest,
    checkMobileRequest,
    resetDuplicateCheck,
    resetEmailVerification,
    checkPasswordLeakRequest,
    resetPasswordLeak
} from "../../../reducers/userReducer";

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

// 비밀번호 강도 계산
const getPasswordStrength = (password) => {
    if (!password) {
        return {
            score: 0,
            percent: 0,
            text: "",
        };
    }

    let score = 0;

    // 길이
    if (password.length >= 8) score++;
    if (password.length >= 12) score++;

    // 영문 대문자
    if (/[A-Z]/.test(password)) score++;

    // 영문 소문자
    if (/[a-z]/.test(password)) score++;

    // 숫자
    if (/[0-9]/.test(password)) score++;

    // 특수문자
    if (/[^A-Za-z0-9]/.test(password)) score++;

    if (score <= 2) {
        return {
            score,
            percent: 25,
            text: "매우 약함",
        };
    }

    if (score === 3) {
        return {
            score,
            percent: 50,
            text: "약함",
        };
    }

    if (score === 4) {
        return {
            score,
            percent: 75,
            text: "보통",
        };
    }

    if (score === 5) {
        return {
            score,
            percent: 90,
            text: "강함",
        };
    }

    return {
        score,
        percent: 100,
        text: "매우 강함",
    };
};

function Signup(){
    const dispatch = useDispatch();
    const router = useRouter();

    const [form] = Form.useForm();


// rudux 상태
const {
    loading,
    error,
    success,
    emailVerification,
    duplicateCheck,
    passwordLeak
} = useSelector((state) => state.user);

// 입력된 이메일 
const [email, setEmail] = useState(""); 

const [verificationCode, setVerificationCode] = useState("");
// 입력된 아이디 
const [loginId, setLoginId] = useState(""); 
// 입력된 닉네임 
const [nickname, setNickname] = useState(""); 
// 입력된 전화번호 
const [mobile, setMobile] = useState("");
// 입력된 비밀번호
const [password, setPassword] = useState("");

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
    dispatch(resetDuplicateCheck("loginId"));
};

// 이메일 입력변경 시
const handleEmailChange = (e)=>{
    const value = e.target.value;

    setEmail(value);
    setVerificationCode("");

    dispatch(resetDuplicateCheck("email"));
    dispatch(resetEmailVerification());
};

// 닉네임 입력변경 시
const handleNicknameChange = (e)=>{
    const value = e.target.value;

    setNickname(value);

    dispatch(resetDuplicateCheck("nickname"));
};

// 전화번호 입력변경 시
const handleMobileChange = (e)=>{
    const value = e.target.value;

    setMobile(value);

    dispatch(resetDuplicateCheck("mobile"));
};

// 아이디 중복확인
const handleCheckLoginId = ()=>{
    if(!loginId.trim()){
        message.warning("아이디를 입력해주세요.");
        return;
    }

    dispatch(checkLoginIdRequest(loginId.trim()));
};

// 비밀번호 유출검사
const handlePasswordChange = (e) => {
    const value = e.target.value;

    setPassword(value);

    // 비밀번호가 변경되면 기존 유출검사 결과 초기화
    dispatch(resetPasswordLeak());
};

useEffect(() => {

    // 8자 미만이면 검사하지 않음
    if (!password || password.length < 8) {
        return;
    }

    // 사용자가 입력을 멈춘 후 700ms 뒤 검사
    const timer = setTimeout(() => {
        dispatch(checkPasswordLeakRequest(password));
    }, 700);

    // 다음 입력이 발생하면 기존 타이머 제거
    return () => clearTimeout(timer);

}, [password, dispatch]);

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
    if(!duplicateCheck.email){
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
    if (!emailVerification.sent) {
        message.warning("먼저 인증번호를 발송해주세요.");
        return;
    }
    if(!verificationCode.trim()){
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
    
    // 중복확인 여부
    if(!duplicateCheck.loginId){
        message.error("아이디 중복확인을 완료해주세요.");
        return;
    }
    if(!duplicateCheck.email){
        message.error("이메일 중복확인을 완료해주세요.");
        return;
    }

    // 이메일 인증여부
    if(!emailVerification.verified){
        message.error("이메일 인증을 완료해주세요.");
        return;
    }

    // 중복확인 여부
    if(!duplicateCheck.nickname){
        message.error("닉네임 중복확인을 완료해주세요.");
        return;
    }
    if(!duplicateCheck.mobile){
        message.error("전화번호 중복확인을 완료해주세요.");
        return;
    }

    // 비밀번호 유출 여부
    if (!passwordLeak.checked) {
    message.error("비밀번호 보안 검증을 완료해주세요.");
    return;
    }

    if (passwordLeak.leaked) {
        message.error("유출된 비밀번호는 사용할 수 없습니다.");
        return;
    }

    // 생년월일
    let birth = null;

    if(values.birth){
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

// 비밀번호 강도
const passwordStrength = getPasswordStrength(password);

///////////////////////////////
return (
    <div style={{maxWidth:"900px", margin:"50px auto", padding:"0 20px"}}>
        <Card>
            {/* 제목 */}
            <div style={{textAlign:"center",marginBottom:"40px"}}>
                <Title level={2}>
                    MOIT 회원가입
                </Title>
                <Text type="secondary">
                    MOIT에서 새로운 모임을 시작해보세요.
                </Text>
            </div>
            <Form form={form} 
                  layout="vertical" 
                  onFinish={handleSignup}
                  initialValues={{
                    memberTypeId: 1,
                    gender: "N",
                    interestIds: []
                  }}
            >
                {/* 회원유형 */}
                <Divider orientation="left">
                    회원유형
                </Divider>

                <Form.Item label="회원유형"
                           name="memberTypeId"
                           rules={[
                            {
                                required: true,
                                message: "회원유형을 선택해주세요."
                            }
                           ]}
                >
                    <Radio.Group>
                        <Radio value={1}>
                           일반회원
                        </Radio>
                        <Radio value={2}>
                            제휴회원
                        </Radio>
                    </Radio.Group>
                </Form.Item>

                {/* 아이디 */}
                <Divider orientation="left">
                    계정정보
                </Divider>

                <Form.Item label="아이디"
                           name="loginId"
                           rules={[
                            {
                                required: true,
                                message: "아이디를 입력해주세요."
                            }
                           ]}
                >
                    <Space.Compact style={{width:"100%"}}>
                        <Input prefix={<UserOutlined />}
                               placeholder="아이디를 입력해주세요."
                               value={loginId}
                               onChange={handleLoginIdChange}
                        />
                        <Button type="primary"
                                onClick={handleCheckLoginId}
                        >
                            중복확인
                        </Button>
                    </Space.Compact>
                </Form.Item>
                {duplicateCheck.loginId && (
                    <Text type="success">
                        <CheckOutlined />
                        {""}사용 가능한 아이디입니다.
                    </Text>
                )}

                {/* 비밀번호 */}
                <Form.Item label="비밀번호"
                           name="password"
                           rules={[
                            {
                                required: true,
                                message: "비밀번호를 입력해주세요."
                            },
                            {
                                min: 8,
                                message: "비밀번호는 8자 이상 입력해주세요."
                            }
                           ]}
                           hasFeedback
                >
                    <Input.Password
                        prefix={<LockOutlined />}
                        placeholder="비밀번호를 입력해주세요."
                        onChange={handlePasswordChange}
                    />
                </Form.Item>
                {/* 비밀번호 강도 */}
                {password && (
                    <div style={{ marginTop: "-10px", marginBottom: "15px" }}>
                        <div style={{
                            display: "flex",
                            justifyContent: "space-between",
                            marginBottom: "4px"
                        }}>
                            <Text type="secondary">
                                비밀번호 강도
                            </Text>

                            <Text strong>
                                {passwordStrength.text}
                            </Text>
                        </div>

                        <Progress
                            percent={passwordStrength.percent}
                            showInfo={false}
                            size="small"
                        />
                    </div>
                )}

                {/* 비밀번호 유출 검사 */}
                {passwordLeak.checking && (
                    <div style={{ marginBottom: "10px" }}>
                        <Text type="secondary">
                            비밀번호 유출 여부 확인 중...
                        </Text>
                    </div>
                )}

                {passwordLeak.checked && !passwordLeak.leaked && (
                    <div style={{ marginBottom: "10px" }}>
                        <Text type="success">
                            <CheckOutlined />
                            {" "}유출되지 않은 안전한 비밀번호입니다.
                        </Text>
                    </div>
                )}

                {passwordLeak.checked && passwordLeak.leaked && (
                    <div style={{ marginBottom: "10px" }}>
                        <Text type="danger">
                            ⚠ 유출된 비밀번호입니다.
                            <br />
                            <strong>
                                총 {passwordLeak.count.toLocaleString()}회
                            </strong>
                            {" "}유출된 것으로 확인되었습니다.
                            <br />
                            다른 비밀번호를 사용해주세요.
                        </Text>
                    </div>
                )}

                {passwordLeak.error && (
                    <div style={{ marginBottom: "10px" }}>
                        <Text type="danger">
                            {passwordLeak.error}
                        </Text>
                    </div>
                )}

                {/* 비밀번호 확인*/}
                <Form.Item label="비밀번호 확인"
                           name="passwordConfirm"
                           dependencies={["password"]}
                           hasFeedback
                           rules={[
                            {
                                required: true,
                                message: "비밀번호를 다시 입력해주세요."
                            },                            
                            ({getFieldValue }) => ({
                                validator(_, value){
                                    if(!value || getFieldValue("password") === value){
                                        return Promise.resolve();
                                    }

                                    return Promise.reject(new Error("비밀번호가 일치하지 않습니다."));
                                }
                            })                            
                           ]}
                >
                    <Input.Password
                        prefix={<LockOutlined />}
                        placeholder="비밀번호를 다시 입력해주세요."
                    />
                </Form.Item>

                {/* 닉네임 */}
                <Form.Item label="닉네임"
                           name="nickname"
                           rules={[
                            {
                                required: true,
                                message: "닉네임을 입력해주세요."
                            }
                           ]}
                >
                    <Space.Compact style={{width:"100%"}}>
                           <Input
                                prefix={<UserOutlined />}
                                placeholder="닉네임을 입력해주세요."
                                value={nickname}
                                onChange={handleNicknameChange}
                           />
                           <Button type="primary"
                                   onClick={handleCheckNickname}
                           >
                            중복확인
                           </Button>
                    </Space.Compact>
                </Form.Item>
                {duplicateCheck.nickname && (
                    <Text type="success">
                        <CheckOutlined />
                        사용 가능한 닉네임입니다.
                    </Text>
                )}

                {/* 이메일 */}
                <Divider orientation="left">
                    이메일 인증
                </Divider>

                <Form.Item label="이메일"
                           name="email"
                           rules={[
                            {
                                required: true,
                                message: "이메일을 입력해주세요."
                            },
                            {
                                type: "email",
                                message: "올바른 이메일 형식을 입력해주세요."
                            }
                           ]}
                >
                    <Space.Compact style={{width:"100%"}}>
                           <Input
                                prefix={<MailOutlined />}
                                placeholder="이메일을 입력해주세요."
                                value={email}
                                onChange={handleEmailChange}
                           />
                           <Button onClick={handleCheckEmail}>
                            중복확인 
                           </Button>
                    </Space.Compact>
                </Form.Item>
                {duplicateCheck.email && (
                    <Text type="success">
                        <CheckOutlined />
                        이메일 사용 가능합니다.
                    </Text>
                )}

                <Form.Item label="이메일 인증번호" name="verificationCode">
                    <Space.Compact style={{ width: "100%" }}>

                        <Input
                            placeholder="인증번호 6자리를 입력해주세요."
                            maxLength={6}
                            value={verificationCode}
                            onChange={(e) => {setVerificationCode(e.target.value);}}
                            disabled={!emailVerification.sent}
                        />

                        <Button
                            type="primary"
                            loading={emailVerification.sending}
                            onClick={handleSendEmailCode}
                            disabled={!duplicateCheck.email}
                        >
                            인증번호 발송
                        </Button>

                        <Button
                            type="primary"
                            loading={emailVerification.verifying}
                            onClick={handleVerifyEmail}
                            disabled={!emailVerification.sent}
                        >
                            인증확인
                        </Button>

                    </Space.Compact>
                </Form.Item>
                {emailVerification.verified && (
                    <Text type="success">
                        <CheckOutlined />
                        {""}이메일 인증이 완료되었습니다.
                    </Text>
                )}
                {emailVerification.error && (
                    <div style={{color:"#ff4d4f", marginTop:"8px"}}>
                        {emailVerification.error}
                    </div>
                )} 

                {/* 전화번호 */}  
                <Divider orientation="left">
                    개인정보
                </Divider>  

                <Form.Item label="전화번호"
                           name="mobile"
                           rules={[
                            {
                                required: true,
                                message: "전화번호를 입력해주세요."
                            }
                           ]}
                >
                    <Space.Compact style={{width:"100%"}}>
                           <Input
                                prefix={<PhoneOutlined />}
                                placeholder="전화번호를 입력해주세요."
                                value={mobile}
                                onChange={handleMobileChange}
                           />
                           <Button type="primary"
                                   onClick={handleCheckMobile}
                           >
                            중복확인
                           </Button>
                    </Space.Compact>
                </Form.Item> 
                {duplicateCheck.mobile && (
                    <Text type="success">
                        <CheckOutlined />
                        사용 가능한 전화번호입니다.
                    </Text>
                )}

                {/* 성별 */}
                <Form.Item label="성별"
                           name="gender"
                           rules={[
                            {
                                required: true,
                                message: "성별을 선택해주세요."
                            }
                           ]}
                >
                    <Radio.Group>
                        <Radio value="M">
                            남성
                        </Radio>

                        <Radio value="F">
                            여성
                        </Radio>

                        <Radio value="N">
                            비공개
                        </Radio>
                    </Radio.Group>
                </Form.Item> 

                {/* 생년월일 */}      
                <Form.Item label="생년월일"
                           name="birth"
                           rules={[
                            {
                                required: true,
                                message: "생년월일을 입력해주세요."
                            }
                           ]}
                >
                    <DatePicker
                        style={{width:"100%"}}
                        format="YYYY-MM-DD"
                        placeholder="생년월일을 입력해주세요."
                        disabledDate={(current)=> current && current > dayjs().endOf("day")}
                    />
                </Form.Item>

                {/* 관심사 */}
                <Form.Item label="관심사"
                           name="interestIds"
                           rules={[
                            {
                                required: true,
                                message: "관심사를 하나 이상 선택해주세요."
                            }
                           ]}
                >
                    <Checkbox.Group style={{width:"100%"}}>
                        <Row gutter={[16,16]}>
                           {interests.map((interest)=>(
                            <Col xs={12} sm={8} md={6} key={interest.id}>
                                <Checkbox value={interest.id}>
                                    {interest.name}
                                </Checkbox>
                            </Col>
                           ))}
                        </Row>
                    </Checkbox.Group>
                </Form.Item>

                {/* 회원가입 버튼 */}
                <Form.Item style={{marginTop:"40px"}}>
                    <Button type="primary"
                            htmlType="submit"
                            size="large"
                            block
                            loading={loading}
                    >
                    회원가입
                    </Button>
                </Form.Item>

                {/* 로그인 페이지로 이동 */}
                <div style={{textAlign:"center"}}>
                    <Text type="secondary">
                        이미 회원이신가요?
                    </Text>
                    <Button type="link"
                            onClick={()=>router.push("/user/member/login")}
                    >
                        로그인
                    </Button>
                </div> 

                {error && (
                    <div style={{marginTop:"20px", textAlign:"center", color:"#ff4d4f"}}>
                        {error}
                    </div>
                )}    

            </Form>
        </Card>
    </div>
);
}
export default Signup;