import Vue from 'vue'
import Vuex from 'vuex'
import user from './user'
import settings from './settings'
import app from './app'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    user,
    app,
    settings
  },
  getters
})

export default store
