// pages/transaction/transaction.js
const request = require('../../utils/request');
const app = getApp();

Page({
  data: {
    currentYear: new Date().getFullYear(),
    currentMonth: new Date().getMonth() + 1,
    currentDate: '',
    activeTab: 'all',
    totalExpense: '0.00',
    totalIncome: '0.00',
    transactions: [],
    page: 1,
    size: 20,
    hasMore: true
  },

  onLoad() {
    // 设置当前日期
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    this.setData({
      currentYear: year,
      currentMonth: month,
      currentDate: `${year}-${month < 10 ? '0' + month : month}`
    });
  },

  onShow() {
    this.loadTransactions(true);
  },

  // 切换标签
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({
      activeTab: tab,
      page: 1,
      hasMore: true
    });
    this.loadTransactions(true);
  },

  // 日期选择器变化
  onDateChange(e) {
    const date = e.detail.value;
    const [year, month] = date.split('-');
    this.setData({
      currentYear: parseInt(year),
      currentMonth: parseInt(month),
      currentDate: date,
      page: 1,
      hasMore: true
    });
    this.loadTransactions(true);
  },

  // 加载交易记录
  loadTransactions(refresh = false) {
    const { currentYear, currentMonth, activeTab, page, size } = this.data;
    
    // 构建查询参数
    const params = {
      bookId: 1, // 默认账本
      page: page,
      size: size,
      year: currentYear,
      month: currentMonth
    };
    
    // 根据标签筛选交易类型
    if (activeTab === 'expense') {
      params.type = 2; // 支出
    } else if (activeTab === 'income') {
      params.type = 1; // 收入
    }
    
    wx.showLoading({ title: '加载中...' });
    
    request.get('/transactions', params).then(res => {
      wx.hideLoading();
      
      const newTransactions = res.data.records || [];
      const hasMore = newTransactions.length === size;
      
      this.setData({
        transactions: refresh ? newTransactions : [...this.data.transactions, ...newTransactions],
        hasMore: hasMore,
        page: refresh ? 1 : page + 1
      });
      
      // 计算总收支
      this.calculateTotal();
    }).catch(() => {
      wx.hideLoading();
    });
  },

  // 计算总收支
  calculateTotal() {
    const { transactions } = this.data;
    let totalExpense = 0;
    let totalIncome = 0;
    
    transactions.forEach(item => {
      if (item.type === 1) { // 收入
        totalIncome += parseFloat(item.amount);
      } else { // 支出
        totalExpense += parseFloat(item.amount);
      }
    });
    
    this.setData({
      totalExpense: totalExpense.toFixed(2),
      totalIncome: totalIncome.toFixed(2)
    });
  },

  // 跳转到交易详情
  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/transaction/detail?id=${id}`
    });
  },

  // 跳转到添加交易
  goToAdd() {
    wx.navigateTo({
      url: '/pages/transaction/add'
    });
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.setData({
      page: 1,
      hasMore: true
    });
    this.loadTransactions(true);
    wx.stopPullDownRefresh();
  },

  // 上拉加载更多
  onReachBottom() {
    if (this.data.hasMore) {
      this.loadTransactions();
    }
  }
})