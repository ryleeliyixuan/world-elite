import axios from 'axios'
import Vue from 'vue'
import store from '@/store'
import router from '@/router'
import { getToken } from '@/utils/auth'
import Toast from '@/utils/toast'
import {curRelativePath} from '@/utils/common'

// allow cookie
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
    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['X-Token'] = getToken()
      console.log(config);
    }
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
  response => {
    console.log(response);
    const res = response.data

    if (res.code !== 0) {
      if (res.code === 402) {
        Toast.error(res.msg);
        // to re-login
        store.dispatch('user/LOGOUT').then(() => {
          router.push({path: '/login', query: {redirect: curRelativePath()}})
        });
      }else if(res.code === 405){
         router.push({path: '/verify-status'})
      }else{
        Toast.error(res.msg);
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res
    }
  },
  error => {
    Toast.error(error.message);
    return Promise.reject(error)
  }
)

Vue.prototype.$axios = service;

export default service
