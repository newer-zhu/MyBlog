import Vue from 'vue'
import App from './App.vue'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import VueParticles from 'vue-particles'
import router from './router'
import axios from './axios'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import './plugins/hljs'
import dataV from '@jiaminghi/data-view'
import global from "./Js/global";
import scroll from 'vue-infinite-auto-scroll'
Vue.use(ElementUI);
Vue.use(mavonEditor);
Vue.use(VueParticles);
Vue.use(dataV);
Vue.use(scroll);
Vue.prototype.$axios = axios;
Vue.prototype.global = global;
Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
