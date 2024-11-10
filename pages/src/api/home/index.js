import Cookies from 'js-cookie'
import http from '@/utils/request'

export function getData(data){
    const token=Cookies.get("token");
    console.log(data)
    return http({
        url:"/local/home/getHomeInformation",
        method:"post",
        data,
        headers:{
            "token":token
        },
    })
}