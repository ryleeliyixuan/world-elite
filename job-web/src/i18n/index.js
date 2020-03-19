import Vue from 'vue'
import VueI18n from 'vue-i18n'

import zh from './config/zh';

Vue.use(VueI18n);

const i18n = new VueI18n({
    locale: 'zh-CN',
    messages: {
        'zh-CN': zh
    }
})

export default i18n;