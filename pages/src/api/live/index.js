import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getlivingList(data){

    const token=Cookies.get("token");

    return http({
        url:"/local/live/selectLiving/"+data,
        method:"get",
        headers:{
            "token":token
        },
    })
}