// pages/login/login.js
const request = require('../../utils/request');
const app = getApp();

Page({
  data: {
    username: '',
    password: ''
  },

  // 用户名输入事件
  onUsernameInput(e) {
    this.setData({
      username: e.detail.value
    });
  },

  // 密码输入事件
  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
    });
  },

  // 登录方法
  login() {
    const { username, password } = this.data;
    
    // 表单验证
    if (!username || !password) {
      wx.showToast({
        title: '用户名和密码不能为空',
        icon: 'none'
      });
      return;
    }
    
    // 显示加载提示
    wx.showLoading({
      title: '登录中...',
    });
    
    // 调用登录接口
    request.post('/auth/login', {
      username,
      password
    }).then(res => {
      // 隐藏加载提示
      wx.hideLoading();
      
      // 保存token
      wx.setStorageSync('token', res.data.token);
      app.globalData.isLoggedIn = true;
      app.globalData.token = res.data.token;
      
      // 获取用户信息
      return request.get('/auth/info');
    }).then(res => {
      // 保存用户信息
      app.globalData.userInfo = res.data;
      
      // 跳转到首页
      wx.switchTab({
        url: '/pages/index/index'
      });
    }).catch(err => {
      // 隐藏加载提示
      wx.hideLoading();
      
      // 显示错误信息
      wx.showToast({
        title: err.message || '登录失败',
        icon: 'none'
      });
    });
  },

  // 跳转到注册页面
  goToRegister() {
    wx.navigateTo({
      url: '/pages/register/register'
    });
  }
})