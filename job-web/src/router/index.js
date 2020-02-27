import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const constantRoutes = [
    {
        path: '/',
        component: () => import('@/views/HomePage'),
        meta: { title: '首页' }
    },
    {
        path: '/login',
        component: () => import('@/views/LoginPage'),
        meta: { title: '登录' }
    },
    {
        path: '/register',
        component: () => import('@/views/RegisterPage'),
        meta: { title: '注册' }
    },
    {
        path: '/edit-resume',
        component: () => import('@/views/EditResumePage'),
        meta: { title: '编辑简历' }
    },
    {
        path: '/company/:id',
        name: 'company',
        component: () => import('@/views/CompanyHomePage'),
        meta: { title: '公司主页' }
    },
    {
        path: '/job/:id',
        name: 'job',
        component: () => import('@/views/JobPage'),
        meta: { title: '职位详情' }
    },
    {
        path: '/job-list',
        component: () => import('@/views/JobListPage'),
        meta: { title: '职位列表' }
    },
    {
        path: '/apply-jobs',
        component: () => import('@/views/ApplyJobListPage'),
        meta: { title: '我的投递' }
    },
    {
        path: '/favorites',
        component: () => import('@/views/FavoriteListPage'),
        meta: { title: '我的收藏' }
    },
    {
        path: '/messages',
        component: () => import('@/views/MessageListPage'),
        meta: { title: '消息通知' }
    }
]

const createRouter = () => new Router({
    mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRoutes
})

const router = createRouter()

export default router