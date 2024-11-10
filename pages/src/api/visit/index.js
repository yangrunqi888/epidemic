import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getRecords(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/visit/getRecords/"+data,
        method:"get",
        headers:{
            "token":token
        },
    })
}

export function getData(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/visit/selectVisit",
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
        url:"/local/visit/insertVisit",
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
        url:"/local/visit/updateVisit",
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
        url:"/local/visit/deleteVisit",
        method:"delete",
        data,
        headers:{
            "token":token
        },
    })
}