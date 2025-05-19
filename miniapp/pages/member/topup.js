// pages/member/topup.js
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    memberId: '',
    member: null,
    // 充值金额选项
    amountOptions: [
      { value: 100, desc: '基础充值' },
      { value: 200, desc: '推荐充值' },
      { value: 500, desc: '优惠充值' },
      { value: 1000, desc: '大额充值' },
      { value: 2000, desc: '尊享充值' }
    ],
    selectedAmount: null,
    customAmount: '',
    // 支付方式
    paymentMethods: [
      { name: '现金', icon: '/images/payment/cash.png' },
      { name: '微信支付', icon: '/images/payment/wechat.png' },
      { name: '支付宝', icon: '/images/payment/alipay.png' }
    ],
    paymentMethodIndex: 0,
    remark: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    if (options.id) {
      this.setData({
        memberId: options.id
      });
      this.loadMemberInfo();
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

  // 加载会员信息
  loadMemberInfo() {
    wx.showLoading({
      title: '加载中...',
    });

    app.request({
      url: '/miniapp/member/detail',
      data: {
        id: this.data.memberId
      },
      success: (res) => {
        this.setData({
          member: res.data
        });
      },
      complete: () => {
        wx.hideLoading();
      }
    });
  },

  // 选择充值金额
  selectAmount(e) {
    const amount = e.currentTarget.dataset.amount;
    this.setData({
      selectedAmount: amount,
      customAmount: ''
    });
  },

  // 选择自定义金额
  selectCustomAmount() {
    this.setData({
      selectedAmount: null
    });
  },

  // 自定义金额输入
  onCustomAmountInput(e) {
    this.setData({
      customAmount: e.detail.value
    });
  },

  // 选择支付方式
  selectPaymentMethod(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({
      paymentMethodIndex: index
    });
  },

  // 备注输入
  onRemarkInput(e) {
    this.setData({
      remark: e.detail.value
    });
  },

  // 表单验证
  validateForm() {
    const { selectedAmount, customAmount, paymentMethodIndex } = this.data;
    const amount = selectedAmount || customAmount;

    if (!amount) {
      wx.showToast({
        title: '请选择或输入充值金额',
        icon: 'none'
      });
      return false;
    }

    if (isNaN(Number(amount)) || Number(amount) <= 0) {
      wx.showToast({
        title: '请输入正确的充值金额',
        icon: 'none'
      });
      return false;
    }

    return true;
  },

  // 提交充值
  submitRecharge() {
    if (!this.validateForm()) {
      return;
    }

    const { memberId, selectedAmount, customAmount, paymentMethodIndex, paymentMethods, remark } = this.data;
    const amount = selectedAmount || customAmount;

    wx.showLoading({
      title: '充值中...',
    });

    app.request({
      url: '/miniapp/member/recharge',
      method: 'POST',
      data: {
        memberId,
        amount: Number(amount),
        paymentMethod: paymentMethods[paymentMethodIndex].name,
        remark
      },
      success: (res) => {
        wx.hideLoading();
        wx.showToast({
          title: '充值成功',
          icon: 'success'
        });
        
        // 延迟返回上一页
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '充值失败',
          icon: 'none'
        });
      }
    });
  }
})