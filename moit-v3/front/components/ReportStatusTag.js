import { Tag } from 'antd';

function ReportStatusTag({ status }) {
    if (status === 'PENDING') {
        return <Tag color="orange">처리 대기</Tag>;
    }
    if (status === 'APPROVED') {
        return <Tag color="green">승인</Tag>;
    }
    if (status === 'REJECTED') {
        return <Tag color="red">반려</Tag>;
    }

    return null;
}

export default ReportStatusTag;