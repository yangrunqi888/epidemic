import http from '../../utils/request'


export function staffLogin(data){
    return http({
        // url:"/local/login/staffLogin",
        url:"http://localhost:8850/login/staffLogin",
        method:"post",
        data
    })
}

export function guardLogin(data){
    return http({
        url:"/local/login/guardLogin",
        method:"post",
        data
    })
}

export function residentLogin(data){
    return http({
        url:"/local/login/residentLogin",
        method:"post",
        data
    })
}