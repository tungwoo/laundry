// pages/clothes/add.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    mode: 'smart', // 当前模式：smart-智能识别，manual-手动录入
    tempImagePath: '', // 临时图片路径
    recognitionResult: null, // 识别结果
    // 手动录入表单数据
    clothesTypes: ['外套', '衬衫', '裤子', '裙子', 'T恤', '毛衣', '其他'],
    typeIndex: null,
    brand: '',
    colors: ['黑色', '白色', '红色', '蓝色', '绿色', '黄色', '灰色', '其他'],
    colorIndex: null,
    materials: ['棉', '麻', '丝', '毛', '化纤', '混纺', '其他'],
    materialIndex: null,
    price: '',
    specialRequirements: [
      { value: 'urgent', name: '加急', checked: false },
      { value: 'noFade', name: '防褪色', checked: false },
      { value: 'special', name: '特殊处理', checked: false }
    ],
    remark: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // 页面加载时的初始化逻辑
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

  // 切换模式
  switchMode(e) {
    const mode = e.currentTarget.dataset.mode;
    this.setData({ mode });
  },

  // 选择图片
  chooseImage() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        this.setData({
          tempImagePath: res.tempFilePaths[0]
        });
      }
    });
  },

  // 预览图片
  previewImage() {
    wx.previewImage({
      urls: [this.data.tempImagePath]
    });
  },

  // 开始识别
  startRecognition() {
    wx.showLoading({
      title: '识别中...',
    });

    // 模拟识别过程
    setTimeout(() => {
      // 这里应该调用实际的识别API
      const mockResult = {
        type: '外套',
        brand: '示例品牌',
        color: '黑色',
        suggestedPrice: '88.00'
      };

      this.setData({
        recognitionResult: mockResult
      });

      wx.hideLoading();
    }, 1500);
  },

  // 编辑识别结果
  editRecognitionResult() {
    // 切换到手动模式并填充识别结果
    this.setData({
      mode: 'manual',
      typeIndex: this.data.clothesTypes.indexOf(this.data.recognitionResult.type),
      brand: this.data.recognitionResult.brand,
      colorIndex: this.data.colors.indexOf(this.data.recognitionResult.color),
      price: this.data.recognitionResult.suggestedPrice
    });
  },

  // 确认识别结果
  confirmRecognitionResult() {
    // 这里应该调用API保存识别结果
    wx.showToast({
      title: '添加成功',
      icon: 'success',
      duration: 2000,
      success: () => {
        setTimeout(() => {
          wx.navigateBack();
        }, 2000);
      }
    });
  },

  // 手动录入表单相关方法
  onTypeChange(e) {
    this.setData({
      typeIndex: e.detail.value
    });
  },

  onBrandInput(e) {
    this.setData({
      brand: e.detail.value
    });
  },

  onColorChange(e) {
    this.setData({
      colorIndex: e.detail.value
    });
  },

  onMaterialChange(e) {
    this.setData({
      materialIndex: e.detail.value
    });
  },

  onPriceInput(e) {
    this.setData({
      price: e.detail.value
    });
  },

  onSpecialRequirementsChange(e) {
    const values = e.detail.value;
    const specialRequirements = this.data.specialRequirements.map(item => ({
      ...item,
      checked: values.includes(item.value)
    }));
    this.setData({ specialRequirements });
  },

  onRemarkInput(e) {
    this.setData({
      remark: e.detail.value
    });
  },

  // 提交手动录入表单
  submitManualForm() {
    // 表单验证
    if (this.data.typeIndex === null) {
      wx.showToast({
        title: '请选择衣物类型',
        icon: 'none'
      });
      return;
    }

    if (!this.data.brand) {
      wx.showToast({
        title: '请输入品牌',
        icon: 'none'
      });
      return;
    }

    if (this.data.colorIndex === null) {
      wx.showToast({
        title: '请选择颜色',
        icon: 'none'
      });
      return;
    }

    if (this.data.materialIndex === null) {
      wx.showToast({
        title: '请选择材质',
        icon: 'none'
      });
      return;
    }

    if (!this.data.price) {
      wx.showToast({
        title: '请输入价格',
        icon: 'none'
      });
      return;
    }

    // 构建提交数据
    const formData = {
      type: this.data.clothesTypes[this.data.typeIndex],
      brand: this.data.brand,
      color: this.data.colors[this.data.colorIndex],
      material: this.data.materials[this.data.materialIndex],
      price: this.data.price,
      specialRequirements: this.data.specialRequirements
        .filter(item => item.checked)
        .map(item => item.value),
      remark: this.data.remark
    };

    // 这里应该调用API保存表单数据
    wx.showLoading({
      title: '保存中...',
    });

    // 模拟API调用
    setTimeout(() => {
      wx.hideLoading();
      wx.showToast({
        title: '添加成功',
        icon: 'success',
        duration: 2000,
        success: () => {
          setTimeout(() => {
            wx.navigateBack();
          }, 2000);
        }
      });
    }, 1000);
  }
})