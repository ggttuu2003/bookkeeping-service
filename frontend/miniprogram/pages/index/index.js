// pages/index/index.js
const request = require('../../utils/request');
const app = getApp();

Page({
  data: {
    userInfo: {},
    monthlyExpense: '0.00',
    monthlyIncome: '0.00',
    transactions: []
  },

  onLoad() {
    this.checkLoginStatus();
  },

  onShow() {
    if (app.globalData.isLoggedIn) {
      this.getUserInfo();
      this.getMonthlyStatistics();
      this.getRecentTransactions();
    }
  },

  // 检查登录状态
  checkLoginStatus() {
    if (!app.globalData.isLoggedIn) {
      wx.redirectTo({
        url: '/pages/login/login'
      });
    }
  },

  // 获取用户信息
  getUserInfo() {
    request.get('/auth/info').then(res => {
      this.setData({
        userInfo: res.data
      });
      app.globalData.userInfo = res.data;
    });
  },

  // 获取月度统计
  getMonthlyStatistics() {
    const date = new Date();
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    
    request.get('/statistics/monthly', {
      year: year,
      bookId: 1 // 默认账本
    }).then(res => {
      const monthData = res.data.find(item => item.month === month);
      if (monthData) {
        this.setData({
          monthlyExpense: monthData.expense.toFixed(2),
          monthlyIncome: monthData.income.toFixed(2)
        });
      }
    });
  },

  // 获取最近交易记录
  getRecentTransactions() {
    request.get('/transactions', {
      bookId: 1, // 默认账本
      page: 1,
      size: 5
    }).then(res => {
      this.setData({
        transactions: res.data.records
      });
    });
  },

  // 跳转到交易详情
  goToTransactionDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/transaction/detail?id=${id}`
    });
  },

  // 跳转到交易列表
  goToTransactionList() {
    wx.switchTab({
      url: '/pages/transaction/transaction'
    });
  },

  // 跳转到添加交易
  goToAddTransaction() {
    wx.navigateTo({
      url: '/pages/transaction/add'
    });
  },

  // 跳转到统计分析
  goToStatistics() {
    wx.switchTab({
      url: '/pages/statistics/statistics'
    });
  }
})