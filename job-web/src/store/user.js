import {getToken, setToken, removeToken} from '@/utils/auth'
import {register, login, logout, getMyInfo} from '@/api/user_api'
import Toast from '@/utils/toast'
import {storage} from "@/utils/storage";

const state = {
    token: getToken(),
    name: storage.getUsername(),
    avatar: storage.getAvatar()
}

const mutations = {
    SET_TOKEN: (state, token) => {
        state.token = token
    },
    SET_NAME: (state, name) => {
        state.name = name;
    },
    SET_AVATAR: (state, avatar) => {
        state.avatar = avatar;
    },
    SET_USERID: (state, userId) => {
        state.userId = userId;
    }
}

const actions = {
    LOGIN: ({commit}, loginForm) => {
        return new Promise((resolve, reject) => {
            login(loginForm).then(response => {
                const {data} = response

                if (data.type != 1) {
                    Toast.error('该账号不允许登录用户端');
                    return;
                }

                commit('SET_TOKEN', data.token)
                commit('SET_NAME', data.name)
                commit('SET_AVATAR', data.avatar)
                commit('SET_USERID', data.userId)

                setToken(data.token, loginForm.rememberFlag)
                storage.setUserInfo(data);
                storage.setLoginInfo(loginForm);
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },
    REGISTER: ({commit}, registerForm) => {
        return new Promise((resolve, reject) => {
            register(registerForm).then(response => {
                const {data} = response

                commit('SET_TOKEN', data.token)
                commit('SET_NAME', data.name)
                commit('SET_AVATAR', data.avatar)
                commit('SET_USERID', data.userId)
                
                setToken(data.token)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },
    MYINFO: ({commit}) => {
        return new Promise((resolve, reject) => {
            getMyInfo().then(response => {
                const {data} = response
                commit('SET_NAME', data.name)
                commit('SET_AVATAR', data.avatar)
                commit('SET_USERID', data.userId)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    },
    LOGOUT: ({commit}) => {
        return new Promise((resolve, reject) => {
            logout().then(() => {

                commit('SET_TOKEN', undefined)
                commit('SET_NAME', undefined)
                commit('SET_AVATAR', undefined)
                commit('SET_USERID', undefined)

                removeToken()
                storage.removeUserInfo();
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
