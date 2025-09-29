// app.js
App({
  onLaunch: function () {
    // 小程序启动初始化
    console.log('智能记账小程序启动');
    this.loadInitialData();
  },

  onShow: function () {
    // 小程序从后台进入前台时
    this.loadInitialData();
  },

  async loadInitialData() {
    try {
      const storage = require('./utils/storage');

      // 验证并修复缓存
      storage.validateAndRepairCache();

      // 获取缓存统计
      const cacheStats = storage.getCacheStats();
      console.log('缓存状态:', cacheStats);

      // 尝试从缓存加载分类数据，如果缓存过期则重新获取
      let categories = storage.getCategories();
      if (!categories || categories.length === 0) {
        console.log('分类数据缓存无效，重新获取');
        try {
          categories = await storage.refreshCategories();
        } catch (error) {
          console.error('刷新分类数据失败，将使用默认数据:', error);
        }
      }

      // 尝试从缓存加载支付方式数据，如果缓存过期则重新获取
      let paymentMethods = storage.getPaymentMethods();
      if (!paymentMethods || paymentMethods.length === 0) {
        console.log('支付方式数据缓存无效，重新获取');
        try {
          paymentMethods = await storage.refreshPaymentMethods();
        } catch (error) {
          console.error('刷新支付方式数据失败，将使用默认数据:', error);
        }
      }

      console.log('初始数据加载完成');

      // 再次获取缓存统计
      const finalCacheStats = storage.getCacheStats();
      console.log('最终缓存状态:', finalCacheStats);
    } catch (error) {
      console.error('加载初始数据失败:', error);
      // 失败时不影响应用启动，将使用默认数据
    }
  },

  globalData: {
    baseUrl: 'http://localhost:8081/api',
    categoriesCache: null,
    paymentMethodsCache: null
  }
})