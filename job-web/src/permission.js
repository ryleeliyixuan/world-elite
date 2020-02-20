import router from './router'
import store from './store'
import { getToken } from '@/utils/auth'

const title = 'World Edlite'

function getPageTitle(pageTitle) {
    if (pageTitle) {
        return `${pageTitle} - ${title}`
    }
    return `${title}`
}

router.beforeEach(async (to, from, next) => {
    document.title = getPageTitle(to.meta.title)
    const hasToken = getToken()
    if (hasToken && (store.getters.name === undefined)) {
        await store.dispatch('user/MYINFO')
    }
    next()
})