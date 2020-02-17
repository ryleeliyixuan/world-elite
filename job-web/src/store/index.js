import Vue from 'vue'
import Vuex from 'vuex'
import user from './user'
import setting from './setting'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
    modules: {
        user,
        setting
    },
    getters
})

export default store
