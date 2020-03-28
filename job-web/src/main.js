  import 'core-js/stable';
  import 'regenerator-runtime/runtime';
import Vue from 'vue'
import App from './App.vue'
import router from './router' 
import store from './store'
import { BootstrapVue} from 'bootstrap-vue'
import '@/style/app.scss'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";
import i18n from '@/i18n'

import '@/permission' 
import '@/icons' // icon

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(BootstrapVue)
Vue.component('font-awesome-icon', FontAwesomeIcon)

const _vm = new Vue({
  router,
  store,
  i18n,
  render: h => h(App),
}).$mount('#app')

document._vm_instance = _vm