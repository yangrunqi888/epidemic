import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getNotice(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/notice/selectNotice",
        method:"post",
        data,
        headers:{
            "token":token
        },
    })
}

export function insertData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/notice/insertNotice",
        method:"post",
        data,
        headers:{
            "token":token
        },
    })
}
export function deleteData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/notice/deleteNotice/"+data,
        method:"delete",
        headers:{
            "token":token
        },
    })
}