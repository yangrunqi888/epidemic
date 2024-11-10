import Mock from 'mockjs'
import chinaApi from './mockServerData/chinaData'
//定义mock请求拦截
Mock.mock("http://www.888.com",function(){
    //拦截到请求后的处理逻辑
    console.log("888已被拦截")
    return chinaApi.getData();
})

Mock.mock("13.com",function(){
    // //拦截到请求后的处理逻辑
    // console.log("已被拦截")
    // return chinaApi.getData();
})
