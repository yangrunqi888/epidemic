import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getData(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/complaint/selectComplaint",
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
        url:"/local/complaint/deleteComplaint/"+data,
        method:"delete",
        headers:{
            "token":token
        },
    })
}
export function copeComplaint(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/complaint/copeComplaint",
        method:"put",
        data,
        headers:{
            "token":token
        },
    })
}

export function commentComplaint(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/complaint/commentComplaint",
        method:"put",
        data,
        headers:{
            "token":token
        },
    })
}

export function insertData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/complaint/insertComplaint",
        method:"post",
        data,
        headers:{
            "token":token
        },
    })
}