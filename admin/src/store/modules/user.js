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
      // 由于可能还没有后端API，这里先使用模拟数据
      if (process.env.NODE_ENV === 'development') {
        // 开发环境使用模拟数据
        setTimeout(() => {
          const token = 'admin-token-' + Date.now();
          commit('SET_TOKEN', token);
          setToken(token);
          // 设置默认用户信息
          commit('SET_USER_INFO', { name: username || '管理员', roles: ['admin'] });
          commit('SET_ROLES', ['admin']);
          resolve();
        }, 1000);
      } else {
        // 生产环境调用实际API
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
      }
    });
  },

  // 获取用户信息
  getUserInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      // 由于可能还没有后端API，这里先使用模拟数据
      if (process.env.NODE_ENV === 'development') {
        // 开发环境使用模拟数据
        setTimeout(() => {
          const data = {
            name: '管理员',
            avatar: '',
            roles: ['admin'],
            permissions: ['*']
          };
          
          const { roles } = data;

          // 验证返回的roles是否是一个非空数组
          if (!roles || roles.length <= 0) {
            reject(new Error('用户角色必须是非空数组!'));
          }

          commit('SET_ROLES', roles);
          commit('SET_USER_INFO', data);
          resolve(data);
        }, 300);
      } else {
        // 生产环境调用实际API
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
      }
    });
  },

  // 用户退出登录
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      if (process.env.NODE_ENV === 'development') {
        // 开发环境直接清除本地数据
        commit('SET_TOKEN', '');
        commit('SET_ROLES', []);
        commit('SET_USER_INFO', {});
        removeToken();
        resolve();
      } else {
        // 生产环境调用实际API
        logout()
          .then(() => {
            commit('SET_TOKEN', '');
            commit('SET_ROLES', []);
            commit('SET_USER_INFO', {});
            removeToken();
            resolve();
          })
          .catch(error => {
            reject(error);
          });
      }
    });
  },

  // 前端登出
  fedLogout({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '');
      commit('SET_USER_INFO', {});
      commit('SET_ROLES', []);
      removeToken();
      resolve();
    });
  },

  // 重置token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '');
      commit('SET_ROLES', []);
      commit('SET_USER_INFO', {});
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