// 常量配置文件

// 交易类型
const TRANSACTION_TYPES = {
  INCOME: 1,
  EXPENSE: 2
};

// 支出类目
const EXPENSE_CATEGORIES = [
  { id: 1, name: '餐饮', icon: '🍽️', color: '#FF6B6B' },
  { id: 2, name: '交通', icon: '🚗', color: '#4ECDC4' },
  { id: 3, name: '购物', icon: '🛍️', color: '#45B7D1' },
  { id: 4, name: '娱乐', icon: '🎬', color: '#96CEB4' },
  { id: 5, name: '居家', icon: '🏠', color: '#FFEAA7' },
  { id: 6, name: '学习', icon: '📚', color: '#DDA0DD' },
  { id: 7, name: '医疗', icon: '💊', color: '#98D8C8' },
  { id: 8, name: '服饰', icon: '👔', color: '#F7DC6F' },
  { id: 9, name: '人情', icon: '🎁', color: '#BB8FCE' },
  { id: 10, name: '其他', icon: '📝', color: '#A9A9A9' }
];

// 收入类目
const INCOME_CATEGORIES = [
  { id: 11, name: '工资', icon: '💰', color: '#58D68D' },
  { id: 12, name: '理财', icon: '📈', color: '#5DADE2' },
  { id: 13, name: '兼职', icon: '💼', color: '#F8C471' },
  { id: 14, name: '红包', icon: '🧧', color: '#EC7063' },
  { id: 15, name: '其他', icon: '💎', color: '#A569BD' }
];

// 支付方式
const PAYMENT_METHODS = [
  { id: 1, name: '微信支付', icon: '💬', color: '#1AAD19' },
  { id: 2, name: '支付宝', icon: '💙', color: '#1677FF' },
  { id: 3, name: '银行卡', icon: '💳', color: '#FA8C16' },
  { id: 4, name: '现金', icon: '💵', color: '#52C41A' }
];

// 获取分类数据的方法
const getCategoriesByType = (type) => {
  return type === TRANSACTION_TYPES.INCOME ? INCOME_CATEGORIES : EXPENSE_CATEGORIES;
};

// 根据ID获取分类信息
const getCategoryById = (id) => {
  const allCategories = [...EXPENSE_CATEGORIES, ...INCOME_CATEGORIES];
  return allCategories.find(category => category.id === id);
};

// 根据ID获取支付方式信息
const getPaymentMethodById = (id) => {
  return PAYMENT_METHODS.find(method => method.id === id);
};

module.exports = {
  TRANSACTION_TYPES,
  EXPENSE_CATEGORIES,
  INCOME_CATEGORIES,
  PAYMENT_METHODS,
  getCategoriesByType,
  getCategoryById,
  getPaymentMethodById
};