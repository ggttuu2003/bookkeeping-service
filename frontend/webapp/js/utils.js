// 工具函数
const Utils = {
    // 显示加载中
    showLoading: function() {
        document.getElementById('loading').style.display = 'flex';
    },
    
    // 隐藏加载中
    hideLoading: function() {
        document.getElementById('loading').style.display = 'none';
    },
    
    // 获取本地存储
    getStorage: function(key) {
        return localStorage.getItem(key);
    },
    
    // 设置本地存储
    setStorage: function(key, value) {
        localStorage.setItem(key, value);
    },
    
    // 删除本地存储
    removeStorage: function(key) {
        localStorage.removeItem(key);
    },
    
    // 获取Token
    getToken: function() {
        return this.getStorage(CONFIG.STORAGE_KEYS.TOKEN);
    },
    
    // 设置Token
    setToken: function(token) {
        this.setStorage(CONFIG.STORAGE_KEYS.TOKEN, token);
    },
    
    // 获取用户信息
    getUserInfo: function() {
        const userInfoStr = this.getStorage(CONFIG.STORAGE_KEYS.USER_INFO);
        return userInfoStr ? JSON.parse(userInfoStr) : null;
    },
    
    // 设置用户信息
    setUserInfo: function(userInfo) {
        this.setStorage(CONFIG.STORAGE_KEYS.USER_INFO, JSON.stringify(userInfo));
    },
    
    // 清除用户数据
    clearUserData: function() {
        this.removeStorage(CONFIG.STORAGE_KEYS.TOKEN);
        this.removeStorage(CONFIG.STORAGE_KEYS.USER_INFO);
    },
    
    // 格式化日期
    formatDate: function(date, format = 'YYYY-MM-DD') {
        if (!date) return '';
        
        const d = new Date(date);
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        
        return format
            .replace('YYYY', year)
            .replace('MM', month)
            .replace('DD', day);
    },
    
    // 格式化金额
    formatAmount: function(amount) {
        return parseFloat(amount).toFixed(2);
    },
    
    // 显示提示信息
    showToast: function(message, type = 'info') {
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.textContent = message;
        
        document.body.appendChild(toast);
        
        // 显示动画
        setTimeout(() => {
            toast.classList.add('show');
        }, 10);
        
        // 自动关闭
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => {
                document.body.removeChild(toast);
            }, 300);
        }, 3000);
    }
};