import router from './router'
import {setToken} from '@/utils/auth'
import {setPageTitle} from '@/utils/setting'

router.beforeEach(async (to, from, next) => {
    setPageTitle(to.meta.title);
    //从url中获取token
    if (to.query._token) {
        setToken(to.query._token);
    }
    next()
})
