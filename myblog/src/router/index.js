import Vue from 'vue';
import VueRouter from 'vue-router';
import Index from 'views/Index';
import Register from 'views/Register';
import Login from 'views/Login';
import BlogEdit from 'views/Blog/BlogEdit';
import BlogDetail from "views/Blog/BlogDetail";
import ListDetail from "../views/Blog/ListDetail";
import ProjectDetail from "../views/Project/ProjectDetail"
import StartProject from '../views/Project/StartProject'
import ProjectList from '../views/Project/ProjectList'
import DisplayList from '../views/DisplayList'
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
      path: '/display/:querystr',
      name: 'DisplayList',
      component: DisplayList
    },
    {
      path: '/home',
      name: 'Index',
      component: Index,
      children: [{
          name: 'ListDetail',
          path: 'listDetail/:columnId',
          components: {listDetail: ListDetail}
        },
        {
          name: 'ProjectList',
          path: 'projectList/:userId/:isFavorite',
          components: {projectList: ProjectList}
        }
      ]
    },
    {
      path: '/startproject',
      name: 'StartProject',
      component: StartProject
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
    {
      path: '/projectdetail/:projectId',
      name: 'ProjectDetail',
      component: ProjectDetail
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
