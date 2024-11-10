import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getData(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/out/selectOut",
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
        url:"/local/out/insertOut",
        method:"post",
        data,
        headers:{
            "token":token
        },
    })
}

export function updateData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/out/updateOut",
        method:"put",
        data,
        headers:{
            "token":token
        },
    })
}

export function deleteData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/out/deleteOut",
        method:"delete",
        data,
        headers:{
            "token":token
        },
    })
}