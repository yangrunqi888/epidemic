import Cookies from 'js-cookie'
import http from '@/utils/request'
export function getResisdentProhibit(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/resident/getProhibit/"+data,
        method:"get",
        headers:{
            "token":token
        },
        responseType:"blob"
    })
}
export function uploadResisdentProhibit(userId, formData) {
    const token=Cookies.get("token");
    return http({
      url: `/local/resident/upload/${userId}`,
      method: 'post',
      headers: {
        'Content-Type': 'multipart/form-data',
        "token":token
      },
      data: formData
    })
  }
export function getData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/resident/selectPage",
        method:"post",
        data,
        headers:{
            "token":token
        },
    })
}

export function getList(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/resident/selectResident/"+data,
        method:"get",
        headers:{
            "token":token
        },
    })
}

export function insertData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/resident/insertResident",
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
        url:"/local/resident/deleteResident/"+data,
        method:"delete",
        headers:{
            "token":token
        },
    })
}

export function resetPassword(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/resident/resetResident/"+data,
        method:"put",
        headers:{
            "token":token
        },
    })
}

export function editResident(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/resident/updateResident",
        method:"put",
        data,
        headers:{
            "token":token
        },
    })
}