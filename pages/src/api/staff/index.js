import Cookies from 'js-cookie'
import http from '../../utils/request'

export function getProhibit(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/staff/getProhibit/"+data,
        method:"get",
        headers:{
            "token":token
        },
        responseType:"blob"
    })
}

export function selectList(){
    const token=Cookies.get("token");
    return http({
        url:"/local/staff/selectStaff",
        method:"get",
        headers:{
            "token":token
        },
    })
}


export function getData(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/staff/selectPage",
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
        url:"/local/staff/insertStaff",
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
        url:"/local/staff/deleteStaff/"+data,
        method:"delete",
        headers:{
            "token":token
        },
    })
}

export function resetPassword(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/staff/resetStaff/"+data,
        method:"put",
        headers:{
            "token":token
        },
    })
}

export function editData(data){
    const token=Cookies.get("token");

    return http({
        url:"/local/staff/updateStaff",
        method:"put",
        data,
        headers:{
            "token":token
        },
    })
}

export function uploadProhibit(userId, formData) {
    const token=Cookies.get("token");
    return http({
      url: `/local/staff/upload/${userId}`,
      method: 'post',
      headers: {
        'Content-Type': 'multipart/form-data',
        "token":token
      },
      data: formData
    })
  }