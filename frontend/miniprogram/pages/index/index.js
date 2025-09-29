// 首页逻辑
const request = require('../../utils/request');

Page({
  data: {
    balance: 0,
    monthIncome: 0,
    monthExpense: 0,
    recentTransactions: [],
    loading: false,
    hasError: false
  },

  onLoad() {
    this.loadData();
  },

  onShow() {
    this.loadData();
  },

  onPullDownRefresh() {
    this.loadData().finally(() => {
      wx.stopPullDownRefresh();
    });
  },

  async loadData() {
    this.setData({ loading: true, hasError: false });

    try {
      const response = await request.get('/home/summary');

      if (response.code === 200) {
        const data = response.data;

        // 处理最近交易记录数据格式
        const recentTransactions = (data.recentTransactions || []).map(transaction => ({
          id: transaction.id,
          type: transaction.type === 1 ? 'expense' : 'income',
          amount: parseFloat(transaction.amount),
          categoryName: transaction.categoryName,
          categoryIcon: transaction.categoryIcon || (transaction.type === 1 ? '📝' : '💰'),
          description: transaction.description || '',
          date: this.formatTransactionTime(transaction.transactionTime)
        }));

        this.setData({
          balance: parseFloat(data.balance || 0),
          monthIncome: parseFloat(data.monthIncome || 0),
          monthExpense: parseFloat(data.monthExpense || 0),
          recentTransactions: recentTransactions,
          loading: false
        });
      } else {
        this.handleLoadError('获取数据失败');
      }
    } catch (error) {
      console.error('首页数据加载失败:', error);
      this.handleLoadError('网络请求失败');
    }
  },

  handleLoadError(message) {
    this.setData({
      loading: false,
      hasError: true
    });
    wx.showToast({
      title: message,
      icon: 'none'
    });
  },

  formatTransactionTime(transactionTime) {
    if (!transactionTime) return '';

    const date = new Date(transactionTime);
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const transactionDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());

    const diffDays = Math.floor((today - transactionDate) / (1000 * 60 * 60 * 24));

    if (diffDays === 0) {
      return `今天 ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
    } else if (diffDays === 1) {
      return `昨天 ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
    } else {
      return `${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
    }
  },

  retryLoad() {
    this.loadData();
  },

  goToRecord() {
    wx.navigateTo({
      url: '/pages/transaction/add'
    });
  },

  goToStatistics() {
    wx.navigateTo({
      url: '/pages/statistics/statistics'
    });
  },

  viewAllRecords() {
    // 跳转到所有记录页面
    console.log('查看所有记录');
  }
});