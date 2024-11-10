import Cookies from 'js-cookie'
import http from '../../utils/request'

export function getGuardProhibit(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/guard/getProhibit/"+data,
        method:"get",
        headers:{
            "token":token
        },
        responseType:"blob"
    })
}

export function getData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/guard/selectPage",
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
        url:"/local/guard/insertGuard",
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
        url:"/local/guard/deleteGuard/"+data,
        method:"delete",
        headers:{
            "token":token
        },
    })
}

export function resetPassword(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/guard/resetGuard/"+data,
        method:"put",
        headers:{
            "token":token
        },
    })
}

export function editData(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/guard/updateGuard",
        method:"put",
        data,
        headers:{
            "token":token
        },
    })
}

export function uploadStaffProhibit(userId, formData) {
    const token=Cookies.get("token");
    return http({
      url: `/local/guard/upload/${userId}`,
      method: 'post',
      headers: {
        'Content-Type': 'multipart/form-data',
        "token":token
      },
      data: formData
    })
  }