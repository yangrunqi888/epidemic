import axios from 'axios'

const http = axios.create({
    //请求地址前缀
    // baseURL: 'http://192.169.3.84:8081',
    //超时时间(毫秒)
    timeout: 10000,

})

http.interceptors.request.use(function (config) {
    return config;
},
    function (error) {
        return Promise.reject(error);
    }
)

http.interceptors.response.use(function (response) {
    return response;
},
    function (error) {
        return Promise.reject(error);
    }
)
export default http;