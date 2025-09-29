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
    paymentMethods: constants.PAYMENT_METHODS,
    date: '',
    description: '',
    bookId: 1 // 默认账本ID
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
    
    // 加载分类
    this.loadCategories();
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
  loadCategories() {
    const categories = constants.getCategoriesByType(this.data.transactionType);
    this.setData({
      categories: categories
    });
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
    const { transactionType, amount, categoryIndex, categories, paymentMethodIndex, paymentMethods, date, description, bookId } = this.data;
    
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
      amount: parseFloat(amount),
      categoryId: categories[categoryIndex].id,
      paymentMethodId: paymentMethods[paymentMethodIndex].id,
      transactionTime: date,
      description: description,
      bookId: bookId
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