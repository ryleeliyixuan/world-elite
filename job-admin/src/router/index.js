import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '管理面板', icon: 'dashboard' }
    }]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/company',
    meta: { title: '用户管理', icon: 'user' },
    children: [{
      path: 'company',
      component: () => import('@/views/user/CompanyUserList'),
      meta: { title: '企业用户' }
    },
    {
      path: 'general',
      component: () => import('@/views/user/index'),
      meta: { title: '普通用户' }
    }]
  },
  {
    path: '/company',
    component: Layout,
    redirect: '/company/list',
    meta: { title: '企业管理', icon: 'company' },
    children: [{
      path: 'list',
      component: () => import('@/views/company/index'),
      meta: { title: '企业列表' }
    },
    {
      path: 'edit',
      hidden: true,
      component: () => import('@/views/company/EditCompanyPage'),
      meta: { title: '编辑企业' }
    },
    {
      path: 'edit-wiki',
      hidden: true,
      component: () => import('@/views/company/EditCompanyWiki'),
      meta: { title: '企业百科' }
    }]
  },
  {
    path: '/resume',
    component: Layout,
    redirect: '/resume/list',
    children: [{
      path: 'list',
      component: () => import('@/views/resume/index'),
      meta: { title: '简历管理', icon: 'resume' }
    }]
  },
  {
    path: '/job',
    component: Layout,
    redirect: '/job/list',
    children: [{
      path: 'list',
      component: () => import('@/views/job/index'),
      meta: { title: '职位管理', icon: 'job' }
    }]
  },
  {
    path: '/activity',
    component: Layout,
    redirect: '/activity/list',
    meta: { title: '活动管理', icon: 'activity' },
    children: [{
      path: 'list',
      component: () => import('@/views/activity/index'),
      meta: { title: '活动列表' }
    },
    {
      path: 'edit',
      component: () => import('@/views/activity/EditActivityPage'),
      meta: { title: '活动编辑' },
      hidden: true
    }]
  },
  {
    path: '/dict',
    component: Layout,
    redirect: '/dict/index',
    children: [
      {
        path: 'index',
        meta: { title: '字典管理', icon: 'dict' },
        component: () => import('@/views/dict/index')
      }
    ]
  },
  {
    path: '/school',
    component: Layout,
    redirect: '/school/index',
    children: [
      {
        path: 'index',
        meta: { title: '学校管理', icon: 'dict' },
        component: () => import('@/views/school/index')
      }
    ]
  },
  {
    path: '/download',
    component: Layout,
    redirect: '/download/index',
    children: [
      {
        path: 'index',
        meta: { title: '下载管理', icon: 'download' },
        component: () => import('@/views/download/index')
      }
    ]
  },
  {
    path: '/config',
    component: Layout,
    meta: { title: '首页设置', icon: 'setting' },
    children: [
      {
        path: 'index',
        meta: { title: '文案配置' },
        component: () => import('@/views/config/ConfigIndexPage')
      },
      {
        path: 'recommend-job',
        meta: { title: '推荐职位' },
        component: () => import('@/views/config/RecommendJobPage')
      },
      {
        path: 'recommend-company',
        meta: { title: '推荐企业' },
        component: () => import('@/views/config/RecommendCompanyPage')
      }
    ]
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  }
]

const createRouter = () => new Router({
  mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
