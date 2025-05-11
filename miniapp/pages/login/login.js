// 获取应用实例
const app = getApp();

Page({
  data: {
    loginType: 'wechat', // 登录类型：wechat-微信登录，account-账号登录
    username: '',
    password: '',
    isAgree: false,
    redirectUrl: '',
    isLoading: false
  },
  
  onLoad: function(options) {
    // 记录重定向URL
    if (options.redirect) {
      this.setData({
        redirectUrl: decodeURIComponent(options.redirect)
      })
    }

    // 检查是否已登录
    const token = wx.getStorageSync('token')
    if (token) {
      this.navigateBack()
    }
  },
  
  // 切换登录方式
  switchLoginType: function (e) {
    const type = e.currentTarget.dataset.type
    this.setData({
      loginType: type
    })
  },
  
  // 输入用户名
  inputUsername: function (e) {
    this.setData({
      username: e.detail.value
    })
  },
  
  // 输入密码
  inputPassword: function (e) {
    this.setData({
      password: e.detail.value
    })
  },
  
  // 同意用户协议
  checkboxChange: function (e) {
    this.setData({
      isAgree: e.detail.value.length > 0
    })
  },
  
  // 账号密码登录
  accountLogin: function () {
    const { username, password, isAgree } = this.data

    if (!username) {
      wx.showToast({
        title: '请输入用户名',
        icon: 'none'
      })
      return
    }

    if (!password) {
      wx.showToast({
        title: '请输入密码',
        icon: 'none'
      })
      return
    }

    if (!isAgree) {
      wx.showToast({
        title: '请阅读并同意用户协议',
        icon: 'none'
      })
      return
    }

    this.setData({
      isLoading: true
    })

    wx.request({
      url: `${app.globalData.baseURL}/miniapp/auth/login`,
      method: 'POST',
      data: {
        username,
        password
      },
      success: (res) => {
        if (res.data.code === 1) {
          // 登录成功
          const { token, userInfo } = res.data.data
          // 保存登录信息
          wx.setStorageSync('token', token)
          wx.setStorageSync('userInfo', userInfo)
          app.globalData.userInfo = userInfo
          
          wx.showToast({
            title: '登录成功',
            icon: 'success',
            duration: 1500,
            success: () => {
              setTimeout(() => {
                this.navigateBack()
              }, 1500)
            }
          })
        } else {
          // 登录失败
          wx.showToast({
            title: res.data.msg || '登录失败',
            icon: 'none'
          })
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络异常，请稍后重试',
          icon: 'none'
        })
      },
      complete: () => {
        this.setData({
          isLoading: false
        })
      }
    })
  },
  
  // 微信一键登录
  wechatLogin: function () {
    if (!this.data.isAgree) {
      wx.showToast({
        title: '请阅读并同意用户协议',
        icon: 'none'
      })
      return
    }

    this.setData({
      isLoading: true
    })

    wx.login({
      success: (res) => {
        if (res.code) {
          // 发送code到后台换取openId和sessionKey
          wx.request({
            url: `${app.globalData.baseURL}/miniapp/auth/wechat-login`,
            method: 'POST',
            data: {
              code: res.code
            },
            success: (loginRes) => {
              if (loginRes.data.code === 1) {
                const data = loginRes.data.data
                
                if (data.isNewUser) {
                  // 新用户需要绑定手机号
                  wx.setStorageSync('openid', data.openid)
                  wx.navigateTo({
                    url: '/pages/login/bind-phone'
                  })
                } else {
                  // 老用户直接登录成功
                  wx.setStorageSync('token', data.token)
                  wx.setStorageSync('userInfo', data.userInfo)
                  app.globalData.userInfo = data.userInfo
                  
                  wx.showToast({
                    title: '登录成功',
                    icon: 'success',
                    duration: 1500,
                    success: () => {
                      setTimeout(() => {
                        this.navigateBack()
                      }, 1500)
                    }
                  })
                }
              } else {
                // 登录失败
                wx.showToast({
                  title: loginRes.data.msg || '微信登录失败',
                  icon: 'none'
                })
              }
            },
            fail: () => {
              wx.showToast({
                title: '网络异常，请稍后重试',
                icon: 'none'
              })
            },
            complete: () => {
              this.setData({
                isLoading: false
              })
            }
          })
        } else {
          wx.showToast({
            title: '微信登录失败',
            icon: 'none'
          })
          this.setData({
            isLoading: false
          })
        }
      },
      fail: () => {
        wx.showToast({
          title: '微信登录失败',
          icon: 'none'
        })
        this.setData({
          isLoading: false
        })
      }
    })
  },
  
  // 跳转到用户协议
  navigateToAgreement: function () {
    wx.navigateTo({
      url: '/pages/agreement/agreement'
    })
  },
  
  // 跳转回原页面
  navigateBack: function () {
    const { redirectUrl } = this.data
    if (redirectUrl) {
      wx.reLaunch({
        url: redirectUrl
      })
    } else {
      wx.switchTab({
        url: '/pages/index/index'
      })
    }
  }
}); 