import { login, logout, getUserInfo } from '@/api/user';
import { getToken, setToken, removeToken } from '@/utils/auth';

const state = {
  token: getToken(),
  userInfo: {},
  roles: []
};

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token;
  },
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo;
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles;
  }
};

const actions = {
  // 用户登录
  login({ commit }, userInfo) {
    const { username, password } = userInfo;
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password })
        .then(response => {
          const { data } = response;
          commit('SET_TOKEN', data.token);
          setToken(data.token);
          resolve();
        })
        .catch(error => {
          reject(error);
        });
    });
  },

  // 获取用户信息
  getUserInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getUserInfo()
        .then(response => {
          const { data } = response;

          if (!data) {
            reject(new Error('验证失败，请重新登录'));
          }

          const { roles } = data;

          // 验证返回的roles是否是一个非空数组
          if (!roles || roles.length <= 0) {
            reject(new Error('用户角色必须是非空数组!'));
          }

          commit('SET_ROLES', roles);
          commit('SET_USER_INFO', data);
          resolve(data);
        })
        .catch(error => {
          reject(error);
        });
    });
  },

  // 用户退出登录
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout()
        .then(() => {
          commit('SET_TOKEN', '');
          commit('SET_ROLES', []);
          removeToken();

          // 重置已访问的视图和缓存的视图
          // dispatch('tagsView/delAllViews', null, { root: true });

          resolve();
        })
        .catch(error => {
          reject(error);
        });
    });
  },

  // 前端登出
  fedLogout({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '');
      removeToken();
      resolve();
    });
  },

  // 重置token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '');
      commit('SET_ROLES', []);
      removeToken();
      resolve();
    });
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions
}; 