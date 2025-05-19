// pages/order/detail.js
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    orderId: null,
    order: null,
    loading: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    if (options.id) {
      this.setData({
        orderId: options.id
      });
      this.loadOrderDetail();
    } else {
      wx.showToast({
        title: '订单ID不能为空',
        icon: 'none'
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  // 加载订单详情
  loadOrderDetail() {
    this.setData({ loading: true });
    
    app.request({
      url: '/miniapp/order/detail',
      method: 'GET',
      data: {
        id: this.data.orderId
      },
      success: (res) => {
        const order = res.data;
        // 处理订单状态文本
        order.statusText = this.getStatusText(order.status);
        order.statusDesc = this.getStatusDesc(order.status);
        order.paymentStatusText = this.getPaymentStatusText(order.paymentStatus);
        
        this.setData({
          order,
          loading: false
        });
      },
      fail: () => {
        wx.showToast({
          title: '加载订单详情失败',
          icon: 'none'
        });
        this.setData({ loading: false });
      }
    });
  },

  // 获取订单状态文本
  getStatusText(status) {
    const statusMap = {
      'pending': '待处理',
      'processing': '处理中',
      'ready': '待取件',
      'completed': '已完成',
      'cancelled': '已取消'
    };
    return statusMap[status] || status;
  },

  // 获取订单状态描述
  getStatusDesc(status) {
    const descMap = {
      'pending': '订单已创建，等待处理',
      'processing': '衣物正在清洗中',
      'ready': '衣物已清洗完成，等待取件',
      'completed': '订单已完成',
      'cancelled': '订单已取消'
    };
    return descMap[status] || '';
  },

  // 获取支付状态文本
  getPaymentStatusText(status) {
    const statusMap = {
      'unpaid': '未支付',
      'paid': '已支付',
      'refunded': '已退款'
    };
    return statusMap[status] || status;
  },

  // 更新订单状态
  updateStatus(e) {
    const status = e.currentTarget.dataset.status;
    const statusText = this.getStatusText(status);
    
    wx.showModal({
      title: '确认操作',
      content: `确定要将订单状态更新为"${statusText}"吗？`,
      success: (res) => {
        if (res.confirm) {
          this.doUpdateStatus(status);
        }
      }
    });
  },

  // 执行状态更新
  doUpdateStatus(status) {
    wx.showLoading({
      title: '更新中...',
    });

    app.request({
      url: '/miniapp/order/update-status',
      method: 'POST',
      data: {
        id: this.data.orderId,
        status: status
      },
      success: () => {
        wx.hideLoading();
        wx.showToast({
          title: '更新成功',
          icon: 'success'
        });
        // 重新加载订单详情
        this.loadOrderDetail();
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '更新失败',
          icon: 'none'
        });
      }
    });
  },

  // 打印订单
  printOrder() {
    wx.showLoading({
      title: '打印中...',
    });

    app.request({
      url: '/miniapp/order/print',
      method: 'POST',
      data: {
        id: this.data.orderId
      },
      success: () => {
        wx.hideLoading();
        wx.showToast({
          title: '打印成功',
          icon: 'success'
        });
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '打印失败',
          icon: 'none'
        });
      }
    });
  },

  // 返回列表
  goBack() {
    wx.navigateBack();
  }
})