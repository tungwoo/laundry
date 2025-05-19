// pages/clothes/edit.js
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: '',
    clothesTypes: ['上衣', '裤子', '裙子', '外套', '内衣', '其他'],
    typeIndex: -1,
    brand: '',
    color: '',
    material: '',
    price: '',
    washMethods: ['普通水洗', '手洗', '干洗', '不可水洗'],
    washMethodIndex: -1,
    ironingLevels: ['无需熨烫', '低温熨烫', '中温熨烫', '高温熨烫'],
    ironingLevelIndex: -1,
    specialTreatments: [
      { label: '去渍', value: 'stain_removal', checked: false },
      { label: '漂白', value: 'bleaching', checked: false },
      { label: '柔顺', value: 'softening', checked: false },
      { label: '除菌', value: 'sterilization', checked: false }
    ],
    remark: '',
    submitting: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    if (options.id) {
      this.setData({ id: options.id })
      this.loadClothesDetail()
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

  // 加载衣物详情
  async loadClothesDetail() {
    try {
      const res = await wx.cloud.callFunction({
        name: 'getClothesDetail',
        data: { id: this.data.id }
      })

      const clothes = res.result
      const typeIndex = this.data.clothesTypes.findIndex(type => type === clothes.type)
      const washMethodIndex = this.data.washMethods.findIndex(method => method === clothes.washMethod)
      const ironingLevelIndex = this.data.ironingLevels.findIndex(level => level === clothes.ironingLevel)

      // 处理特殊处理选项
      const specialTreatments = this.data.specialTreatments.map(item => ({
        ...item,
        checked: clothes.specialTreatments.includes(item.value)
      }))

      this.setData({
        typeIndex,
        brand: clothes.brand,
        color: clothes.color,
        material: clothes.material,
        price: clothes.price,
        washMethodIndex,
        ironingLevelIndex,
        specialTreatments,
        remark: clothes.remark
      })
    } catch (error) {
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      })
    }
  },

  // 选择衣物类型
  onTypeChange(e) {
    this.setData({
      typeIndex: parseInt(e.detail.value)
    })
  },

  // 选择洗涤方式
  onWashMethodChange(e) {
    this.setData({
      washMethodIndex: parseInt(e.detail.value)
    })
  },

  // 选择熨烫要求
  onIroningLevelChange(e) {
    this.setData({
      ironingLevelIndex: parseInt(e.detail.value)
    })
  },

  // 选择特殊处理
  onSpecialTreatmentChange(e) {
    const value = e.currentTarget.dataset.value
    const specialTreatments = this.data.specialTreatments.map(item => {
      if (item.value === value) {
        return { ...item, checked: !item.checked }
      }
      return item
    })
    this.setData({ specialTreatments })
  },

  // 表单验证
  validateForm() {
    if (this.data.typeIndex === -1) {
      wx.showToast({
        title: '请选择衣物类型',
        icon: 'none'
      })
      return false
    }

    if (!this.data.brand) {
      wx.showToast({
        title: '请输入品牌',
        icon: 'none'
      })
      return false
    }

    if (!this.data.color) {
      wx.showToast({
        title: '请输入颜色',
        icon: 'none'
      })
      return false
    }

    if (!this.data.material) {
      wx.showToast({
        title: '请输入材质',
        icon: 'none'
      })
      return false
    }

    if (!this.data.price) {
      wx.showToast({
        title: '请输入价格',
        icon: 'none'
      })
      return false
    }

    return true
  },

  // 提交表单
  async onSubmit(e) {
    if (!this.validateForm()) return

    this.setData({ submitting: true })
    try {
      const formData = e.detail.value
      const specialTreatments = this.data.specialTreatments
        .filter(item => item.checked)
        .map(item => item.value)

      const data = {
        type: this.data.clothesTypes[this.data.typeIndex],
        brand: formData.brand,
        color: formData.color,
        material: formData.material,
        price: parseFloat(formData.price),
        washMethod: this.data.washMethodIndex === -1 ? '' : this.data.washMethods[this.data.washMethodIndex],
        ironingLevel: this.data.ironingLevelIndex === -1 ? '' : this.data.ironingLevels[this.data.ironingLevelIndex],
        specialTreatments,
        remark: formData.remark
      }

      if (this.data.id) {
        await wx.cloud.callFunction({
          name: 'updateClothes',
          data: { id: this.data.id, ...data }
        })
      } else {
        await wx.cloud.callFunction({
          name: 'createClothes',
          data
        })
      }

      wx.showToast({
        title: '保存成功',
        icon: 'success'
      })

      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    } catch (error) {
      wx.showToast({
        title: '保存失败',
        icon: 'none'
      })
    } finally {
      this.setData({ submitting: false })
    }
  }
})