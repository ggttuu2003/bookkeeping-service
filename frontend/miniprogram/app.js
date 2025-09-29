// app.js
App({
  onLaunch: function () {
    // 小程序启动初始化
    console.log('智能记账小程序启动');
  },
  globalData: {
    baseUrl: 'http://localhost:8081/api'
  }
})