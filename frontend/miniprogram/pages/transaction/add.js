// pages/transaction/add.js
const request = require('../../utils/request');
const constants = require('../../utils/constants');
const app = getApp();

Page({
  data: {
    transactionType: 2, // 默认支出
    amount: '',
    categoryIndex: -1,
    categories: [],
    paymentMethodIndex: 0, // 默认选择第一个支付方式
    paymentMethods: [],
    date: '',
    description: '',
    categoriesLoading: false,
    hasError: false
  },

  onLoad() {
    // 设置当前日期
    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth() + 1;
    const day = now.getDate();
    this.setData({
      date: `${year}-${month < 10 ? '0' + month : month}-${day < 10 ? '0' + day : day}`
    });
    
    // 加载分类和支付方式
    this.loadCategories();
    this.loadPaymentMethods();
  },

  // 设置交易类型
  setType(e) {
    const type = parseInt(e.currentTarget.dataset.type);
    this.setData({
      transactionType: type,
      categoryIndex: -1 // 重置分类选择
    });
    this.loadCategories();
  },

  // 加载分类
  async loadCategories() {
    try {
      this.setData({ categoriesLoading: true, hasError: false });

      // 先尝试从缓存获取
      const categories = constants.getCategoriesByType(this.data.transactionType);

      // 如果缓存中没有数据，尝试刷新
      if (!categories || categories.length === 0) {
        const storage = require('../../utils/storage');
        await storage.refreshCategories();

        // 重新获取数据
        const refreshedCategories = constants.getCategoriesByType(this.data.transactionType);
        this.setData({
          categories: refreshedCategories,
          categoriesLoading: false
        });
      } else {
        this.setData({
          categories: categories,
          categoriesLoading: false
        });
      }
    } catch (error) {
      console.error('加载分类失败:', error);

      // 使用默认数据作为fallback
      const defaultCategories = constants.getCategoriesByType(this.data.transactionType);
      this.setData({
        categories: defaultCategories,
        categoriesLoading: false,
        hasError: true
      });

      wx.showToast({
        title: '分类加载失败，使用默认分类',
        icon: 'none'
      });
    }
  },

  // 加载支付方式
  async loadPaymentMethods() {
    try {
      // 先尝试从缓存获取
      const paymentMethods = constants.getPaymentMethods();

      // 如果缓存中没有数据，尝试刷新
      if (!paymentMethods || paymentMethods.length === 0) {
        const storage = require('../../utils/storage');
        await storage.refreshPaymentMethods();

        // 重新获取数据
        const refreshedPaymentMethods = constants.getPaymentMethods();
        this.setData({
          paymentMethods: refreshedPaymentMethods
        });
      } else {
        this.setData({
          paymentMethods: paymentMethods
        });
      }
    } catch (error) {
      console.error('加载支付方式失败:', error);

      // 使用默认数据作为fallback
      const defaultPaymentMethods = constants.getPaymentMethods();
      this.setData({
        paymentMethods: defaultPaymentMethods
      });

      wx.showToast({
        title: '支付方式加载失败，使用默认数据',
        icon: 'none'
      });
    }
  },

  // 金额输入
  onAmountInput(e) {
    this.setData({
      amount: e.detail.value
    });
  },

  // 分类选择
  onCategoryChange(e) {
    this.setData({
      categoryIndex: parseInt(e.detail.value)
    });
  },

  // 日期选择
  onDateChange(e) {
    this.setData({
      date: e.detail.value
    });
  },

  // 支付方式选择
  onPaymentMethodChange(e) {
    this.setData({
      paymentMethodIndex: parseInt(e.detail.value)
    });
  },

  // 描述输入
  onDescriptionInput(e) {
    this.setData({
      description: e.detail.value
    });
  },

  // 提交交易
  submitTransaction() {
    const { transactionType, amount, categoryIndex, categories, paymentMethodIndex, paymentMethods, date, description } = this.data;
    
    // 表单验证
    if (!amount || amount <= 0) {
      wx.showToast({
        title: '请输入有效金额',
        icon: 'none'
      });
      return;
    }
    
    if (categoryIndex === -1) {
      wx.showToast({
        title: '请选择分类',
        icon: 'none'
      });
      return;
    }
    
    // 构建交易数据
    const transaction = {
      type: transactionType,
      amount: amount, // 直接传字符串，后端会转换为BigDecimal
      categoryId: categories[categoryIndex].id,
      paymentMethodId: paymentMethods[paymentMethodIndex].id,
      transactionTime: date,
      description: description,
      bookId: 1 // 添加必需的bookId字段
    };
    
    wx.showLoading({ title: '保存中...' });
    
    request.post('/transactions', transaction).then(() => {
      wx.hideLoading();
      wx.showToast({
        title: '添加成功',
        icon: 'success'
      });
      
      // 返回上一页
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }).catch(() => {
      wx.hideLoading();
    });
  }
})