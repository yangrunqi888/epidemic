import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getList(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/building/selectBuilding/"+data,
        method:"get",
        headers:{
            "token":token
        },
    })
}
export function getData(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/building/selectPage",
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
        url:"/local/building/deleteBuilding/"+data,
        method:"delete",
        headers:{
            "token":token
        },
    })
}

export function insertData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/building/insertBuilding",
        method:"post",
        data,
        headers:{
            "token":token
        },
    })
}

export function editBuilding(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/building/updateBuilding",
        method:"put",
        data,
        headers:{
            "token":token
        },
    })
}