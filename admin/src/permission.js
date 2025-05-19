import router from './router';
import store from './store';
import { Message } from 'ant-design-vue';
import NProgress from 'nprogress'; // 进度条
import 'nprogress/nprogress.css'; // 进度条样式
import { getToken } from '@/utils/auth'; // 获取token

NProgress.configure({ showSpinner: false }); // NProgress配置

const whiteList = ['/login']; // 不重定向白名单

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
  // 开始进度条
  NProgress.start();

  // 设置页面标题
  document.title = to.meta.title ? to.meta.title + ' - 洗衣店管理系统' : '洗衣店管理系统';

  // 获取用户token
  const hasToken = getToken();

  // 判断是否有token
  if (hasToken) {
    if (to.path === '/login') {
      // 如果已登录，重定向到首页
      next({ path: '/' });
      NProgress.done();
    } else {
      // 确定用户是否已通过getUserInfo获取到权限角色
      const hasRoles = store.getters.roles && store.getters.roles.length > 0;
      
      if (hasRoles) {
        next();
      } else {
        try {
          // 获取用户信息
          // 注意：roles必须是一个数组！ 如: ['admin'] 或 ['developer', 'editor']
          await store.dispatch('user/getUserInfo');

          // 重新加载页面
          next({ ...to, replace: true });
        } catch (error) {
          // 删除token并跳转到登录页面重新登录
          await store.dispatch('user/resetToken');
          Message.error(error || '验证失败，请重新登录');
          next(`/login?redirect=${to.path}`);
          NProgress.done();
        }
      }
    }
  } else {
    /* 没有token */
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单中，直接进入
      next();
    } else {
      // 没有访问权限的其他页面将重定向到登录页面
      next(`/login?redirect=${to.path}`);
      NProgress.done();
    }
  }
});

router.afterEach(() => {
  // 结束进度条
  NProgress.done();
}); 