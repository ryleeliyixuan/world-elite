import router from './router'
import store from './store'
import {getToken, setToken} from '@/utils/auth'
import {setPageTitle} from '@/utils/setting'

router.beforeEach(async (to, from, next) => {
    setPageTitle(to.meta.title);

    //从url中获取token
    if (to.query._token) {
        setToken(to.query._token);
        store.commit('user/SET_TOKEN', to.query._token)
    }

    const hasToken = getToken()
    if (hasToken && (store.getters.name === undefined)) {
        await store.dispatch('user/MYINFO')
    }
    next()
})
