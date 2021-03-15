import axios from 'axios';
import store from '../store'
import Element from "element-ui"
import router from "../router";
axios.defaults.baseURL = 'http://localhost:8081'; // 请求的默认域名
// 添加一个请求拦截器
axios.interceptors.request.use(config => {
        config.headers.languagetype = 'CN'; // 举例，加上一个公共头部
        return config;
    },
    err => {
        return Promise.reject(err);
    });
//添加一个响应拦截器
axios.interceptors.response.use(response => {
    //在这里对返回的数据进行处理
    let res = response.data
    if (res.code/100 == 2){
        return response;
    }else{
        Element.Message.error(res.msg)
        return Promise.reject(res.msg)
    }
}, error => {
    // console.log( error)
    //未授权
    if(error.response.data) {
        error.message = error.response.data.msg
    }
    if(error.response.status === 401){
        store.commit('REMOVE_INFO')
        router.push("/login")
        error.message = "请重新登录"
    }
    if(error.response.status === 403){
        error.message = "权限不足，无法访问"
    }

    Element.Message.error(error.message)
    return Promise.reject(error)
})

export default axios

