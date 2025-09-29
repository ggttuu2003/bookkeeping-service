// 首页逻辑
Page({
  data: {
    balance: 0,
    monthIncome: 0,
    monthExpense: 0,
    recentTransactions: []
  },

  onLoad() {
    this.loadData();
  },

  onShow() {
    this.loadData();
  },

  loadData() {
    // 模拟数据
    const mockData = {
      balance: 12580.50,
      monthIncome: 15000,
      monthExpense: 2419.50,
      recentTransactions: [
        {
          id: 1,
          type: 'expense',
          amount: 35.00,
          categoryName: '餐饮',
          categoryIcon: '🍽️',
          description: '午餐',
          date: '今天 12:30'
        },
        {
          id: 2,
          type: 'expense',
          amount: 8.00,
          categoryName: '交通',
          categoryIcon: '🚗',
          description: '地铁',
          date: '今天 08:15'
        },
        {
          id: 3,
          type: 'income',
          amount: 15000.00,
          categoryName: '工资',
          categoryIcon: '💰',
          description: '月度工资',
          date: '09-28'
        }
      ]
    };

    this.setData(mockData);
  },

  goToRecord() {
    wx.navigateTo({
      url: '/pages/record/record'
    });
  },

  goToStatistics() {
    wx.navigateTo({
      url: '/pages/statistics/statistics'
    });
  },

  goToBudget() {
    wx.navigateTo({
      url: '/pages/budget/budget'
    });
  },

  viewAllRecords() {
    // 跳转到所有记录页面
    console.log('查看所有记录');
  }
});