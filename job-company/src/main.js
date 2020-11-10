import 'core-js/stable';
import 'regenerator-runtime/runtime';
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import {BootstrapVue} from 'bootstrap-vue'
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import '@/style/app.scss'
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome'

import i18n from '@/i18n'
import '@/permission'
import '@/icons' // icon
import Loading from "@/components/Loading";

import storage from "@/utils/storage"

import FullCalendar from 'vue-full-calendar'

Vue.use(FullCalendar)

// 数据存储
Vue.use(storage);

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(BootstrapVue)
Vue.use(Loading)
Vue.component('font-awesome-icon', FontAwesomeIcon)

const _vm = new Vue({
    router,
    store,
    i18n,
    render: h => h(App),
}).$mount('#app')

document._vm_instance = _vm

Date.prototype.format = function (fmt) {

    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };

    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}
