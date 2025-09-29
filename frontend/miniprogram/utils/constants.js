// 常量配置文件

// 交易类型
const TRANSACTION_TYPES = {
  INCOME: 1,
  EXPENSE: 2
};

// 分类数据现在从API获取，这里保留默认分类作为fallback
const DEFAULT_EXPENSE_CATEGORIES = [
  { id: 1, name: '餐饮', icon: '🍽️', color: '#FF6B6B', type: 1 },
  { id: 2, name: '交通', icon: '🚗', color: '#4ECDC4', type: 1 },
  { id: 3, name: '购物', icon: '🛍️', color: '#45B7D1', type: 1 },
  { id: 4, name: '娱乐', icon: '🎬', color: '#96CEB4', type: 1 },
  { id: 5, name: '居家', icon: '🏠', color: '#FFEAA7', type: 1 },
  { id: 6, name: '学习', icon: '📚', color: '#DDA0DD', type: 1 },
  { id: 7, name: '医疗', icon: '💊', color: '#98D8C8', type: 1 },
  { id: 8, name: '服饰', icon: '👔', color: '#F7DC6F', type: 1 },
  { id: 9, name: '人情', icon: '🎁', color: '#BB8FCE', type: 1 },
  { id: 10, name: '其他', icon: '📝', color: '#A9A9A9', type: 1 }
];

const DEFAULT_INCOME_CATEGORIES = [
  { id: 11, name: '工资', icon: '💰', color: '#58D68D', type: 2 },
  { id: 12, name: '理财', icon: '📈', color: '#5DADE2', type: 2 },
  { id: 13, name: '兼职', icon: '💼', color: '#F8C471', type: 2 },
  { id: 14, name: '红包', icon: '🧧', color: '#EC7063', type: 2 },
  { id: 15, name: '其他', icon: '💎', color: '#A569BD', type: 2 }
];

// 默认支付方式，作为fallback
const DEFAULT_PAYMENT_METHODS = [
  { id: 1, name: '微信支付', icon: '💬', color: '#1AAD19' },
  { id: 2, name: '支付宝', icon: '💙', color: '#1677FF' },
  { id: 3, name: '银行卡', icon: '💳', color: '#FA8C16' },
  { id: 4, name: '现金', icon: '💵', color: '#52C41A' }
];

// 获取分类数据的方法（现在从缓存获取，fallback到默认数据）
const getCategoriesByType = (type) => {
  const storage = require('./storage');
  let categories = storage.getCategoriesByType(type);

  // 如果API数据不可用，使用默认数据
  if (!categories || categories.length === 0) {
    console.log('使用默认分类数据');
    return type === TRANSACTION_TYPES.INCOME ? DEFAULT_INCOME_CATEGORIES : DEFAULT_EXPENSE_CATEGORIES;
  }

  return categories;
};

// 根据ID获取分类信息
const getCategoryById = (id) => {
  const storage = require('./storage');
  let category = storage.getCategoryById(id);

  // 如果API数据不可用，使用默认数据
  if (!category) {
    const allDefaultCategories = [...DEFAULT_EXPENSE_CATEGORIES, ...DEFAULT_INCOME_CATEGORIES];
    category = allDefaultCategories.find(cat => cat.id === id);
  }

  return category;
};

// 获取支付方式数据（现在从缓存获取，fallback到默认数据）
const getPaymentMethods = () => {
  const storage = require('./storage');
  let paymentMethods = storage.getPaymentMethods();

  // 如果API数据不可用，使用默认数据
  if (!paymentMethods || paymentMethods.length === 0) {
    console.log('使用默认支付方式数据');
    return DEFAULT_PAYMENT_METHODS;
  }

  return paymentMethods;
};

// 根据ID获取支付方式信息
const getPaymentMethodById = (id) => {
  const storage = require('./storage');
  let paymentMethod = storage.getPaymentMethodById(id);

  // 如果API数据不可用，使用默认数据
  if (!paymentMethod) {
    paymentMethod = DEFAULT_PAYMENT_METHODS.find(method => method.id === id);
  }

  return paymentMethod;
};

module.exports = {
  TRANSACTION_TYPES,
  DEFAULT_EXPENSE_CATEGORIES,
  DEFAULT_INCOME_CATEGORIES,
  DEFAULT_PAYMENT_METHODS,
  getCategoriesByType,
  getCategoryById,
  getPaymentMethods,
  getPaymentMethodById
};