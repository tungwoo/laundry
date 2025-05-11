// 获取应用实例
const app = getApp();

Page({
  data: {
    userInfo: null,
    hasUserInfo: false,
    canIUseGetUserProfile: false,
    summaryData: {
      todayOrderCount: 0,
      todayIncome: 0,
      pendingOrderCount: 0,
      todayNewMembers: 0
    },
    recentOrders: [],
    loading: true
  },
  
  onLoad: function() {
    if (wx.getUserProfile) {
      this.setData({
        canIUseGetUserProfile: true
      });
    }
    
    // 检查登录状态
    const userInfo = app.globalData.userInfo;
    if (userInfo) {
      this.setData({
        userInfo: userInfo,
        hasUserInfo: true
      });
      this.loadDashboardData();
    } else {
      // 跳转到登录页
      wx.navigateTo({
        url: '/pages/login/login'
      });
    }
  },
  
  onShow: function() {
    if (app.globalData.userInfo) {
      this.loadDashboardData();
    }
  },
  
  // 获取用户信息
  getUserProfile: function() {
    wx.getUserProfile({
      desc: '用于完善会员资料',
      success: (res) => {
        const userInfo = res.userInfo;
        app.globalData.userInfo = userInfo;
        this.setData({
          userInfo: userInfo,
          hasUserInfo: true
        });
      }
    });
  },
  
  // 加载首页数据
  loadDashboardData: function() {
    this.setData({
      loading: true
    });
    
    // 获取首页统计数据
    app.request({
      url: '/miniapp/dashboard/summary',
      method: 'GET',
      success: (res) => {
        this.setData({
          summaryData: res.data,
          loading: false
        });
      }
    });
    
    // 获取最近订单
    app.request({
      url: '/miniapp/dashboard/recent-orders',
      method: 'GET',
      success: (res) => {
        this.setData({
          recentOrders: res.data,
          loading: false
        });
      }
    });
  },
  
  // 跳转到订单列表
  goToOrders: function() {
    wx.switchTab({
      url: '/pages/order/order'
    });
  },
  
  // 跳转到订单详情
  goToOrderDetail: function(e) {
    const orderId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/order/detail?id=' + orderId
    });
  },
  
  // 跳转到会员列表
  goToMembers: function() {
    wx.switchTab({
      url: '/pages/member/member'
    });
  },
  
  // 跳转到录入订单页面
  goToCreateOrder: function() {
    wx.navigateTo({
      url: '/pages/order/create'
    });
  },
  
  // 下拉刷新
  onPullDownRefresh: function() {
    this.loadDashboardData();
    wx.stopPullDownRefresh();
  }
}); 