import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '../views/DashboardView.vue'
import EngineerReviewView from '../views/EngineerReviewView.vue'
import ResourceView from '../views/ResourceView.vue'
import OrderDispatchView from '../views/OrderDispatchView.vue'
import LoginView from '../views/LoginView.vue'
import UserManageView from '../views/UserManageView.vue'
import BannerManageView from '../views/BannerManageView.vue'
import { getToken } from '../api'

const routes = [
  {
    path: '/',
    name: 'dashboard',
    component: DashboardView
  },
  {
    path: '/engineers',
    name: 'engineers',
    component: EngineerReviewView
  },
  {
    path: '/resources',
    name: 'resources',
    component: ResourceView
  },
  {
    path: '/orders',
    name: 'orders',
    component: OrderDispatchView
  },
  {
    path: '/users',
    name: 'users',
    component: UserManageView
  },
  {
    path: '/banners',
    name: 'banners',
    component: BannerManageView
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  if (to.path === '/login') {
    return true
  }
  if (!getToken()) {
    return '/login'
  }
  return true
})

export default router
