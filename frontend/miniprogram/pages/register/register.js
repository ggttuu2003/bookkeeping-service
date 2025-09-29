// pages/register/register.js
const request = require('../../utils/request');

Page({
  data: {
    username: '',
    password: '',
    nickname: '',
    phone: ''
  },

  // 输入事件处理
  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
  },
  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },
  onNicknameInput(e) {
    this.setData({ nickname: e.detail.value });
  },
  onPhoneInput(e) {
    this.setData({ phone: e.detail.value });
  },

  // 注册方法
  register() {
    const { username, password, nickname, phone } = this.data;
    
    // 表单验证
    if (!username || !password) {
      wx.showToast({
        title: '用户名和密码不能为空',
        icon: 'none'
      });
      return;
    }
    
    wx.showLoading({ title: '注册中...' });
    
    // 调用注册接口
    request.post('/auth/register', {
      username,
      password,
      nickname,
      phone
    }).then(res => {
      wx.hideLoading();
      wx.showToast({
        title: '注册成功',
        icon: 'success'
      });
      
      // 延迟跳转到登录页
      setTimeout(() => {
        wx.navigateTo({
          url: '/pages/login/login'
        });
      }, 1500);
    }).catch(err => {
      wx.hideLoading();
      wx.showToast({
        title: err.message || '注册失败',
        icon: 'none'
      });
    });
  },

  // 跳转到登录页面
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  }
})