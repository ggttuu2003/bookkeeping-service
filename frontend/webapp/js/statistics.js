// 统计分析页面控制器
const StatisticsController = {
    // 初始化
    init: function() {
        const date = new Date();
        this.currentYear = date.getFullYear();
        this.currentMonth = date.getMonth() + 1;
        this.activeTab = 'overview';
        
        // 初始化日期显示
        document.querySelector('.date-picker').textContent = 
            `${this.currentYear}年${this.currentMonth}月 ▼`;
        
        // 绑定日期选择器点击事件
        document.querySelector('.date-picker').addEventListener('click', this.handleDatePicker.bind(this));
        
        // 绑定标签点击事件
        document.querySelectorAll('.tab').forEach(tab => {
            tab.addEventListener('click', this.handleTabClick.bind(this));
        });
        
        // 加载统计数据
        this.loadStatistics();
    },
    
    // 处理日期选择器点击
    handleDatePicker: function() {
        // 简化实现，实际应该使用日期选择器组件
        const month = prompt('请输入月份(1-12):', this.currentMonth);
        if (month && !isNaN(month) && month >= 1 && month <= 12) {
            this.currentMonth = parseInt(month);
            document.querySelector('.date-picker').textContent = 
                `${this.currentYear}年${this.currentMonth}月 ▼`;
            this.loadStatistics();
        }
    },
    
    // 处理标签点击
    handleTabClick: function(e) {
        const tabType = e.target.getAttribute('data-tab');
        document.querySelector('.tab.active').classList.remove('active');
        e.target.classList.add('active');
        this.activeTab = tabType;
        this.loadStatistics();
    },
    
    // 加载统计数据
    loadStatistics: function() {
        Utils.showLoading();
        
        // 根据当前标签加载不同的统计数据
        switch (this.activeTab) {
            case 'overview':
                this.loadOverview();
                break;
            case 'category':
                this.loadCategoryStats();
                break;
            case 'trend':
                this.loadTrendStats();
                break;
        }
    },
    
    // 加载概览数据
    loadOverview: async function() {
        const result = await API.getMonthlyStats(this.currentYear, CONFIG.DEFAULT_BOOK_ID);
        
        Utils.hideLoading();
        
        if (result && result.data) {
            const monthData = result.data.find(item => item.month === this.currentMonth);
            
            if (monthData) {
                // 更新收支数据
                document.querySelector('.summary-item:nth-child(1) .amount').textContent = 
                    `￥${Utils.formatAmount(monthData.expense)}`;
                document.querySelector('.summary-item:nth-child(2) .amount').textContent = 
                    `￥${Utils.formatAmount(monthData.income)}`;
                document.querySelector('.summary-item:nth-child(3) .amount').textContent = 
                    `￥${Utils.formatAmount(monthData.income - monthData.expense)}`;
                
                // 简单的概览图表
                const chartContainer = document.querySelector('.chart-container');
                
                // 计算比例
                const total = monthData.income + monthData.expense;
                const incomePercent = total > 0 ? (monthData.income / total * 100).toFixed(1) : 0;
                const expensePercent = total > 0 ? (monthData.expense / total * 100).toFixed(1) : 0;
                
                chartContainer.innerHTML = `
                    <div class="overview-chart">
                        <div class="chart-item">
                            <div class="chart-bar expense" style="height: ${expensePercent}%"></div>
                            <div class="chart-label">支出 ${expensePercent}%</div>
                        </div>
                        <div class="chart-item">
                            <div class="chart-bar income" style="height: ${incomePercent}%"></div>
                            <div class="chart-label">收入 ${incomePercent}%</div>
                        </div>
                    </div>
                `;
            } else {
                document.querySelector('.chart-container').innerHTML = '<div class="empty-tip">暂无数据</div>';
            }
        } else {
            document.querySelector('.chart-container').innerHTML = '<div class="empty-tip">暂无数据</div>';
        }
    },
    
    // 加载分类统计
    loadCategoryStats: async function() {
        const params = {
            bookId: CONFIG.DEFAULT_BOOK_ID,
            year: this.currentYear,
            month: this.currentMonth
        };
        
        const result = await API.getCategoryStats(params);
        
        Utils.hideLoading();
        
        if (result && result.data && result.data.length > 0) {
            const chartContainer = document.querySelector('.chart-container');
            
            // 分类数据
            const categories = result.data;
            
            // 计算总金额
            let totalAmount = 0;
            categories.forEach(item => {
                totalAmount += item.amount;
            });
            
            // 生成分类图表
            let html = '<div class="category-chart">';
            
            categories.forEach(item => {
                const percent = (item.amount / totalAmount * 100).toFixed(1);
                
                html += `
                    <div class="category-item">
                        <div class="category-info">
                            <div class="category-name">${item.categoryName}</div>
                            <div class="category-amount">￥${Utils.formatAmount(item.amount)}</div>
                        </div>
                        <div class="category-bar-container">
                            <div class="category-bar" style="width: ${percent}%"></div>
                            <div class="category-percent">${percent}%</div>
                        </div>
                    </div>
                `;
            });
            
            html += '</div>';
            chartContainer.innerHTML = html;
        } else {
            document.querySelector('.chart-container').innerHTML = '<div class="empty-tip">暂无数据</div>';
        }
    },
    
    // 加载趋势统计
    loadTrendStats: async function() {
        const result = await API.getMonthlyStats(this.currentYear, CONFIG.DEFAULT_BOOK_ID);
        
        Utils.hideLoading();
        
        if (result && result.data && result.data.length > 0) {
            const chartContainer = document.querySelector('.chart-container');
            
            // 月度数据
            const monthlyData = result.data.sort((a, b) => a.month - b.month);
            
            // 找出最大值，用于计算图表高度
            let maxAmount = 0;
            monthlyData.forEach(item => {
                maxAmount = Math.max(maxAmount, item.income, item.expense);
            });
            
            // 生成趋势图表
            let html = '<div class="trend-chart">';
            
            // Y轴标签
            html += '<div class="y-axis">';
            const yLabels = 5; // Y轴标签数量
            for (let i = yLabels; i >= 0; i--) {
                const value = (maxAmount * i / yLabels).toFixed(0);
                html += `<div class="y-label">￥${Utils.formatAmount(value)}</div>`;
            }
            html += '</div>';
            
            // 图表主体
            html += '<div class="chart-body">';
            
            monthlyData.forEach(item => {
                const incomeHeight = maxAmount > 0 ? (item.income / maxAmount * 100) : 0;
                const expenseHeight = maxAmount > 0 ? (item.expense / maxAmount * 100) : 0;
                
                html += `
                    <div class="month-column">
                        <div class="bar-container">
                            <div class="bar income" style="height: ${incomeHeight}%" title="收入: ￥${Utils.formatAmount(item.income)}"></div>
                            <div class="bar expense" style="height: ${expenseHeight}%" title="支出: ￥${Utils.formatAmount(item.expense)}"></div>
                        </div>
                        <div class="month-label">${item.month}月</div>
                    </div>
                `;
            });
            
            html += '</div>'; // 图表主体结束
            
            // 图例
            html += `
                <div class="chart-legend">
                    <div class="legend-item">
                        <div class="legend-color income"></div>
                        <div class="legend-label">收入</div>
                    </div>
                    <div class="legend-item">
                        <div class="legend-color expense"></div>
                        <div class="legend-label">支出</div>
                    </div>
                </div>
            `;
            
            html += '</div>'; // 趋势图表结束
            
            chartContainer.innerHTML = html;
        } else {
            document.querySelector('.chart-container').innerHTML = '<div class="empty-tip">暂无数据</div>';
        }
    }
};

// 导出控制器
window.StatisticsController = StatisticsController;