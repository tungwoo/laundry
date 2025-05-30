// pages/clothes/clothes.js
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    loading: false,
    searchValue: '',
    clothesTypes: [
      { name: '上衣', value: 'shirt' },
      { name: '裤子', value: 'pants' },
      { name: '裙子', value: 'skirt' },
      { name: '外套', value: 'coat' },
      { name: '内衣', value: 'underwear' },
      { name: '其他', value: 'other' }
    ],
    currentType: 'all',
    clothesList: [],
    pageNum: 1,
    pageSize: 10,
    hasMore: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.loadClothesList()
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
    this.setData({
      pageNum: 1,
      hasMore: true,
      clothesList: []
    })
    this.loadClothesList().then(() => {
      wx.stopPullDownRefresh()
    })
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.loadClothesList()
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  // 加载衣物列表
  async loadClothesList() {
    if (this.data.loading) return

    this.setData({ loading: true })
    try {
      const res = await wx.cloud.callFunction({
        name: 'getClothesList',
        data: {
          pageNum: this.data.pageNum,
          pageSize: this.data.pageSize,
          type: this.data.currentType === 'all' ? '' : this.data.currentType,
          keyword: this.data.searchValue
        }
      })

      const { list, total } = res.result
      const hasMore = this.data.clothesList.length + list.length < total

      this.setData({
        clothesList: [...this.data.clothesList, ...list],
        pageNum: this.data.pageNum + 1,
        hasMore
      })
    } catch (error) {
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      })
    } finally {
      this.setData({ loading: false })
    }
  },

  // 搜索输入
  onSearchInput(e) {
    this.setData({
      searchValue: e.detail.value
    })
  },

  // 搜索确认
  onSearch() {
    this.setData({
      pageNum: 1,
      hasMore: true,
      clothesList: []
    })
    this.loadClothesList()
  },

  // 切换衣物类型
  switchType(e) {
    const type = e.currentTarget.dataset.type
    this.setData({
      currentType: type,
      pageNum: 1,
      hasMore: true,
      clothesList: []
    })
    this.loadClothesList()
  },

  // 跳转到详情页
  goToDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/clothes/detail?id=${id}`
    })
  },

  // 编辑衣物
  editClothes(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/clothes/edit?id=${id}`
    })
  },

  // 删除衣物
  deleteClothes(e) {
    const id = e.currentTarget.dataset.id
    wx.showModal({
      title: '确认删除',
      content: '确定要删除这件衣物吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            await wx.cloud.callFunction({
              name: 'deleteClothes',
              data: { id }
            })

            // 从列表中移除
            const clothesList = this.data.clothesList.filter(item => item._id !== id)
            this.setData({ clothesList })

            wx.showToast({
              title: '删除成功',
              icon: 'success'
            })
          } catch (error) {
            wx.showToast({
              title: '删除失败',
              icon: 'none'
            })
          }
        }
      }
    })
  },

  // 新建衣物
  createNewClothes() {
    wx.navigateTo({
      url: '/pages/clothes/edit'
    })
  }
})