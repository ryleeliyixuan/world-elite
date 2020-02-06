import { getToken, setToken, removeToken } from '@/utils/auth'
import { register, login, logout, getMyInfo } from '@/api/user_api'

const state = {
    token: getToken(),
    loginUser: undefined
}

const mutations = {
    SET_TOKEN: (state, token) => {
        state.token = token
    },
    SET_USER: (state, user) => {
        state.loginUser = user;
    }
}

const actions = {
    LOGIN: ({ commit }, loginForm) => {
        return new Promise((resolve, reject) => {
            login(loginForm).then(response => {
                const { data } = response

                commit('SET_TOKEN', data.token)
                commit('SET_USER', data)

                setToken(data.token)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },
    REGISTER: ({ commit }, registerForm) => {
        return new Promise((resolve, reject) => {
            register(registerForm).then(response => {
                const { data } = response

                commit('SET_TOKEN', data.token)
                commit('SET_USER', data)

                setToken(data.token)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },
    MYINFO: ({ commit }) => {
        return new Promise((resolve, reject) => {
            getMyInfo().then(response => {
                const { data } = response
                commit('SET_USER', data)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },
    LOGOUT: ({ commit }) => {
        return new Promise((resolve, reject) => {
            logout().then(() => {

                commit('SET_TOKEN', '')
                commit('SET_USER', null)

                removeToken()
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}
