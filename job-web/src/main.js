import 'core-js/stable';
import 'regenerator-runtime/runtime';
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import VueResource from 'vue-resource'
import { BootstrapVue} from 'bootstrap-vue'
import '@/style/app.scss'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import i18n from '@/i18n'
import ViewUI from 'view-design';
import 'view-design/dist/styles/iview.css';


import echarts from "echarts";
import storage from "@/utils/storage"


import '@/permission'
import '@/icons' // icon

import * as filters from './filters/filters'
Object.keys(filters).forEach(key => {
    Vue.filter(key, filters[key])
})

const version = "2.0";

import Cookies from 'universal-cookie';
const cookies = new Cookies();

if (cookies.get("version") !== version) {
    Object.keys(cookies.getAll()).forEach(key => {
        cookies.remove(key)
    })
    cookies.set("version", version)
}

Vue.use(storage);
Vue.use(VueResource)
Vue.config.productionTip = false

Vue.use(ViewUI)
Vue.use(ElementUI)
Vue.use(BootstrapVue)
Vue.component('font-awesome-icon', FontAwesomeIcon)
Vue.prototype.$echarts = echarts;


const _vm = new Vue({
    router,
    store,
    i18n,
    render: h => h(App),
}).$mount('#app')

document._vm_instance = _vm
