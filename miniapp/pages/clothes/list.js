const app = getApp()

Page({
  data: {
    loading: false,
    searchValue: '',
    clothesTypes: ['全部', '上衣', '裤子', '裙子', '外套', '内衣', '其他'],
    selectedType: '全部',
    clothesList: [],
    pageNum: 1,
    pageSize: 10,
    hasMore: true
  },

  onLoad() {
    this.loadClothesList()
  },

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

  onReachBottom() {
    if (this.data.hasMore && !this.data.loading) {
      this.loadClothesList()
    }
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
          type: this.data.selectedType === '全部' ? '' : this.data.selectedType,
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
  onSearchConfirm() {
    this.setData({
      pageNum: 1,
      hasMore: true,
      clothesList: []
    })
    this.loadClothesList()
  },

  // 选择衣物类型
  onTypeSelect(e) {
    const type = e.currentTarget.dataset.type
    this.setData({
      selectedType: type,
      pageNum: 1,
      hasMore: true,
      clothesList: []
    })
    this.loadClothesList()
  },

  // 编辑衣物
  onEditClothes(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/clothes/edit?id=${id}`
    })
  },

  // 删除衣物
  onDeleteClothes(e) {
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
  onCreateClothes() {
    wx.navigateTo({
      url: '/pages/clothes/edit'
    })
  }
}) 