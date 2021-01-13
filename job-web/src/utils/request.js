import axios from 'axios'
import Vue from 'vue'
import store from '@/store'
import router from '@/router'
import {getToken} from '@/utils/auth'
import Toast from '@/utils/toast'
import {curRelativePath} from '@/utils/common'

// cookie
axios.defaults.withCredentials = true;

// create an axios instance
const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
    // withCredentials: true, // send cookies when cross-domain requests
    timeout: 20000 // request timeout
})

// request interceptor
service.interceptors.request.use(
    config => {
        // do something before request is sent
        const token = getToken()
        console.log("X-Token", token);
        if (token) {
            // let each request carry token
            // ['X-Token'] is a custom headers key
            // please modify it according to the actual situation
            config.headers['X-Token'] = token
        }
        console.log(config.url, config);
        return config
    },
    error => {
        // do something with request error
        return Promise.reject(error)
    }
)

// response interceptor
service.interceptors.response.use(
    /**
     * If you want to get http information such as headers or status
     * Please return  response => response
     */

    /**
     * Determine the request status by custom code
     * Here is just an example
     * You can also judge the status by HTTP Status Code
     */
    async response => {
        console.log(response.config.url, response);
        const res = response.data

        if (res.code !== undefined && res.code !== 0) {
            Toast.close(); // 最多显示一个错误信息
            Toast.error(res.msg);

            if (res.code === 402) {
                // to re-login
                await store.dispatch('user/LOGOUT').then(() => {
                    router.push({path: '/login', query: {redirect: curRelativePath()}})
                });
            }
            return Promise.reject(new Error(res.msg || 'Error'))
        } else if (res.code === undefined) {
            return response
        } else {
            return res;
        }
    },
    error => {
        Toast.close(); // 最多显示一个错误信息
        Toast.error(error.message);
        return Promise.reject(error)
    }
)

// POST multipart/form-data 格式上传文件
service.upload = (url, file, param) => {
    let formData = new FormData();
    for (let key in param) {
        if (param.hasOwnProperty(key))
            formData.append(key, param[key]);
    }
    formData.append("file", file, file.name);
    let config = {
        headers: {"Content-Type": "multipart/form-data"}
    }
    return axios.post(url, formData, config)
}

Vue.prototype.$axios = service;

export default service
