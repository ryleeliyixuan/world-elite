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
        path: '/verify',
        component: () => import('@/views/VerifyCompanyPage'),
        meta: { title: '验证身份' }
    },
    {
        path: '/verify-status',
        component: () => import('@/views/VerifyStatusPage'),
        meta: { title: '审核结果' }
    },
    {
        path: '/manage-job',
        component: () => import('@/views/JobManagePage'),
        meta: { title: '职位管理' }
    },
    {
        path: '/edit-job',
        component: () => import('@/views/EditJobPage'),
        meta: { title: '编辑职位' }
    },
    {
        path: '/edit-company',
        component: () => import('@/views/EditCompanyPage'),
        meta: { title: '编辑公司信息' }
    },
    {
        path: '/manage-resume',
        component: () => import('@/views/ResumeManagePage'),
        meta: { title: '简历管理' }
    },
    {
        path: '/resume',
        component: () => import('@/views/ResumePage'),
        meta: { title: '简历' }
    },
    {
        path: '/forget-pwd',
        component: () => import('@/views/ForgetPwdPage'),
        meta: {title: '忘记密码'}
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