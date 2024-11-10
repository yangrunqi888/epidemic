import Cookies from 'js-cookie'
import http from '../../utils/request'
export function changePassword(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/password/change",
        method:"put",
        data,
        headers:{
            "token":token
        },
    })
}