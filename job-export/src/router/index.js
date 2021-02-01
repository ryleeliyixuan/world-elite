import Vue from 'vue'
import Router from 'vue-router'
import ba from 'vue-ba';

Vue.use(ba, process.env.VUE_APP_SITE_ID);
Vue.use(Router)
const constantRoutes = [
    {
        path: '/resume/:id',
        name: 'resume',
        component: () => import('@/views/ResumePage'),
        meta: { title: '寰球菁英-简历预览' }
    },
    {
        path: '/activity/apply/:id',
        component: () => import('@/views/ActivityApplyPage'),
        meta: { title: '寰球菁英-活动报名表预览' }
    }
]

const createRouter = () => new Router({
    mode: 'history', // require service support
    routes: constantRoutes
})

const router = createRouter()

export default router

router.beforeEach((to, from, next) => {
    if (to.path) {
        ba.trackPageview(to.fullPath);
    }
    next();
});
