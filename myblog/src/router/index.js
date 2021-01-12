import Vue from 'vue';
import VueRouter from 'vue-router';
import Index from 'views/Index';
import Register from 'views/Register';
import Login from 'views/Login';
import BlogEdit from 'views/BlogEdit';
import BlogDetail from "views/BlogDetail";

Vue.use(VueRouter)

  const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
    {
      path: '/home',
      name: 'Index',
      component: Index
    },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
    {
      path: '/blogdetail/:blogId',
      name: 'BlogDetail',
      component: BlogDetail
    },
    {
      path: '/blogedit',
      name: 'BlogEdit',
      component: BlogEdit
    },
    {
      path: '/blogedit/:blogId',
      name: 'BlogEdit',
      component: BlogEdit
    },

  // {
    // path: '/about',
    // name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
  // }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
