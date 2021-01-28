import Vue from 'vue'
import Router from 'vue-router'
import ba from 'vue-ba';

Vue.use(ba, process.env.VUE_APP_SITE_ID);
Vue.use(Router)
const constantRoutes = [
    {
        path: '/',
        component: () => import('@/views/HomePage'),
        meta: { title: '寰球菁英-首页' }
    },
    {
        path: '/login',
        component: () => import('@/views/LoginPage'),
        meta: { title: '寰球菁英-登录' }
    },
    {
        path: '/register',
        component: () => import('@/views/RegisterPage'),
        meta: { title: '寰球菁英-注册' }
    },
    // {
    //     path: '/register-basic',
    //     component: () => import('@/views/Register/RegisterBasicPage'),
    //     meta: { title: '寰球菁英-注册' }
    // },


    {
        path: '/edit-resume',
        component: () => import('@/views/EditResumePage'),
        meta: { title: '寰球菁英-编辑简历' }
    },
    // {
    //   path: '/company/:id',
    //   name: 'company',
    //   component: () => import('@/views/CompanyHomePage'),
    //   meta: { title: '寰球菁英-公司页面' }
    // },
    {
        path: '/post-list/:id',
        name: 'post-list',
        component: () => import('@/views/PostListPage'),
        meta: { title: '寰球菁英-岗位' }
    },
    {
        path: '/job/:id',
        name: 'job',
        component: () => import('@/views/JobPage'),
        meta: { title: '寰球菁英-职位详情' }
    },
    {
        path: '/resume/:id',
        name: 'resume',
        component: () => import('@/views/ResumePage'),
        meta: { title: '寰球菁英-简历预览' }
    },
    {
        path: '/job-list',
        component: () => import('@/views/JobListPage'),
        meta: { title: '寰球菁英-职位列表' }
    },
    {
        path: '/apply-jobs',
        component: () => import('@/views/ApplyJobListPage'),
        meta: { title: '寰球菁英-我的投递' }
    },
    {
        path: '/favorites',
        component: () => import('@/views/FavoriteListPage'),
        meta: { title: '寰球菁英-我的收藏' }
    },
    {
        path: '/my-activities',
        component: () => import('@/views/ActivityMyListPage'),
        meta: { title: '寰球菁英-我的活动' }
    },
    {
        path: '/messages',
        component: () => import('@/views/MessageListPage'),
        meta: { title: '寰球菁英-消息通知' }
    },
    {
        path: '/activity-list',
        component: () => import('@/views/ActivityListPage'),
        meta: { title: '寰球菁英-活动列表' }
    },
    {
        path: '/activity/edit',
        component: () => import('@/views/ActivityEditPage'),
        meta: { title: '寰球菁英-活动发布' }
    },
    {
        path: '/activity/publish',
        component: () => import('@/views/ActivityPublishPage'),
        meta: { title: '寰球菁英-发布活动管理' }
    },
    {
        path: '/activity/apply',
        component: () => import('@/views/ActivityApplyPage'),
        meta: { title: '寰球菁英-发布报名管理' }
    },
    {
        path: '/activity/apply/table',
        component: () => import('@/views/ActivityApplyTablePage'),
        meta: { title: '寰球菁英-活动报名表' }
    },
    {
        path: '/activity/:id',
        component: () => import('@/views/ActivityDetailPage'),
        meta: { title: '寰球菁英-活动详情' }
    },
    {
        path: '/forget-pwd',
        component: () => import('@/views/ForgetPwdPage'),
        meta: { title: '寰球菁英-忘记密码' }
    },
    {
        path: '/modify-email',
        component: () => import('@/views/ModifyEmailPage'),
        meta: { title: '寰球菁英-修改邮箱' }
    },
    {
        path: '/bind-account',
        component: () => import('@/views/BindAccountPage'),
        meta: { title: '寰球菁英-绑定账号' }
    },
    {
        path: '/user-protocol',
        component: () => import('@/views/UserProtocolPage'),
        meta: { title: '寰球菁英-用户协议' }
    },
    {
        path: '/wiki-card',
        component: () => import('@/views/CompanyWikiCardPage'),
        meta: { title: '寰球菁英-百科' }
    },
    {
        path: '/wiki-list',
        component: () => import('@/views/CompanyWikiListPage'),
        meta: { title: '寰球菁英-百科' }
    },
    {
        path: '/regist-success',
        component: () => import('@/views/RegisterSuccessPage'),
        meta: { title: '寰球菁英-注册成功' }
    },
    {
        path: '/modify-pwd',
        component: () => import('@/views/ModifyPwdPage'),
        meta: { title: '寰球菁英-修改密码' }
    },
    {
        path: '/mi',
        component: () => import('@/views/MiPage'),
        meta: { title: '寰球菁英-小米内页' }
    },
    {
        path: "/company/:id",
        name: "company",
        component: () => import("@/views/wiki/CompanyWikiMainPage"),
        redirect: "/company/:id/intro",
        meta: { title: "寰球菁英-公司百科" },
        children: [
            {
                path: "intro",
                name: "wiki-intro",
                component: () => import("@/views/wiki/WikiIntroPage"),
            },
            {
                path: "activity/:name",
                name: "wiki-activity",
                component: () => import("@/views/wiki/WikiActivityPage"),
            },
            {
                path: "job",
                name: "wiki-job",
                component: () => import("@/views/PostListPage"),
            },
            {
                path: "post",
                name: "wiki-community-post",
                component: () => import("@/views/wiki/WikiCommunityPostPage"),
            },
            {
                path: "postdetail",
                name: "wiki-community-post-detail",
                component: () => import("@/views/wiki/WikiCommunityPostDetailPage"),
            },
            {
                path: "score",
                name: "wiki-community-score",
                component: () => import("@/views/wiki/WikiCommunityScorePage"),
            },
        ],
    },
    {
        path: '/chat',
        component: () => import('@/views/ChatBoxPage'),
        meta: { title: '寰球菁英-聊一聊' }
    },
    {
        path: '/webRTC/:id/:interviewerId/:identity',
        component: () => import('@/views/WebRTC'),
        meta: { title: '寰球菁英-模拟面试' }
    },
    {
        path: '/mock/interview',
        component: () => import('@/views/MockHomePage'),
        meta: { title: '寰球菁英-模拟面试' }
    },
    {
        path: '/mock/interviewer/more',
        component: () => import('@/views/MoreInterviewer'),
        meta: { title: '寰球菁英-更多面试官' }
    },
    {
        path: '/mock/interviewer/:id',
        component: () => import('@/views/MockDetailPage'),
        meta: { title: '寰球菁英-面试官详情' }
    },
    {
        path: '/mock/promise/:id',
        component: () => import('@/views/MockPromisePage'),
        meta: { title: '寰球菁英-预约' }
    },
    {
        path: '/mock-mine',
        component: () => import('@/views/MockMinePage'),
        meta: { title: '寰球菁英-我的模拟面试' }
    }, {
        path: '/interviewSecretPage',
        component: () => import('@/views/InterviewSecretPage'),
        meta: { title: '寰球菁英-用户隐私协议' }
    },
    {
        path: '/registerInterviewerPage',
        component: () => import('@/views/RegisterInterviewerPage'),
        meta: { title: '寰球菁英-面试官注册' }
    },
    {
        path: '/registerInterviewerSuccess',
        component: () => import('@/views/RegisterInterviewerSuccess'),
        meta: { title: '寰球菁英-注册成功' }
    },
    {
        path: '/registerOther',
        component: () => import('@/views/RegisterOther'),
        meta: { title: '寰球菁英-其他方式注册' }
    },
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
