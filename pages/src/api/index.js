import http from '../utils/request'

//请求数据定义接口
export const getData=()=>{

    return http.get('/ncov/index?key=e554e3fcbe53b6cef0a43579393de654')
}
export const getChinaData=()=>{

    return http.get('/news/wap/fymap2020_data.d.json')
}
export const getProvinceData=(keyword)=>{
    return http({
        url:"/province/api_region_search/v1/",
        method:"get",
        params:{
            ak:"ipsFG2yRiZB2uRWxvQqTu3ZrS8XfsEPU",
            sub_admin:2,
            keyword
        }
    })
    
}
