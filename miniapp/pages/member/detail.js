// pages/member/detail.js
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    memberId: null,
    member: null,
    loading: true,
    // 充值相关
    showRechargeModal: false,
    rechargeAmount: '',
    paymentMethods: ['现金', '微信支付', '支付宝'],
    paymentMethodIndex: 0,
    // 消费相关
    showConsumptionModal: false,
    consumptionAmount: '',
    consumptionRemark: '',
    // 记录列表
    consumptionRecords: [],
    rechargeRecords: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    if (options.id) {
      this.setData({
        memberId: options.id
      });
      this.loadMemberDetail();
      this.loadRecords();
    } else {
      wx.showToast({
        title: '会员ID不能为空',
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

  // 加载会员详情
  loadMemberDetail() {
    this.setData({ loading: true });
    
    app.request({
      url: '/miniapp/member/detail',
      method: 'GET',
      data: {
        id: this.data.memberId
      },
      success: (res) => {
        this.setData({
          member: res.data,
          loading: false
        });
      },
      fail: () => {
        wx.showToast({
          title: '加载会员详情失败',
          icon: 'none'
        });
        this.setData({ loading: false });
      }
    });
  },

  // 加载记录
  loadRecords() {
    // 加载消费记录
    app.request({
      url: '/miniapp/member/consumption-records',
      method: 'GET',
      data: {
        memberId: this.data.memberId,
        page: 1,
        size: 5
      },
      success: (res) => {
        this.setData({
          consumptionRecords: res.data.list
        });
      }
    });

    // 加载充值记录
    app.request({
      url: '/miniapp/member/recharge-records',
      method: 'GET',
      data: {
        memberId: this.data.memberId,
        page: 1,
        size: 5
      },
      success: (res) => {
        this.setData({
          rechargeRecords: res.data.list
        });
      }
    });
  },

  // 显示充值弹窗
  showRechargeModal() {
    this.setData({
      showRechargeModal: true,
      rechargeAmount: ''
    });
  },

  // 隐藏充值弹窗
  hideRechargeModal() {
    this.setData({
      showRechargeModal: false
    });
  },

  // 充值金额输入
  onRechargeInput(e) {
    this.setData({
      rechargeAmount: e.detail.value
    });
  },

  // 支付方式选择
  onPaymentMethodChange(e) {
    this.setData({
      paymentMethodIndex: e.detail.value
    });
  },

  // 确认充值
  confirmRecharge() {
    const { rechargeAmount, paymentMethodIndex, paymentMethods } = this.data;
    
    if (!rechargeAmount) {
      wx.showToast({
        title: '请输入充值金额',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: '充值中...',
    });

    app.request({
      url: '/miniapp/member/recharge',
      method: 'POST',
      data: {
        memberId: this.data.memberId,
        amount: rechargeAmount,
        paymentMethod: paymentMethods[paymentMethodIndex]
      },
      success: () => {
        wx.hideLoading();
        wx.showToast({
          title: '充值成功',
          icon: 'success'
        });
        this.hideRechargeModal();
        this.loadMemberDetail();
        this.loadRecords();
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '充值失败',
          icon: 'none'
        });
      }
    });
  },

  // 显示消费弹窗
  showConsumptionModal() {
    this.setData({
      showConsumptionModal: true,
      consumptionAmount: '',
      consumptionRemark: ''
    });
  },

  // 隐藏消费弹窗
  hideConsumptionModal() {
    this.setData({
      showConsumptionModal: false
    });
  },

  // 消费金额输入
  onConsumptionInput(e) {
    this.setData({
      consumptionAmount: e.detail.value
    });
  },

  // 消费说明输入
  onConsumptionRemarkInput(e) {
    this.setData({
      consumptionRemark: e.detail.value
    });
  },

  // 确认消费
  confirmConsumption() {
    const { consumptionAmount, consumptionRemark } = this.data;
    
    if (!consumptionAmount) {
      wx.showToast({
        title: '请输入消费金额',
        icon: 'none'
      });
      return;
    }

    if (Number(consumptionAmount) > this.data.member.balance) {
      wx.showToast({
        title: '余额不足',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: '处理中...',
    });

    app.request({
      url: '/miniapp/member/consume',
      method: 'POST',
      data: {
        memberId: this.data.memberId,
        amount: consumptionAmount,
        remark: consumptionRemark
      },
      success: () => {
        wx.hideLoading();
        wx.showToast({
          title: '消费成功',
          icon: 'success'
        });
        this.hideConsumptionModal();
        this.loadMemberDetail();
        this.loadRecords();
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '消费失败',
          icon: 'none'
        });
      }
    });
  },

  // 编辑会员信息
  editMemberInfo() {
    wx.navigateTo({
      url: `/pages/member/edit?id=${this.data.memberId}`
    });
  },

  // 查看更多消费记录
  viewMoreConsumption() {
    wx.navigateTo({
      url: `/pages/member/consumption?id=${this.data.memberId}`
    });
  },

  // 查看更多充值记录
  viewMoreRecharge() {
    wx.navigateTo({
      url: `/pages/member/recharge?id=${this.data.memberId}`
    });
  }
})