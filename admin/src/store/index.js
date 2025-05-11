import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}')
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    LOGOUT(state) {
      state.token = ''
      state.userInfo = {}
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    }
  },
  actions: {
    login({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        // 这里实际项目中应该进行登录API调用
        // 这里仅做模拟
        setTimeout(() => {
          const token = 'admin-token'
          commit('SET_TOKEN', token)
          commit('SET_USER_INFO', { name: '管理员', role: 'admin' })
          resolve()
        }, 1000)
      })
    },
    logout({ commit }) {
      commit('LOGOUT')
    }
  },
  getters: {
    token: state => state.token,
    userInfo: state => state.userInfo,
    isLogin: state => !!state.token
  }
}) 