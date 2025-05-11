import request from '@/utils/request';

/**
 * 用户登录
 * @param {Object} data - 登录信息 {username, password}
 * @returns {Promise}
 */
export function login(data) {
  return request({
    url: '/admin/auth/login',
    method: 'post',
    data
  });
}

/**
 * 用户退出登录
 * @returns {Promise}
 */
export function logout() {
  return request({
    url: '/admin/auth/logout',
    method: 'post'
  });
}

/**
 * 获取用户信息
 * @returns {Promise}
 */
export function getUserInfo() {
  return request({
    url: '/admin/user/info',
    method: 'get'
  });
}

/**
 * 修改密码
 * @param {Object} data - 密码数据 {oldPassword, newPassword}
 * @returns {Promise}
 */
export function changePassword(data) {
  return request({
    url: '/admin/user/changePassword',
    method: 'post',
    data
  });
}

/**
 * 更新用户个人信息
 * @param {Object} data - 用户信息
 * @returns {Promise}
 */
export function updateUserInfo(data) {
  return request({
    url: '/admin/user/updateInfo',
    method: 'put',
    data
  });
}

/**
 * 获取用户列表
 * @param {Object} query - 查询参数
 * @returns {Promise}
 */
export function getUserList(query) {
  return request({
    url: '/admin/user/list',
    method: 'get',
    params: query
  });
}

/**
 * 添加用户
 * @param {Object} data - 用户数据
 * @returns {Promise}
 */
export function addUser(data) {
  return request({
    url: '/admin/user/add',
    method: 'post',
    data
  });
}

/**
 * 更新用户
 * @param {Object} data - 用户数据
 * @returns {Promise}
 */
export function updateUser(data) {
  return request({
    url: '/admin/user/update',
    method: 'put',
    data
  });
}

/**
 * 删除用户
 * @param {number} id - 用户ID
 * @returns {Promise}
 */
export function deleteUser(id) {
  return request({
    url: `/admin/user/delete/${id}`,
    method: 'delete'
  });
}

/**
 * 重置用户密码
 * @param {number} id - 用户ID
 * @returns {Promise}
 */
export function resetUserPassword(id) {
  return request({
    url: `/admin/user/resetPassword/${id}`,
    method: 'post'
  });
} 