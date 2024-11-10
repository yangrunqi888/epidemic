import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getData(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/backHome/selectBackHome",
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
        url:"/local/backHome/deleteBack",
        method:"delete",
        data,
        headers:{
            "token":token
        },
    })
}

export function getHealthPicture(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/backHome/getHealthPicture",
        method:"post",
        data,
        headers:{
            "token":token
        },
        responseType:"blob"
    })
}

export function getTravelPicture(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/backHome/getTravelPicture",
        method:"post",
        data,
        headers:{
            "token":token
        },
        responseType:"blob"
    })
}
export function verify(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/backHome/verify",
        method:"put",
        data,
        headers:{
            "token":token
        },
    })
}

export function insertData(formData){

    const token=Cookies.get("token");

    return http({
        url:"/local/backHome/insertBackHome",
        method:"post",
        headers:{
            'Content-Type': 'multipart/form-data',
            "token":token
        },
        data: formData
    })
}