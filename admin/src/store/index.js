import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    user
  },
  state: {
  },
  mutations: {
  },
  actions: {
  },
  getters: {
    token: state => state.user.token,
    userInfo: state => state.user.userInfo,
    roles: state => state.user.roles,
    isLogin: state => !!state.user.token
  }
}) 