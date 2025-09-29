# 智能记账小程序 API 文档

## 基础信息

- 基础路径: `/api`
- 请求格式: JSON
- 响应格式: JSON
- 认证方式: JWT Token (在请求头中添加 `Authorization: Bearer {token}`)

## 通用响应格式

```json
{
  "code": 200,          // 状态码，200表示成功，非200表示失败
  "message": "success", // 响应消息
  "data": {}            // 响应数据，可能是对象、数组或null
}
```

## 错误码说明

| 错误码 | 说明 |
| ------ | ---- |
| 200    | 成功 |
| 400    | 参数错误 |
| 401    | 未登录 |
| 403    | 无权限 |
| 404    | 资源不存在 |
| 500    | 服务器内部错误 |

## API 接口列表

### 1. 用户认证

#### 1.1 用户注册

- **URL**: `/auth/register`
- **方法**: POST
- **描述**: 注册新用户
- **请求参数**:

```json
{
  "username": "user123",     // 用户名，必填，长度5-20
  "password": "password123", // 密码，必填，长度6-20
  "nickname": "张三",        // 昵称，可选
  "mobile": "13800138000",   // 手机号，可选
  "email": "user@example.com" // 邮箱，可选
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": 10001,
    "username": "user123",
    "nickname": "张三",
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
}
```

#### 1.2 用户登录

- **URL**: `/auth/login`
- **方法**: POST
- **描述**: 用户登录获取token
- **请求参数**:

```json
{
  "username": "user123",     // 用户名，必填
  "password": "password123"  // 密码，必填
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": 10001,
    "username": "user123",
    "nickname": "张三",
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 86400       // token有效期，单位秒
  }
}
```

#### 1.3 获取当前用户信息

- **URL**: `/auth/info`
- **方法**: GET
- **描述**: 获取当前登录用户信息
- **请求参数**: 无
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": 10001,
    "username": "user123",
    "nickname": "张三",
    "avatar": "https://example.com/avatar.jpg",
    "mobile": "13800138000",
    "email": "user@example.com",
    "gender": 1,             // 1-男，2-女，0-未知
    "roles": ["ROLE_USER"]   // 用户角色列表
  }
}
```

#### 1.4 更新用户信息

- **URL**: `/auth/info`
- **方法**: PUT
- **描述**: 更新当前登录用户信息
- **请求参数**:

```json
{
  "nickname": "李四",        // 昵称，可选
  "avatar": "https://example.com/new-avatar.jpg", // 头像URL，可选
  "mobile": "13900139000",   // 手机号，可选
  "email": "new@example.com", // 邮箱，可选
  "gender": 1                // 性别，可选，1-男，2-女，0-未知
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

### 2. 账本管理

#### 2.1 创建账本

- **URL**: `/books`
- **方法**: POST
- **描述**: 创建新账本
- **请求参数**:

```json
{
  "name": "日常开销",        // 账本名称，必填
  "description": "记录日常生活开销", // 账本描述，可选
  "icon": "daily_expense",   // 账本图标，可选
  "color": "#4CAF50"         // 账本颜色，可选
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "bookId": 1001,
    "name": "日常开销",
    "description": "记录日常生活开销",
    "icon": "daily_expense",
    "color": "#4CAF50",
    "createTime": "2023-06-01T10:00:00"
  }
}
```

#### 2.2 获取账本列表

- **URL**: `/books`
- **方法**: GET
- **描述**: 获取当前用户的账本列表
- **请求参数**: 无
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "bookId": 1001,
      "name": "日常开销",
      "description": "记录日常生活开销",
      "icon": "daily_expense",
      "color": "#4CAF50",
      "createTime": "2023-06-01T10:00:00",
      "memberCount": 1,
      "transactionCount": 56,
      "totalIncome": 5000.00,
      "totalExpense": 3200.50
    },
    {
      "bookId": 1002,
      "name": "旅行账本",
      "description": "记录旅行花销",
      "icon": "travel",
      "color": "#2196F3",
      "createTime": "2023-06-15T14:30:00",
      "memberCount": 2,
      "transactionCount": 24,
      "totalIncome": 0.00,
      "totalExpense": 4500.75
    }
  ]
}
```

#### 2.3 获取账本详情

- **URL**: `/books/{bookId}`
- **方法**: GET
- **描述**: 获取指定账本的详细信息
- **路径参数**:
  - `bookId`: 账本ID
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "bookId": 1001,
    "name": "日常开销",
    "description": "记录日常生活开销",
    "icon": "daily_expense",
    "color": "#4CAF50",
    "createTime": "2023-06-01T10:00:00",
    "owner": {
      "userId": 10001,
      "username": "user123",
      "nickname": "张三"
    },
    "members": [
      {
        "userId": 10001,
        "username": "user123",
        "nickname": "张三",
        "role": "owner"
      }
    ],
    "statistics": {
      "transactionCount": 56,
      "totalIncome": 5000.00,
      "totalExpense": 3200.50,
      "balance": 1799.50
    }
  }
}
```

#### 2.4 更新账本信息

- **URL**: `/books/{bookId}`
- **方法**: PUT
- **描述**: 更新指定账本的信息
- **路径参数**:
  - `bookId`: 账本ID
- **请求参数**:

```json
{
  "name": "家庭开销",        // 账本名称，可选
  "description": "记录家庭日常开销", // 账本描述，可选
  "icon": "family",          // 账本图标，可选
  "color": "#FF9800"         // 账本颜色，可选
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

#### 2.5 删除账本

- **URL**: `/books/{bookId}`
- **方法**: DELETE
- **描述**: 删除指定账本
- **路径参数**:
  - `bookId`: 账本ID
- **响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 3. 交易记录管理

#### 3.1 创建交易记录

- **URL**: `/transactions`
- **方法**: POST
- **描述**: 创建新的交易记录
- **请求参数**:

```json
{
  "bookId": 1001,            // 账本ID，必填
  "categoryId": 8,           // 分类ID，必填
  "amount": 125.50,          // 金额，必填，精确到小数点后2位
  "type": 2,                 // 交易类型，必填，1-收入，2-支出
  "transactionTime": "2023-06-20T12:30:00", // 交易时间，必填
  "description": "超市购物",  // 交易描述，可选
  "location": "家乐福超市",   // 交易地点，可选
  "imageUrl": "https://example.com/receipt.jpg", // 票据图片URL，可选
  "tagIds": [5, 8]           // 标签ID列表，可选
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "transactionId": 10001,
    "bookId": 1001,
    "categoryId": 8,
    "amount": 125.50,
    "type": 2,
    "transactionTime": "2023-06-20T12:30:00",
    "description": "超市购物",
    "location": "家乐福超市",
    "imageUrl": "https://example.com/receipt.jpg",
    "createTime": "2023-06-20T14:00:00"
  }
}
```

#### 3.2 分页查询交易记录

- **URL**: `/transactions`
- **方法**: GET
- **描述**: 分页查询交易记录
- **查询参数**:
  - `bookId`: 账本ID，必填
  - `type`: 交易类型，可选，1-收入，2-支出
  - `categoryId`: 分类ID，可选
  - `tagId`: 标签ID，可选
  - `startDate`: 开始日期，可选，格式：yyyy-MM-dd
  - `endDate`: 结束日期，可选，格式：yyyy-MM-dd
  - `minAmount`: 最小金额，可选
  - `maxAmount`: 最大金额，可选
  - `keyword`: 关键词，可选，搜索描述和地点
  - `page`: 页码，可选，默认1
  - `size`: 每页条数，可选，默认20
  - `sort`: 排序字段，可选，默认transactionTime
  - `order`: 排序方向，可选，asc或desc，默认desc
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 56,
    "pages": 3,
    "current": 1,
    "size": 20,
    "records": [
      {
        "transactionId": 10001,
        "bookId": 1001,
        "categoryId": 8,
        "categoryName": "购物",
        "amount": 125.50,
        "type": 2,
        "transactionTime": "2023-06-20T12:30:00",
        "description": "超市购物",
        "location": "家乐福超市",
        "imageUrl": "https://example.com/receipt.jpg",
        "tags": [
          {
            "tagId": 5,
            "name": "日用品",
            "color": "#9C27B0"
          },
          {
            "tagId": 8,
            "name": "食品",
            "color": "#FF5722"
          }
        ]
      },
      // 更多记录...
    ]
  }
}
```

#### 3.3 获取交易记录详情

- **URL**: `/transactions/{transactionId}`
- **方法**: GET
- **描述**: 获取指定交易记录的详细信息
- **路径参数**:
  - `transactionId`: 交易记录ID
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "transactionId": 10001,
    "bookId": 1001,
    "bookName": "日常开销",
    "userId": 10001,
    "username": "user123",
    "nickname": "张三",
    "categoryId": 8,
    "categoryName": "购物",
    "amount": 125.50,
    "type": 2,
    "transactionTime": "2023-06-20T12:30:00",
    "description": "超市购物",
    "location": "家乐福超市",
    "imageUrl": "https://example.com/receipt.jpg",
    "createTime": "2023-06-20T14:00:00",
    "updateTime": "2023-06-20T14:00:00",
    "tags": [
      {
        "tagId": 5,
        "name": "日用品",
        "color": "#9C27B0"
      },
      {
        "tagId": 8,
        "name": "食品",
        "color": "#FF5722"
      }
    ]
  }
}
```

#### 3.4 更新交易记录

- **URL**: `/transactions/{transactionId}`
- **方法**: PUT
- **描述**: 更新指定交易记录
- **路径参数**:
  - `transactionId`: 交易记录ID
- **请求参数**:

```json
{
  "categoryId": 9,           // 分类ID，可选
  "amount": 135.80,          // 金额，可选
  "transactionTime": "2023-06-20T13:00:00", // 交易时间，可选
  "description": "超市购物和日用品", // 交易描述，可选
  "location": "沃尔玛超市",   // 交易地点，可选
  "imageUrl": "https://example.com/new-receipt.jpg", // 票据图片URL，可选
  "tagIds": [5, 8, 10]       // 标签ID列表，可选，会覆盖原有标签
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

#### 3.5 删除交易记录

- **URL**: `/transactions/{transactionId}`
- **方法**: DELETE
- **描述**: 删除指定交易记录
- **路径参数**:
  - `transactionId`: 交易记录ID
- **响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

#### 3.6 OCR识别创建交易

- **URL**: `/transactions/ocr`
- **方法**: POST
- **描述**: 通过OCR识别票据创建交易记录
- **请求参数**:

```json
{
  "bookId": 1001,            // 账本ID，必填
  "imageBase64": "data:image/jpeg;base64,/9j/4AAQSkZJRg...", // 图片Base64编码，必填
  "type": 2                  // 交易类型，必填，1-收入，2-支出
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "识别成功",
  "data": {
    "categoryId": 8,
    "categoryName": "购物",
    "amount": 125.50,
    "transactionTime": "2023-06-20T12:30:00",
    "description": "超市购物",
    "location": "家乐福超市"
  }
}
```

### 4. 分类管理

#### 4.1 获取分类列表

- **URL**: `/categories`
- **方法**: GET
- **描述**: 获取分类列表
- **查询参数**:
  - `type`: 交易类型，可选，1-收入，2-支出
  - `isSystem`: 是否系统预设，可选，true或false
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "categoryId": 1,
      "name": "工资",
      "icon": "salary",
      "color": "#4CAF50",
      "type": 1,
      "isSystem": true
    },
    {
      "categoryId": 7,
      "name": "餐饮",
      "icon": "food",
      "color": "#FF5722",
      "type": 2,
      "isSystem": true
    },
    {
      "categoryId": 8,
      "name": "购物",
      "icon": "shopping",
      "color": "#9C27B0",
      "type": 2,
      "isSystem": true
    }
    // 更多分类...
  ]
}
```

#### 4.2 创建自定义分类

- **URL**: `/categories`
- **方法**: POST
- **描述**: 创建自定义分类
- **请求参数**:

```json
{
  "name": "投资",             // 分类名称，必填
  "icon": "investment",      // 分类图标，可选
  "color": "#2196F3",        // 分类颜色，可选
  "type": 1                  // 交易类型，必填，1-收入，2-支出
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "categoryId": 20,
    "name": "投资",
    "icon": "investment",
    "color": "#2196F3",
    "type": 1,
    "isSystem": false
  }
}
```

### 5. 标签管理

#### 5.1 获取标签列表

- **URL**: `/tags`
- **方法**: GET
- **描述**: 获取标签列表
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "tagId": 1,
      "name": "必需品",
      "color": "#4CAF50"
    },
    {
      "tagId": 2,
      "name": "娱乐",
      "color": "#2196F3"
    },
    {
      "tagId": 5,
      "name": "日用品",
      "color": "#9C27B0"
    }
    // 更多标签...
  ]
}
```

#### 5.2 创建标签

- **URL**: `/tags`
- **方法**: POST
- **描述**: 创建新标签
- **请求参数**:

```json
{
  "name": "旅行",             // 标签名称，必填
  "color": "#FF9800"         // 标签颜色，可选
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "tagId": 10,
    "name": "旅行",
    "color": "#FF9800"
  }
}
```

### 6. 预算管理

#### 6.1 创建预算

- **URL**: `/budgets`
- **方法**: POST
- **描述**: 创建新预算
- **请求参数**:

```json
{
  "bookId": 1001,            // 账本ID，必填
  "amount": 5000.00,         // 预算金额，必填
  "year": 2023,              // 年份，必填
  "month": 7,                // 月份，必填，1-12
  "categoryIds": [7, 8, 9]   // 分类ID列表，可选，不填表示所有支出分类
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "budgetId": 101,
    "bookId": 1001,
    "amount": 5000.00,
    "year": 2023,
    "month": 7,
    "createTime": "2023-06-25T10:00:00"
  }
}
```

#### 6.2 获取预算列表

- **URL**: `/budgets`
- **方法**: GET
- **描述**: 获取预算列表
- **查询参数**:
  - `bookId`: 账本ID，必填
  - `year`: 年份，可选
  - `month`: 月份，可选，1-12
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "budgetId": 101,
      "bookId": 1001,
      "amount": 5000.00,
      "year": 2023,
      "month": 7,
      "createTime": "2023-06-25T10:00:00",
      "categories": [
        {
          "categoryId": 7,
          "name": "餐饮"
        },
        {
          "categoryId": 8,
          "name": "购物"
        },
        {
          "categoryId": 9,
          "name": "交通"
        }
      ],
      "execution": {
        "actualExpense": 2500.50,
        "executionRate": 50.01,
        "remaining": 2499.50
      }
    }
    // 更多预算...
  ]
}
```

### 7. 统计分析

#### 7.1 获取每日收支统计

- **URL**: `/statistics/daily`
- **方法**: GET
- **描述**: 获取每日收支统计
- **查询参数**:
  - `bookId`: 账本ID，必填
  - `startDate`: 开始日期，必填，格式：yyyy-MM-dd
  - `endDate`: 结束日期，必填，格式：yyyy-MM-dd
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "date": "2023-06-01",
      "income": 0.00,
      "expense": 150.50,
      "net": -150.50
    },
    {
      "date": "2023-06-02",
      "income": 5000.00,
      "expense": 320.75,
      "net": 4679.25
    }
    // 更多日期...
  ]
}
```

#### 7.2 获取月度收支统计

- **URL**: `/statistics/monthly`
- **方法**: GET
- **描述**: 获取月度收支统计
- **查询参数**:
  - `bookId`: 账本ID，必填
  - `year`: 年份，必填
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "year": 2023,
      "month": 1,
      "income": 5000.00,
      "expense": 4200.50,
      "net": 799.50,
      "budgetAmount": 5000.00,
      "budgetExecutionRate": 84.01
    },
    {
      "year": 2023,
      "month": 2,
      "income": 5000.00,
      "expense": 3800.25,
      "net": 1199.75,
      "budgetAmount": 5000.00,
      "budgetExecutionRate": 76.01
    }
    // 更多月份...
  ]
}
```

#### 7.3 获取分类收支统计

- **URL**: `/statistics/category`
- **方法**: GET
- **描述**: 获取分类收支统计
- **查询参数**:
  - `bookId`: 账本ID，必填
  - `startDate`: 开始日期，必填，格式：yyyy-MM-dd
  - `endDate`: 结束日期，必填，格式：yyyy-MM-dd
  - `type`: 交易类型，必填，1-收入，2-支出
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "categoryId": 8,
      "categoryName": "购物",
      "amount": 1500.50,
      "percentage": 35.25,
      "count": 12
    },
    {
      "categoryId": 7,
      "categoryName": "餐饮",
      "amount": 1200.75,
      "percentage": 28.20,
      "count": 25
    },
    {
      "categoryId": 9,
      "categoryName": "交通",
      "amount": 800.25,
      "percentage": 18.80,
      "count": 18
    }
    // 更多分类...
  ]
}
```

#### 7.4 获取收支趋势分析

- **URL**: `/statistics/trend`
- **方法**: GET
- **描述**: 获取收支趋势分析
- **查询参数**:
  - `bookId`: 账本ID，必填
  - `months`: 分析月数，可选，默认6
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "months": ["2023-01", "2023-02", "2023-03", "2023-04", "2023-05", "2023-06"],
    "incomes": [5000.00, 5000.00, 5000.00, 5500.00, 5500.00, 5500.00],
    "expenses": [4200.50, 3800.25, 4100.75, 4300.50, 3900.25, 4200.50],
    "incomeGrowthRates": [null, 0.00, 0.00, 10.00, 0.00, 0.00],
    "expenseGrowthRates": [null, -9.53, 7.91, 4.87, -9.31, 7.70]
  }
}
```

#### 7.5 获取消费习惯分析

- **URL**: `/statistics/habits`
- **方法**: GET
- **描述**: 获取消费习惯分析
- **查询参数**:
  - `bookId`: 账本ID，必填
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "mostExpensiveDay": "SATURDAY",
    "mostExpensiveDayAmount": 1500.50,
    "expensesByTimeSlot": {
      "morning": 800.25,
      "afternoon": 1200.75,
      "evening": 1800.50,
      "night": 200.00
    },
    "avgTransactionsPerDay": 1.85,
    "avgAmountPerTransaction": 125.50
  }
}
```

#### 7.6 获取预算执行情况

- **URL**: `/statistics/budget`
- **方法**: GET
- **描述**: 获取预算执行情况
- **查询参数**:
  - `bookId`: 账本ID，必填
  - `year`: 年份，必填
  - `month`: 月份，必填，1-12
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "hasBudget": true,
    "budgetAmount": 5000.00,
    "actualExpense": 2500.50,
    "executionRate": 50.01,
    "remaining": 2499.50,
    "dailyRemainingBudget": 166.63,
    "categoryExpenses": {
      "餐饮": 800.25,
      "购物": 1200.75,
      "交通": 500.50
    }
  }
}
```

### 8. AI智能服务

#### 8.1 预测未来支出

- **URL**: `/ai/predict/expense`
- **方法**: GET
- **描述**: 预测未来支出
- **查询参数**:
  - `bookId`: 账本ID，必填
  - `categoryId`: 分类ID，可选
  - `days`: 预测天数，可选，默认30
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "predictionDate": "2023-07-01",
      "predictedAmount": 150.25,
      "confidence": 0.85,
      "type": 2
    },
    {
      "predictionDate": "2023-07-02",
      "predictedAmount": 120.50,
      "confidence": 0.75,
      "type": 2
    }
    // 更多预测...
  ]
}
```

#### 8.2 预测未来收入

- **URL**: `/ai/predict/income`
- **方法**: GET
- **描述**: 预测未来收入
- **查询参数**:
  - `bookId`: 账本ID，必填
  - `categoryId`: 分类ID，可选
  - `days`: 预测天数，可选，默认30
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "predictionDate": "2023-07-01",
      "predictedAmount": 0.00,
      "confidence": 0.95,
      "type": 1
    },
    {
      "predictionDate": "2023-07-15",
      "predictedAmount": 5500.00,
      "confidence": 0.90,
      "type": 1
    }
    // 更多预测...
  ]
}
```

#### 8.3 获取消费建议

- **URL**: `/ai/advice`
- **方法**: GET
- **描述**: 获取消费建议
- **查询参数**:
  - `bookId`: 账本ID，必填
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": "根据您最近30天的消费数据分析：\n\n1. 总支出：4200.50元\n2. 最高支出类别：购物，占总支出的35.25%\n3. 建议：您在购物上的支出比例较高，建议适当控制这方面的开支。\n4. 一般建议：\n   - 建立预算计划，控制非必要支出\n   - 关注经常性小额支出，它们累积起来可能是一笔不小的开销\n   - 定期检查您的消费模式，及时调整消费习惯"
}
```

#### 8.4 智能推荐分类

- **URL**: `/ai/suggest-category`
- **方法**: POST
- **描述**: 智能推荐交易分类
- **请求参数**:

```json
{
  "description": "肯德基午餐", // 交易描述，必填
  "amount": 35.50,           // 金额，可选
  "type": 2                  // 交易类型，必填，1-收入，2-支出
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "categoryId": 7,
    "categoryName": "餐饮",
    "confidence": 0.95
  }
}
```

## 错误响应示例

### 参数错误

```json
{
  "code": 400,
  "message": "参数错误",
  "data": {
    "username": "用户名不能为空",
    "password": "密码长度必须在6-20之间"
  }
}
```

### 未登录

```json
{
  "code": 401,
  "message": "未登录或登录已过期",
  "data": null
}
```

### 无权限

```json
{
  "code": 403,
  "message": "无权限执行此操作",
  "data": null
}
```

### 资源不存在

```json
{
  "code": 404,
  "message": "资源不存在",
  "data": null
}
```

### 服务器内部错误

```json
{
  "code": 500,
  "message": "服务器内部错误",
  "data": null
}
```