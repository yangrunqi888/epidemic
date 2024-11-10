import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getList(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/gridManage/selectByEmployee/"+data,
        method:"get",
        headers:{
            "token":token
        },
    })
}

export function getData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/grid/selectPage",
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
        url:"/local/grid/insertGrid",
        method:"post",
        data,
        headers:{
            "token":token
        },
    })
}

export function updatetData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/grid/updateGrid",
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
        url:"/local/grid/deleteGrid/"+data,
        method:"delete",
        headers:{
            "token":token
        },
    })
}