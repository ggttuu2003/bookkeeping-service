// 交易管理页面控制器
const TransactionController = {
    // 初始化
    init: function() {
        const date = new Date();
        this.currentYear = date.getFullYear();
        this.currentMonth = date.getMonth() + 1;
        this.activeTab = 'all';
        this.page = 1;
        this.hasMore = true;
        
        // 初始化日期显示
        document.querySelector('.date-picker').textContent = 
            `${this.currentYear}年${this.currentMonth}月 ▼`;
        
        // 绑定日期选择器点击事件
        document.querySelector('.date-picker').addEventListener('click', this.handleDatePicker.bind(this));
        
        // 绑定标签点击事件
        document.querySelectorAll('.tab').forEach(tab => {
            tab.addEventListener('click', this.handleTabClick.bind(this));
        });
        
        // 绑定添加按钮点击事件
        document.querySelector('.add-btn').addEventListener('click', this.handleAddClick.bind(this));
        
        // 绑定滚动加载更多
        document.getElementById('content').addEventListener('scroll', this.handleScroll.bind(this));
        
        // 加载交易记录
        this.loadTransactions(true);
    },
    
    // 处理日期选择器点击
    handleDatePicker: function() {
        // 简化实现，实际应该使用日期选择器组件
        const month = prompt('请输入月份(1-12):', this.currentMonth);
        if (month && !isNaN(month) && month >= 1 && month <= 12) {
            this.currentMonth = parseInt(month);
            document.querySelector('.date-picker').textContent = 
                `${this.currentYear}年${this.currentMonth}月 ▼`;
            this.page = 1;
            this.hasMore = true;
            this.loadTransactions(true);
        }
    },
    
    // 处理标签点击
    handleTabClick: function(e) {
        const tabType = e.target.getAttribute('data-tab');
        document.querySelector('.tab.active').classList.remove('active');
        e.target.classList.add('active');
        this.activeTab = tabType;
        this.page = 1;
        this.hasMore = true;
        this.loadTransactions(true);
    },
    
    // 处理添加按钮点击
    handleAddClick: function() {
        Router.navigate('/transaction/add');
    },
    
    // 处理滚动加载更多
    handleScroll: function() {
        const content = document.getElementById('content');
        const scrollTop = content.scrollTop;
        const scrollHeight = content.scrollHeight;
        const clientHeight = content.clientHeight;
        
        if (scrollTop + clientHeight >= scrollHeight - 50 && this.hasMore) {
            this.page++;
            this.loadTransactions();
        }
    },
    
    // 加载交易记录
    loadTransactions: async function(refresh = false) {
        // 构建查询参数
        const params = {
            bookId: CONFIG.DEFAULT_BOOK_ID,
            page: this.page,
            size: 20,
            year: this.currentYear,
            month: this.currentMonth
        };
        
        // 根据标签筛选交易类型
        if (this.activeTab === 'expense') {
            params.type = 2; // 支出
        } else if (this.activeTab === 'income') {
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
                    item.addEventListener('click', this.handleTransactionClick.bind(this));
                });
            } else if (refresh) {
                transactionList.innerHTML = '<div class="empty-tip">暂无交易记录</div>';
            }
            
            // 计算总收支
            this.calculateTotal();
        }
    },
    
    // 处理交易项点击
    handleTransactionClick: function(e) {
        const id = e.currentTarget.getAttribute('data-id');
        Router.navigate(`/transaction/detail?id=${id}`);
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
};

// 交易添加页面控制器
const TransactionAddController = {
    // 初始化
    init: function() {
        this.transactionType = 2; // 默认支出
        
        // 设置默认日期为今天
        const today = new Date();
        const dateStr = today.toISOString().split('T')[0];
        document.getElementById('date').value = dateStr;
        
        // 绑定类型选择器点击事件
        document.querySelectorAll('.type-btn').forEach(btn => {
            btn.addEventListener('click', this.handleTypeClick.bind(this));
        });
        
        // 绑定表单提交事件
        document.querySelector('form').addEventListener('submit', this.handleSubmit.bind(this));
        
        // 加载分类
        this.loadCategories();
    },
    
    // 处理类型点击
    handleTypeClick: function(e) {
        const type = parseInt(e.target.getAttribute('data-type'));
        this.transactionType = type;
        
        document.querySelector('.type-btn.active').classList.remove('active');
        e.target.classList.add('active');
        
        // 重新加载对应类型的分类
        this.loadCategories();
    },
    
    // 加载分类
    loadCategories: async function() {
        Utils.showLoading();
        
        const result = await API.getCategories(this.transactionType);
        
        Utils.hideLoading();
        
        if (result && result.data) {
            const categories = result.data;
            const categorySelect = document.getElementById('category');
            
            // 清空现有选项
            categorySelect.innerHTML = '<option value="">请选择分类</option>';
            
            // 添加新选项
            categories.forEach(item => {
                const option = document.createElement('option');
                option.value = item.id;
                option.textContent = item.name;
                categorySelect.appendChild(option);
            });
        }
    },
    
    // 处理表单提交
    handleSubmit: async function(e) {
        e.preventDefault();
        
        const amount = document.getElementById('amount').value;
        const categoryId = document.getElementById('category').value;
        const date = document.getElementById('date').value;
        const description = document.getElementById('description').value;
        
        if (!amount || !categoryId || !date) {
            Utils.showToast('请填写完整信息', 'error');
            return;
        }
        
        const data = {
            type: this.transactionType,
            amount: parseFloat(amount),
            categoryId: parseInt(categoryId),
            bookId: CONFIG.DEFAULT_BOOK_ID,
            transactionTime: date,
            description: description
        };
        
        Utils.showLoading();
        
        const result = await API.addTransaction(data);
        
        Utils.hideLoading();
        
        if (result && result.data) {
            Utils.showToast('添加成功', 'success');
            
            // 返回上一页
            setTimeout(() => {
                window.history.back();
            }, 1000);
        }
    }
};

// 导出控制器
window.TransactionController = TransactionController;
window.TransactionAddController = TransactionAddController;