import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss' // global css
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import App from './App'
import store from './store'
import router from './router'
import echarts from "echarts";
import * as filters from './filters/filters'
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

import '@/icons' // icon
import '@/permission' // permission control

Vue.use(ElementUI)
Vue.component('font-awesome-icon', FontAwesomeIcon)
Vue.prototype.$echarts = echarts;

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
