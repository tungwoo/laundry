const app = getApp();

Page({
  data: {
    // 表单数据
    name: '',
    phone: '',
    levelIndex: 0,
    initialAmount: '',
    paymentMethodIndex: 0,
    remark: '',
    // 会员等级列表
    memberLevels: [
      {
        id: 1,
        name: '普通会员',
        description: '享受基础会员服务，消费无折扣'
      },
      {
        id: 2,
        name: '银卡会员',
        description: '享受9折优惠，每月赠送积分'
      },
      {
        id: 3,
        name: '金卡会员',
        description: '享受8折优惠，每月赠送积分，生日当月双倍积分'
      }
    ],
    // 支付方式
    paymentMethods: ['现金', '微信支付', '支付宝']
  },

  // 姓名输入
  onNameInput(e) {
    this.setData({
      name: e.detail.value
    });
  },

  // 手机号输入
  onPhoneInput(e) {
    this.setData({
      phone: e.detail.value
    });
  },

  // 会员等级选择
  onLevelChange(e) {
    this.setData({
      levelIndex: e.detail.value
    });
  },

  // 初始充值金额输入
  onInitialAmountInput(e) {
    this.setData({
      initialAmount: e.detail.value
    });
  },

  // 支付方式选择
  onPaymentMethodChange(e) {
    this.setData({
      paymentMethodIndex: e.detail.value
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
    const { name, phone, levelIndex, initialAmount } = this.data;
    
    if (!name.trim()) {
      wx.showToast({
        title: '请输入会员姓名',
        icon: 'none'
      });
      return false;
    }

    if (!phone.trim()) {
      wx.showToast({
        title: '请输入手机号码',
        icon: 'none'
      });
      return false;
    }

    if (!/^1[3-9]\d{9}$/.test(phone)) {
      wx.showToast({
        title: '请输入正确的手机号码',
        icon: 'none'
      });
      return false;
    }

    if (levelIndex === '') {
      wx.showToast({
        title: '请选择会员等级',
        icon: 'none'
      });
      return false;
    }

    if (initialAmount && isNaN(Number(initialAmount))) {
      wx.showToast({
        title: '请输入正确的充值金额',
        icon: 'none'
      });
      return false;
    }

    return true;
  },

  // 提交表单
  submitForm() {
    if (!this.validateForm()) {
      return;
    }

    const { name, phone, levelIndex, memberLevels, initialAmount, paymentMethodIndex, paymentMethods, remark } = this.data;

    wx.showLoading({
      title: '创建中...',
    });

    app.request({
      url: '/miniapp/member/create',
      method: 'POST',
      data: {
        name,
        phone,
        levelId: memberLevels[levelIndex].id,
        initialAmount: initialAmount ? Number(initialAmount) : 0,
        paymentMethod: paymentMethods[paymentMethodIndex],
        remark
      },
      success: (res) => {
        wx.hideLoading();
        wx.showToast({
          title: '创建成功',
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
          title: '创建失败',
          icon: 'none'
        });
      }
    });
  }
}); 