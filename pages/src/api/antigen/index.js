import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getData(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/antigen/selectAntigen",
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
        url:"/local/antigen/deleteAntigen",
        method:"delete",
        data,
        headers:{
            "token":token
        },
    })
}

export function getAntigenPicture(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/antigen/getAntigenPicture",
        method:"post",
        data,
        headers:{
            "token":token
        },
        responseType:"blob"
    })
}

export function insertData(formData){

    const token=Cookies.get("token");

    return http({
        url:"/local/antigen/insertAntigen",
        method:"post",
        headers:{
            'Content-Type': 'multipart/form-data',
            "token":token
        },
        data: formData
    })
}