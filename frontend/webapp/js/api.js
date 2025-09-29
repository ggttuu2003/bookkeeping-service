// API请求封装
const API = {
    // 基础请求方法
    request: async function(url, method = 'GET', data = null) {
        const token = Utils.getToken();
        const headers = {
            'Content-Type': 'application/json'
        };
        
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        
        const options = {
            method,
            headers
        };
        
        if (data && (method === 'POST' || method === 'PUT')) {
            options.body = JSON.stringify(data);
        }
        
        try {
            const response = await fetch(`${CONFIG.API_BASE_URL}${url}`, options);
            
            // 处理401未授权
            if (response.status === 401) {
                Utils.clearUserData();
                Router.navigate('/login');
                Utils.showToast('登录已过期，请重新登录', 'error');
                return null;
            }
            
            const result = await response.json();
            
            if (!response.ok) {
                throw new Error(result.message || '请求失败');
            }
            
            return result;
        } catch (error) {
            Utils.showToast(error.message || '网络错误', 'error');
            return null;
        }
    },
    
    // GET请求
    get: function(url, params = {}) {
        const queryString = Object.keys(params)
            .filter(key => params[key] !== undefined && params[key] !== null)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
            .join('&');
        
        const fullUrl = queryString ? `${url}?${queryString}` : url;
        return this.request(fullUrl, 'GET');
    },
    
    // POST请求
    post: function(url, data) {
        return this.request(url, 'POST', data);
    },
    
    // PUT请求
    put: function(url, data) {
        return this.request(url, 'PUT', data);
    },
    
    // DELETE请求
    delete: function(url) {
        return this.request(url, 'DELETE');
    },
    
    // 用户登录
    login: function(username, password) {
        return this.post('/auth/login', { username, password });
    },
    
    // 用户注册
    register: function(userData) {
        return this.post('/auth/register', userData);
    },
    
    // 获取用户信息
    getUserInfo: function() {
        return this.get('/auth/info');
    },
    
    // 获取交易列表
    getTransactions: function(params) {
        return this.get('/transactions', params);
    },
    
    // 获取交易详情
    getTransaction: function(id) {
        return this.get(`/transactions/${id}`);
    },
    
    // 创建交易
    createTransaction: function(data) {
        return this.post('/transactions', data);
    },
    
    // 更新交易
    updateTransaction: function(id, data) {
        return this.put(`/transactions/${id}`, data);
    },
    
    // 删除交易
    deleteTransaction: function(id) {
        return this.delete(`/transactions/${id}`);
    },
    
    // 获取分类列表
    getCategories: function(type) {
        return this.get('/categories', { type });
    },
    
    // 获取月度统计
    getMonthlyStats: function(year, bookId) {
        return this.get('/statistics/monthly', { year, bookId });
    },
    
    // 获取日度统计
    getDailyStats: function(year, month, bookId) {
        return this.get('/statistics/daily', { year, month, bookId });
    },
    
    // 获取分类统计
    getCategoryStats: function(year, month, type, bookId) {
        return this.get('/statistics/category', { year, month, type, bookId });
    }
};