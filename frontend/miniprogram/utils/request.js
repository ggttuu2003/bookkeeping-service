// 网络请求工具类

/**
 * 封装网络请求
 * @param {Object} options - 请求选项
 */
const request = (options) => {
  const maxRetries = options.retries || 0;
  const retryDelay = options.retryDelay || 1000;

  const makeRequest = (retryCount = 0) => {
    return new Promise((resolve, reject) => {
      // 构建请求头
      const header = {
        'Content-Type': 'application/json'
      };

      // 暂时不使用认证token

      // 安全获取 app 实例和 baseUrl
      let baseUrl = 'http://localhost:8081/api'; // 默认值
      try {
        const app = getApp();
        if (app && app.globalData && app.globalData.baseUrl) {
          baseUrl = app.globalData.baseUrl;
        }
      } catch (error) {
        console.warn('无法获取 app 实例，使用默认 baseUrl:', error);
      }

      console.log(`请求: ${options.method || 'GET'} ${options.url}${retryCount > 0 ? ` (重试 ${retryCount}/${maxRetries})` : ''}`);

      wx.request({
        url: `${baseUrl}${options.url}`,
        method: options.method || 'GET',
        data: options.data || {},
        header: header,
        success: (res) => {
          console.log(`请求成功: ${options.url}`, res.statusCode);

          // 处理HTTP状态码
          if (res.statusCode >= 200 && res.statusCode < 300) {
            // 请求成功，判断业务状态码
            if (res.data && res.data.code === 200) {
              resolve(res.data);
            } else {
              // 业务错误
              const errorMessage = res.data?.message || '请求失败';
              console.error(`业务错误: ${options.url}`, res.data);

              if (!options.hideErrorToast) {
                wx.showToast({
                  title: errorMessage,
                  icon: 'none'
                });
              }
              reject(res.data || { code: -1, message: errorMessage });
            }
          } else {
            // HTTP状态码错误
            const errorMessage = `服务器错误 (${res.statusCode})`;
            console.error(`HTTP错误: ${options.url}`, res.statusCode);

            if (retryCount < maxRetries) {
              console.log(`${retryDelay}ms后重试...`);
              setTimeout(() => {
                makeRequest(retryCount + 1).then(resolve).catch(reject);
              }, retryDelay);
            } else {
              if (!options.hideErrorToast) {
                wx.showToast({
                  title: errorMessage,
                  icon: 'none'
                });
              }
              reject({ code: res.statusCode, message: errorMessage });
            }
          }
        },
        fail: (err) => {
          console.error(`网络请求失败: ${options.url}`, err);

          // 网络错误，可以重试
          if (retryCount < maxRetries) {
            console.log(`${retryDelay}ms后重试...`);
            setTimeout(() => {
              makeRequest(retryCount + 1).then(resolve).catch(reject);
            }, retryDelay);
          } else {
            if (!options.hideErrorToast) {
              wx.showToast({
                title: '网络请求失败',
                icon: 'none'
              });
            }
            reject({ code: -1, message: '网络请求失败', error: err });
          }
        }
      });
    });
  };

  return makeRequest();
};


// 导出请求方法
module.exports = {
  // 直接调用request方法
  request,

  get: (url, data, options = {}) => {
    return request({
      url,
      method: 'GET',
      data,
      ...options
    });
  },
  post: (url, data, options = {}) => {
    return request({
      url,
      method: 'POST',
      data,
      ...options
    });
  },
  put: (url, data, options = {}) => {
    return request({
      url,
      method: 'PUT',
      data,
      ...options
    });
  },
  delete: (url, data, options = {}) => {
    return request({
      url,
      method: 'DELETE',
      data,
      ...options
    });
  }
};