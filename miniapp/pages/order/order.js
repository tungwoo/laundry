// pages/order/order.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentStatus: 'all', // 当前选中的状态标签
    searchText: '', // 搜索关键词
    orders: [], // 订单列表
    page: 1, // 当前页码
    pageSize: 10, // 每页数量
    totalPages: 1, // 总页数
    loading: false, // 是否加载中
    loadingMore: false, // 是否加载更多中
    statusMapping: {
      'pending': '待处理',
      'processing': '处理中',
      'ready': '待取件',
      'completed': '已完成',
      'cancelled': '已取消'
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    if (options.status) {
      this.setData({
        currentStatus: options.status
      });
    }
    this.loadOrders();
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
    // 每次显示页面都刷新订单列表
    this.setData({
      page: 1
    });
    this.loadOrders();
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
      page: 1,
      orders: []
    });
    this.loadOrders();
    wx.stopPullDownRefresh();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {
    if (this.data.page < this.data.totalPages) {
      this.setData({
        page: this.data.page + 1,
        loadingMore: true
      });
      this.loadOrders();
    }
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  /**
   * 切换订单状态
   */
  switchStatus(e) {
    const status = e.currentTarget.dataset.status;
    if (status !== this.data.currentStatus) {
      this.setData({
        currentStatus: status,
        page: 1,
        orders: []
      });
      this.loadOrders();
    }
  },

  /**
   * 搜索输入
   */
  onSearchInput(e) {
    this.setData({
      searchText: e.detail.value
    });
  },

  /**
   * 执行搜索
   */
  onSearch() {
    this.setData({
      page: 1,
      orders: []
    });
    this.loadOrders();
  },

  /**
   * 加载订单数据
   * 这里使用模拟数据，实际项目中应该调用API
   */
  loadOrders() {
    const self = this;
    self.setData({
      loading: true
    });

    // 模拟网络请求
    setTimeout(() => {
      // 模拟订单数据
      const mockOrders = [
        {
          id: '1001',
          orderNo: 'XY20230612001',
          status: 'pending',
          statusText: '待处理',
          customerName: '张三',
          customerPhone: '138****1234',
          clothesCount: 3,
          totalAmount: 108.00,
          createTime: '2023-06-12 14:30'
        },
        {
          id: '1002',
          orderNo: 'XY20230612002',
          status: 'processing',
          statusText: '处理中',
          customerName: '李四',
          customerPhone: '139****5678',
          clothesCount: 5,
          totalAmount: 165.50,
          createTime: '2023-06-12 16:45'
        },
        {
          id: '1003',
          orderNo: 'XY20230611001',
          status: 'ready',
          statusText: '待取件',
          customerName: '王五',
          customerPhone: '187****9012',
          clothesCount: 2,
          totalAmount: 89.00,
          createTime: '2023-06-11 09:15'
        },
        {
          id: '1004',
          orderNo: 'XY20230610001',
          status: 'completed',
          statusText: '已完成',
          customerName: '赵六',
          customerPhone: '156****3456',
          clothesCount: 4,
          totalAmount: 142.00,
          createTime: '2023-06-10 11:30'
        }
      ];

      // 根据状态筛选
      let filteredOrders = mockOrders;
      if (self.data.currentStatus !== 'all') {
        filteredOrders = mockOrders.filter(item => item.status === self.data.currentStatus);
      }

      // 根据搜索关键词筛选
      if (self.data.searchText) {
        const keyword = self.data.searchText.toLowerCase();
        filteredOrders = filteredOrders.filter(item => 
          item.orderNo.toLowerCase().includes(keyword) || 
          item.customerName.toLowerCase().includes(keyword)
        );
      }

      // 更新数据
      self.setData({
        orders: self.data.page === 1 ? filteredOrders : [...self.data.orders, ...filteredOrders],
        loading: false,
        loadingMore: false,
        totalPages: Math.ceil(filteredOrders.length / self.data.pageSize)
      });
    }, 500);
  },

  /**
   * 跳转到订单详情
   */
  goToOrderDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/order/detail?id=${id}`
    });
  },

  /**
   * 更新订单状态
   */
  updateOrderStatus(e) {
    const id = e.currentTarget.dataset.id;
    const status = e.currentTarget.dataset.status;
    const statusText = this.data.statusMapping[status];
    
    wx.showModal({
      title: '确认操作',
      content: `确定将订单状态更新为"${statusText}"吗？`,
      success: (res) => {
        if (res.confirm) {
          // 模拟API请求更新状态
          wx.showLoading({
            title: '更新中...',
          });
          
          setTimeout(() => {
            // 更新本地订单数据状态
            const orders = this.data.orders.map(item => {
              if (item.id === id) {
                return {
                  ...item,
                  status: status,
                  statusText: statusText
                };
              }
              return item;
            });
            
            this.setData({
              orders: orders
            });
            
            wx.hideLoading();
            wx.showToast({
              title: '状态已更新',
              icon: 'success'
            });
          }, 500);
        }
      }
    });
  },

  /**
   * 创建新订单
   */
  createNewOrder() {
    wx.navigateTo({
      url: '/pages/order/create'
    });
  }
})