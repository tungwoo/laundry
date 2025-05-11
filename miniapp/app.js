App({
  globalData: {
    userInfo: null,
    baseURL: '/api',
    token: '',
    storeInfo: null,
    theme: {
      primaryColor: '#1890ff',
      secondaryColor: '#52c41a',
      backgroundColor: '#f5f5f5',
      textColor: '#333333'
    }
  },
  
  onLaunch: function() {
    // 展示本地存储能力
    const token = wx.getStorageSync('token') || '';
    const userInfo = wx.getStorageSync('userInfo') || null;
    
    this.globalData.token = token;
    this.globalData.userInfo = userInfo;
    
    // 获取系统信息
    const systemInfo = wx.getSystemInfoSync();
    this.globalData.systemInfo = systemInfo;
    
    // 登录状态检查
    if (token) {
      this.checkSession();
    }
  },
  
  checkSession: function() {
    // 检查登录状态是否过期
    wx.checkSession({
      fail: () => {
        // 登录态过期，重新登录
        this.globalData.token = '';
        this.globalData.userInfo = null;
        wx.removeStorageSync('token');
        wx.removeStorageSync('userInfo');
      }
    });
  },
  
  // 用户登录方法
  login: function(callback) {
    wx.login({
      success: res => {
        if (res.code) {
          // 发送 res.code 到后台换取 openId, sessionKey, unionId
          wx.request({
            url: this.globalData.baseURL + '/miniapp/login',
            method: 'POST',
            data: {
              code: res.code
            },
            success: response => {
              if (response.data.code === 1) {
                const token = response.data.data.token;
                const userInfo = response.data.data.userInfo;
                
                // 存储登录信息
                this.globalData.token = token;
                this.globalData.userInfo = userInfo;
                wx.setStorageSync('token', token);
                wx.setStorageSync('userInfo', userInfo);
                
                if (callback) {
                  callback(true);
                }
              } else {
                wx.showToast({
                  title: response.data.msg || '登录失败',
                  icon: 'none'
                });
                if (callback) {
                  callback(false);
                }
              }
            },
            fail: err => {
              wx.showToast({
                title: '网络请求失败',
                icon: 'none'
              });
              if (callback) {
                callback(false);
              }
            }
          });
        } else {
          wx.showToast({
            title: '获取用户登录状态失败',
            icon: 'none'
          });
          if (callback) {
            callback(false);
          }
        }
      }
    });
  },
  
  // 统一请求方法
  request: function(options) {
    const token = this.globalData.token;
    const header = options.header || {};
    
    if (token) {
      header.Authorization = 'Bearer ' + token;
    }
    
    wx.request({
      url: this.globalData.baseURL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: header,
      success: function(res) {
        if (res.data.code === 1) {
          if (options.success) {
            options.success(res.data);
          }
        } else {
          // 登录失效处理
          if (res.data.code === -1) {
            wx.removeStorageSync('token');
            wx.removeStorageSync('userInfo');
            
            wx.showModal({
              title: '提示',
              content: '登录已过期，请重新登录',
              showCancel: false,
              success: function() {
                wx.navigateTo({
                  url: '/pages/login/login'
                });
              }
            });
          } else {
            if (options.fail) {
              options.fail(res.data);
            } else {
              wx.showToast({
                title: res.data.msg || '请求失败',
                icon: 'none'
              });
            }
          }
        }
      },
      fail: function(err) {
        if (options.fail) {
          options.fail(err);
        } else {
          wx.showToast({
            title: '网络请求失败',
            icon: 'none'
          });
        }
      },
      complete: function(res) {
        if (options.complete) {
          options.complete(res);
        }
      }
    });
  }
}); 