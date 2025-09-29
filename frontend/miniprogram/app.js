// app.js
App({
  onLaunch: function () {
    // 检查登录状态
    const token = wx.getStorageSync('token');
    if (token) {
      this.globalData.isLoggedIn = true;
      this.globalData.token = token;
    }
  },
  globalData: {
    userInfo: null,
    isLoggedIn: false,
    token: '',
    baseUrl: 'http://localhost:8080/api'
  }
})