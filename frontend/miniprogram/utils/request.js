// 网络请求工具类
const app = getApp();

/**
 * 封装网络请求
 * @param {Object} options - 请求选项
 */
const request = (options) => {
  return new Promise((resolve, reject) => {
    // 获取token
    const token = wx.getStorageSync('token');
    
    // 构建请求头
    const header = {
      'Content-Type': 'application/json'
    };
    
    // 如果有token，添加到请求头
    if (token) {
      header['Authorization'] = `Bearer ${token}`;
    }
    
    wx.request({
      url: `${app.globalData.baseUrl}${options.url}`,
      method: options.method || 'GET',
      data: options.data || {},
      header: header,
      success: (res) => {
        // 请求成功，判断业务状态码
        if (res.data.code === 200) {
          resolve(res.data);
        } else if (res.data.code === 401) {
          // token失效，跳转到登录页
          wx.removeStorageSync('token');
          app.globalData.isLoggedIn = false;
          wx.showToast({
            title: '登录已过期，请重新登录',
            icon: 'none'
          });
          wx.navigateTo({
            url: '/pages/login/login'
          });
          reject(res.data);
        } else {
          // 其他业务错误
          wx.showToast({
            title: res.data.message || '请求失败',
            icon: 'none'
          });
          reject(res.data);
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
};

// 导出请求方法
module.exports = {
  get: (url, data) => {
    return request({
      url,
      method: 'GET',
      data
    });
  },
  post: (url, data) => {
    return request({
      url,
      method: 'POST',
      data
    });
  },
  put: (url, data) => {
    return request({
      url,
      method: 'PUT',
      data
    });
  },
  delete: (url, data) => {
    return request({
      url,
      method: 'DELETE',
      data
    });
  }
};