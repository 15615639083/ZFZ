import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from '../views/DashboardView.vue'
import EngineerReviewView from '../views/EngineerReviewView.vue'
import ResourceView from '../views/ResourceView.vue'
import OrderDispatchView from '../views/OrderDispatchView.vue'
import LoginView from '../views/LoginView.vue'

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
    path: '/login',
    name: 'login',
    component: LoginView
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
