import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getData(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/temperature/selectTemperature",
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
        url:"/local/temperature/deleteTemperature",
        method:"delete",
        data,
        headers:{
            "token":token
        },
    })
}

export function insertData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/temperature/insertTemperature",
        method:"post",
        data,
        headers:{
            "token":token
        },
    })
}