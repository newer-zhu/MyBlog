import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    //localStorage是永久的， SessionStorage随着窗口消灭消失
    token: localStorage.getItem("token"),
    userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
    // ws: ''
  },
  mutations: {
    SET_TOKEN: (state,token) => {
      state.token = token;
      localStorage.setItem("token",token)
    },
    SET_USERINFO: (state,userInfo) => {
      state.userInfo = userInfo;
      sessionStorage.setItem("userInfo",JSON.stringify(userInfo))
    },
    REMOVE_INFO: (state) => {
      state.token = ''
      state.userInfo = ''
      localStorage.setItem("token",'')
      sessionStorage.setItem("userInfo",'')
    },
    // SET_WS: (state, ws) => {
    //   state.ws = ws;
    // },
    // REMOVE_WS: (state) => {
    //   state.ws = ''
    // }
  },
  getters:{
    getUser: state => {
      return state.userInfo
    },
    // getWs: state => {
    //   return state.ws
    // }
  },
  actions: {
  },
  modules: {
  }
})
