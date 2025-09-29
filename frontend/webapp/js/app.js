// 应用入口文件
document.addEventListener('DOMContentLoaded', function() {
    // 初始化路由
    Router.init();
    
    // 显示加载提示
    Utils.showLoading();
    
    // 检查登录状态
    const token = Utils.getToken();
    if (token) {
        // 已登录，获取用户信息
        API.getUserInfo().then(result => {
            if (result && result.data) {
                Utils.setUserInfo(result.data);
            } else {
                // 用户信息获取失败，清除登录状态
                Utils.clearUserData();
                Router.navigate('/login');
            }
            Utils.hideLoading();
        }).catch(() => {
            // 请求失败，清除登录状态
            Utils.clearUserData();
            Router.navigate('/login');
            Utils.hideLoading();
        });
    } else {
        // 未登录，跳转到登录页
        Router.navigate('/login');
        Utils.hideLoading();
    }
    
    // 添加全局事件处理
    document.addEventListener('click', function(e) {
        // 处理返回按钮点击
        if (e.target.classList.contains('back-btn')) {
            e.preventDefault();
            window.history.back();
        }
    });
});