import request from '@/utils/request';
import Cookies from 'js-cookie'
export function insertComplaint(data) {
    const token=Cookies.get("token");
    return request({
        url: '/local/rubbish/send',  // 假设后端的吐槽 API 路径
        method: 'post',
        headers:{
            "token":token
        },
        data
    });
}