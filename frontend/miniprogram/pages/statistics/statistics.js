// pages/statistics/statistics.js
const request = require('../../utils/request');
const app = getApp();

Page({
  data: {
    currentYear: new Date().getFullYear(),
    currentMonth: new Date().getMonth() + 1,
    currentDate: '',
    activeTab: 'overview',
    categoryType: 2, // 默认支出
    totalIncome: '0.00',
    totalExpense: '0.00',
    balance: '0.00',
    total: '0.00',
    incomePercentage: 0,
    expensePercentage: 0,
    categoryStats: [],
    trendData: [],
    yAxisLabels: []
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
    this.loadStatistics();
  },

  // 切换标签
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({
      activeTab: tab
    });
    
    if (tab === 'category') {
      this.loadCategoryStats();
    } else if (tab === 'trend') {
      this.loadTrendData();
    }
  },

  // 设置分类统计类型
  setCategoryType(e) {
    const type = parseInt(e.currentTarget.dataset.type);
    this.setData({
      categoryType: type
    });
    this.loadCategoryStats();
  },

  // 日期选择器变化
  onDateChange(e) {
    const date = e.detail.value;
    const [year, month] = date.split('-');
    this.setData({
      currentYear: parseInt(year),
      currentMonth: parseInt(month),
      currentDate: date
    });
    this.loadStatistics();
  },

  // 加载统计数据
  loadStatistics() {
    this.loadMonthlyStats();
    
    if (this.data.activeTab === 'category') {
      this.loadCategoryStats();
    } else if (this.data.activeTab === 'trend') {
      this.loadTrendData();
    }
  },

  // 加载月度统计
  loadMonthlyStats() {
    const { currentYear, currentMonth } = this.data;

    request.get('/statistics/monthly', {
      year: currentYear,
      bookId: 1  // 使用默认账本ID
    }).then(res => {
      const monthData = res.data.find(item => item.month === currentMonth);
      
      if (monthData) {
        const income = parseFloat(monthData.income);
        const expense = parseFloat(monthData.expense);
        const balance = income - expense;
        const total = income + expense;
        
        // 计算百分比，确保精度和总和为100%
        let incomePercentage = 0;
        let expensePercentage = 0;

        if (total > 0) {
          incomePercentage = Math.round((income / total) * 100);
          expensePercentage = 100 - incomePercentage; // 确保总和为100%
        }

        // 计算角度值用于饼图渲染
        const incomeAngle = (incomePercentage / 100) * 360;
        const expenseAngle = (expensePercentage / 100) * 360;

        this.setData({
          totalIncome: income.toFixed(2),
          totalExpense: expense.toFixed(2),
          balance: balance.toFixed(2),
          total: total.toFixed(2),
          incomePercentage: incomePercentage,
          expensePercentage: expensePercentage,
          incomeAngle: incomeAngle,
          expenseAngle: expenseAngle
        });
      }
    });
  },

  // 加载分类统计
  loadCategoryStats() {
    const { currentYear, currentMonth, categoryType } = this.data;

    // 计算当月的开始和结束日期
    const startDate = `${currentYear}-${currentMonth.toString().padStart(2, '0')}-01`;
    const endDate = new Date(currentYear, currentMonth, 0).getDate(); // 获取当月最后一天
    const endDateStr = `${currentYear}-${currentMonth.toString().padStart(2, '0')}-${endDate.toString().padStart(2, '0')}`;

    request.get('/statistics/category', {
      startDate: startDate,
      endDate: endDateStr,
      type: categoryType
    }).then(res => {
      // 计算总金额
      let total = 0;
      res.data.forEach(item => {
        total += parseFloat(item.amount);
      });
      
      // 计算每个分类的百分比
      const categoryStats = res.data.map(item => {
        const amount = parseFloat(item.amount);
        const percentage = total > 0 ? Math.round((amount / total) * 100) : 0;
        
        return {
          ...item,
          amount: amount.toFixed(2),
          percentage: percentage
        };
      });
      
      // 按金额排序
      categoryStats.sort((a, b) => parseFloat(b.amount) - parseFloat(a.amount));
      
      this.setData({
        categoryStats: categoryStats
      });
    });
  },

  // 加载趋势数据
  loadTrendData() {
    const { currentYear, currentMonth } = this.data;

    request.get('/statistics/daily', {
      year: currentYear,
      month: currentMonth,
      bookId: 1  // 使用默认账本ID
    }).then(res => {
      // 找出最大值，用于计算图表高度
      let maxAmount = 0;
      res.data.forEach(item => {
        const income = parseFloat(item.income);
        const expense = parseFloat(item.expense);
        maxAmount = Math.max(maxAmount, income, expense);
      });
      
      // 计算图表高度（最大高度为150px）
      const maxHeight = 150;
      const trendData = res.data.map(item => {
        const income = parseFloat(item.income);
        const expense = parseFloat(item.expense);
        
        return {
          day: item.day,
          income: income.toFixed(2),
          expense: expense.toFixed(2),
          incomeHeight: maxAmount > 0 ? Math.round((income / maxAmount) * maxHeight) : 0,
          expenseHeight: maxAmount > 0 ? Math.round((expense / maxAmount) * maxHeight) : 0
        };
      });
      
      // 生成Y轴标签
      const step = maxAmount / 4;
      const yAxisLabels = [
        '0',
        (step).toFixed(0),
        (step * 2).toFixed(0),
        (step * 3).toFixed(0),
        maxAmount.toFixed(0)
      ];
      
      this.setData({
        trendData: trendData,
        yAxisLabels: yAxisLabels
      });
    });
  }
})