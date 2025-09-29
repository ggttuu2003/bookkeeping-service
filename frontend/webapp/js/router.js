// 路由管理
const Router = {
    // 当前路由
    currentRoute: null,
    
    // 路由配置
    routes: {
        '/': {
            template: '/pages/home.html',
            controller: function() {
                // 首页控制器
                const userInfo = Utils.getUserInfo();
                document.querySelector('.welcome').textContent = `您好，${userInfo?.nickname || '用户'}`;
                
                // 加载月度统计
                this.loadMonthlyStats();
                
                // 加载最近交易
                this.loadRecentTransactions();
            },
            
            // 加载月度统计
            loadMonthlyStats: async function() {
                const date = new Date();
                const year = date.getFullYear();
                const month = date.getMonth() + 1;
                
                const result = await API.getMonthlyStats(year, CONFIG.DEFAULT_BOOK_ID);
                if (result && result.data) {
                    const monthData = result.data.find(item => item.month === month);
                    if (monthData) {
                        document.querySelector('.summary-item:nth-child(1) .amount').textContent = 
                            `￥${Utils.formatAmount(monthData.expense)}`;
                        document.querySelector('.summary-item:nth-child(2) .amount').textContent = 
                            `￥${Utils.formatAmount(monthData.income)}`;
                    }
                }
            },
            
            // 加载最近交易
            loadRecentTransactions: async function() {
                const result = await API.getTransactions({
                    bookId: CONFIG.DEFAULT_BOOK_ID,
                    page: 1,
                    size: 5
                });
                
                const transactionList = document.querySelector('.transaction-list');
                
                if (result && result.data && result.data.records) {
                    const transactions = result.data.records;
                    
                    if (transactions.length > 0) {
                        let html = '';
                        
                        transactions.forEach(item => {
                            html += `
                                <div class="transaction-item" data-id="${item.id}">
                                    <div class="transaction-info">
                                        <div class="transaction-category">${item.categoryName}</div>
                                        <div class="transaction-desc">${item.description || '无描述'}</div>
                                        <div class="transaction-time">${item.transactionTime}</div>
                                    </div>
                                    <div class="transaction-amount ${item.type === 1 ? 'income' : 'expense'}">
                                        ${item.type === 1 ? '+' : '-'}￥${Utils.formatAmount(item.amount)}
                                    </div>
                                </div>
                            `;
                        });
                        
                        transactionList.innerHTML = html;
                        
                        // 绑定点击事件
                        document.querySelectorAll('.transaction-item').forEach(item => {
                            item.addEventListener('click', function() {
                                const id = this.getAttribute('data-id');
                                Router.navigate(`/transaction/detail?id=${id}`);
                            });
                        });
                    } else {
                        transactionList.innerHTML = '<div class="empty-tip">暂无交易记录</div>';
                    }
                } else {
                    transactionList.innerHTML = '<div class="empty-tip">暂无交易记录</div>';
                }
                
                // 绑定"查看更多"点击事件
                document.querySelector('.more').addEventListener('click', function() {
                    Router.navigate('/transaction');
                });
                
                // 绑定快捷操作点击事件
                document.querySelector('.action-btn:nth-child(1)').addEventListener('click', function() {
                    Router.navigate('/transaction/add');
                });
                
                document.querySelector('.action-btn:nth-child(2)').addEventListener('click', function() {
                    Router.navigate('/statistics');
                });
            }
        },
        
        '/login': {
            template: '/pages/login.html',
            controller: function() {
                // 登录页控制器
                document.querySelector('form').addEventListener('submit', async function(e) {
                    e.preventDefault();
                    
                    const username = document.getElementById('username').value;
                    const password = document.getElementById('password').value;
                    
                    if (!username || !password) {
                        Utils.showToast('请输入用户名和密码', 'error');
                        return;
                    }
                    
                    Utils.showLoading();
                    
                    const result = await API.login(username, password);
                    
                    Utils.hideLoading();
                    
                    if (result && result.data) {
                        Utils.setToken(result.data.token);
                        
                        // 获取用户信息
                        const userInfo = await API.getUserInfo();
                        if (userInfo && userInfo.data) {
                            Utils.setUserInfo(userInfo.data);
                        }
                        
                        Utils.showToast('登录成功', 'success');
                        Router.navigate('/');
                    }
                });
                
                // 绑定注册链接点击事件
                document.querySelector('.register-link').addEventListener('click', function(e) {
                    e.preventDefault();
                    Router.navigate('/register');
                });
            }
        },
        
        '/register': {
            template: '/pages/register.html',
            controller: function() {
                // 注册页控制器
                document.querySelector('form').addEventListener('submit', async function(e) {
                    e.preventDefault();
                    
                    const username = document.getElementById('username').value;
                    const password = document.getElementById('password').value;
                    const nickname = document.getElementById('nickname').value;
                    const phone = document.getElementById('phone').value;
                    
                    if (!username || !password) {
                        Utils.showToast('请输入用户名和密码', 'error');
                        return;
                    }
                    
                    Utils.showLoading();
                    
                    const result = await API.register({
                        username,
                        password,
                        nickname: nickname || username,
                        phone
                    });
                    
                    Utils.hideLoading();
                    
                    if (result && result.data) {
                        Utils.showToast('注册成功，请登录', 'success');
                        Router.navigate('/login');
                    }
                });
                
                // 绑定登录链接点击事件
                document.querySelector('.login-link').addEventListener('click', function(e) {
                    e.preventDefault();
                    Router.navigate('/login');
                });
            }
        },
        
        '/transaction': {
            template: '/pages/transaction.html',
            controller: function() {
                // 交易列表页控制器
                const date = new Date();
                let currentYear = date.getFullYear();
                let currentMonth = date.getMonth() + 1;
                let activeTab = 'all';
                let page = 1;
                let hasMore = true;
                
                // 初始化日期显示
                document.querySelector('.date-picker').textContent = 
                    `${currentYear}年${currentMonth}月 ▼`;
                
                // 加载交易记录
                this.loadTransactions(true);
                
                // 绑定日期选择器点击事件
                document.querySelector('.date-picker').addEventListener('click', function() {
                    // 简化实现，实际应该使用日期选择器组件
                    const month = prompt('请输入月份(1-12):', currentMonth);
                    if (month && !isNaN(month) && month >= 1 && month <= 12) {
                        currentMonth = parseInt(month);
                        document.querySelector('.date-picker').textContent = 
                            `${currentYear}年${currentMonth}月 ▼`;
                        page = 1;
                        hasMore = true;
                        this.loadTransactions(true);
                    }
                }.bind(this));
                
                // 绑定标签点击事件
                document.querySelectorAll('.tab').forEach(tab => {
                    tab.addEventListener('click', function() {
                        const tabType = this.getAttribute('data-tab');
                        document.querySelector('.tab.active').classList.remove('active');
                        this.classList.add('active');
                        activeTab = tabType;
                        page = 1;
                        hasMore = true;
                        this.loadTransactions(true);
                    }.bind(this));
                }, this);
                
                // 绑定添加按钮点击事件
                document.querySelector('.add-btn').addEventListener('click', function() {
                    Router.navigate('/transaction/add');
                });
                
                // 绑定滚动加载更多
                document.getElementById('content').addEventListener('scroll', function() {
                    const scrollTop = this.scrollTop;
                    const scrollHeight = this.scrollHeight;
                    const clientHeight = this.clientHeight;
                    
                    if (scrollTop + clientHeight >= scrollHeight - 50 && hasMore) {
                        page++;
                        this.loadTransactions();
                    }
                }.bind(document.getElementById('content')));
            },
            
            // 加载交易记录
            loadTransactions: async function(refresh = false) {
                const currentYear = this.currentYear;
                const currentMonth = this.currentMonth;
                const activeTab = this.activeTab;
                const page = this.page;
                
                // 构建查询参数
                const params = {
                    bookId: CONFIG.DEFAULT_BOOK_ID,
                    page: page,
                    size: 20,
                    year: currentYear,
                    month: currentMonth
                };
                
                // 根据标签筛选交易类型
                if (activeTab === 'expense') {
                    params.type = 2; // 支出
                } else if (activeTab === 'income') {
                    params.type = 1; // 收入
                }
                
                Utils.showLoading();
                
                const result = await API.getTransactions(params);
                
                Utils.hideLoading();
                
                if (result && result.data) {
                    const newTransactions = result.data.records || [];
                    this.hasMore = newTransactions.length === 20;
                    
                    const transactionList = document.querySelector('.transaction-list');
                    
                    if (refresh) {
                        transactionList.innerHTML = '';
                    }
                    
                    if (newTransactions.length > 0) {
                        let html = '';
                        
                        newTransactions.forEach(item => {
                            html += `
                                <div class="transaction-item" data-id="${item.id}">
                                    <div class="transaction-info">
                                        <div class="transaction-category">${item.categoryName}</div>
                                        <div class="transaction-desc">${item.description || '无描述'}</div>
                                        <div class="transaction-time">${item.transactionTime}</div>
                                    </div>
                                    <div class="transaction-amount ${item.type === 1 ? 'income' : 'expense'}">
                                        ${item.type === 1 ? '+' : '-'}￥${Utils.formatAmount(item.amount)}
                                    </div>
                                </div>
                            `;
                        });
                        
                        if (refresh) {
                            transactionList.innerHTML = html;
                        } else {
                            transactionList.insertAdjacentHTML('beforeend', html);
                        }
                        
                        // 绑定点击事件
                        document.querySelectorAll('.transaction-item').forEach(item => {
                            item.addEventListener('click', function() {
                                const id = this.getAttribute('data-id');
                                Router.navigate(`/transaction/detail?id=${id}`);
                            });
                        });
                    } else if (refresh) {
                        transactionList.innerHTML = '<div class="empty-tip">暂无交易记录</div>';
                    }
                    
                    // 计算总收支
                    this.calculateTotal();
                }
            },
            
            // 计算总收支
            calculateTotal: function() {
                const transactions = Array.from(document.querySelectorAll('.transaction-item')).map(item => {
                    const amountEl = item.querySelector('.transaction-amount');
                    const isIncome = amountEl.classList.contains('income');
                    const amount = parseFloat(amountEl.textContent.replace(/[^0-9.]/g, ''));
                    
                    return {
                        type: isIncome ? 1 : 2,
                        amount: amount
                    };
                });
                
                let totalExpense = 0;
                let totalIncome = 0;
                
                transactions.forEach(item => {
                    if (item.type === 1) { // 收入
                        totalIncome += item.amount;
                    } else { // 支出
                        totalExpense += item.amount;
                    }
                });
                
                document.querySelector('.summary-item:nth-child(1) .amount').textContent = 
                    `￥${Utils.formatAmount(totalExpense)}`;
                document.querySelector('.summary-item:nth-child(2) .amount').textContent = 
                    `￥${Utils.formatAmount(totalIncome)}`;
            }
        }
    },
    
    // 初始化路由
    init: function() {
        // 监听哈希变化
        window.addEventListener('hashchange', this.handleRouteChange.bind(this));
        
        // 初始加载
        this.handleRouteChange();
        
        // 绑定底部导航点击事件
        document.querySelectorAll('.nav-item').forEach(item => {
            item.addEventListener('click', function(e) {
                e.preventDefault();
                const href = this.getAttribute('href');
                Router.navigate(href.substring(1)); // 去掉开头的#
            });
        });
    },
    
    // 处理路由变化
    handleRouteChange: async function() {
        Utils.showLoading();
        
        // 获取当前哈希路径
        let path = window.location.hash.substring(1) || '/';
        
        // 处理查询参数
        const queryIndex = path.indexOf('?');
        let query = {};
        
        if (queryIndex !== -1) {
            const queryString = path.substring(queryIndex + 1);
            path = path.substring(0, queryIndex);
            
            queryString.split('&').forEach(item => {
                const [key, value] = item.split('=');
                query[key] = decodeURIComponent(value);
            });
        }
        
        // 检查是否需要登录
        const token = Utils.getToken();
        if (!token && path !== '/login' && path !== '/register') {
            this.navigate('/login');
            return;
        }
        
        // 查找路由配置
        let route = this.routes[path];
        
        // 如果找不到路由，使用首页
        if (!route) {
            route = this.routes['/'];
            path = '/';
        }
        
        this.currentRoute = {
            path,
            query
        };
        
        // 加载模板
        try {
            const response = await fetch(route.template);
            const html = await response.text();
            
            document.getElementById('content').innerHTML = html;
            
            // 更新底部导航激活状态
            document.querySelectorAll('.nav-item').forEach(item => {
                item.classList.remove('active');
            });
            
            // 根据路径前缀激活对应的导航项
            let navPath = path;
            if (path.startsWith('/transaction/')) {
                navPath = '/transaction';
            } else if (path.startsWith('/statistics/')) {
                navPath = '/statistics';
            } else if (path.startsWith('/profile/')) {
                navPath = '/profile';
            }
            
            const activeNav = document.querySelector(`.nav-item[href="#${navPath}"]`);
            if (activeNav) {
                activeNav.classList.add('active');
            }
            
            // 执行控制器
            if (route.controller) {
                route.controller.call({
                    ...route,
                    currentYear: this.currentYear,
                    currentMonth: this.currentMonth,
                    activeTab: this.activeTab,
                    page: this.page,
                    hasMore: this.hasMore
                });
            }
        } catch (error) {
            console.error('加载模板失败', error);
            document.getElementById('content').innerHTML = '<div class="error-tip">页面加载失败</div>';
        }
        
        Utils.hideLoading();
    },
    
    // 导航到指定路径
    navigate: function(path) {
        window.location.hash = `#${path}`;
    }
};