import axios from 'axios';
import { Modal, notification, message } from 'ant-design-vue';
import store from '@/store';
import { getToken } from '@/utils/auth';

// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || '/api', // API的基础URL
  timeout: 10000 // 请求超时时间
});

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    // 对请求错误做些什么
    console.error(error); // for debug
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  /**
   * 通过判断HTTP状态码和业务状态码来判断响应状态
   */
  response => {
    const res = response.data;

    // 如果响应的状态码不是200，说明有问题
    if (response.status !== 200) {
      message.error(res.message || '网络错误');
      return Promise.reject(new Error(res.message || '网络错误'));
    }

    // 如果业务状态码不是1，说明业务处理失败
    if (res.code !== 1) {
      message.error(res.msg || '操作失败');
      
      // 401: 未登录或token过期
      if (res.code === 401) {
        // 清除token并重定向到登录页
        localStorage.removeItem('token');
        window.location.href = '/login';
      }
      
      return Promise.reject(new Error(res.msg || '未知错误'));
    } else {
      return res;
    }
  },
  error => {
    console.error('请求错误:', error); // for debug
    message.error(error.message || '网络错误，请稍后重试');
    return Promise.reject(error);
  }
);

export default service; 