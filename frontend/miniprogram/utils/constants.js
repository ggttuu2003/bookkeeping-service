// 常量配置文件

// 交易类型
const TRANSACTION_TYPES = {
  INCOME: 1,
  EXPENSE: 2
};


// 获取分类数据的方法（从 API 获取）
const getCategoriesByType = (type) => {
  const storage = require('./storage');
  return storage.getCategoriesByType(type) || [];
};

// 根据ID获取分类信息
const getCategoryById = (id) => {
  const storage = require('./storage');
  return storage.getCategoryById(id);
};

// 获取支付方式数据（从 API 获取）
const getPaymentMethods = () => {
  const storage = require('./storage');
  return storage.getPaymentMethods() || [];
};

// 根据ID获取支付方式信息
const getPaymentMethodById = (id) => {
  const storage = require('./storage');
  return storage.getPaymentMethodById(id);
};

module.exports = {
  TRANSACTION_TYPES,
  getCategoriesByType,
  getCategoryById,
  getPaymentMethods,
  getPaymentMethodById
};