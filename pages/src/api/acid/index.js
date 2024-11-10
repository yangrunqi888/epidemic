import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getData(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/nucleic/selectNucleic",
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
        url:"/local/nucleic/deleteNucleic",
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
        url:"/local/nucleic/insertNucleic",
        method:"post",
        data,
        headers:{
            "token":token
        },
    })
}

export function getAcidPicture(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/nucleic/getAcidPicture",
        method:"post",
        data,
        headers:{
            "token":token
        },
        responseType:"blob"
    })
}

export function uploadAcid( formData) {
    const token=Cookies.get("token");
    return http({
      url: `/local/nucleic/uploadNucleic`,
      method: 'put',
      headers: {
        'Content-Type': 'multipart/form-data',
        "token":token
      },
      data: formData
    })
  }

  export function revokeAcid(data){
    const token=Cookies.get("token");
    return http({
        url:"/local/nucleic/revokeAcid",
        method:"put",
        data,
        headers:{
            "token":token
        },
    })
}