# 智能记账小程序 API 文档 (简化版)

## 基础信息

- 基础路径: `/api`
- 请求格式: JSON
- 响应格式: JSON
- 认证方式: JWT Token (在请求头中添加 `Authorization: Bearer {token}`)

## 功能概述

本API支持以下核心功能：
1. 记账功能：支持收入/支出记录
2. 展示与管理：记账列表、编辑/删除功能
3. 统计/汇总：支持分类支出占比、月度收支趋势

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
  "amount": 125.50,          // 金额，必填，精确到小数点后2位
  "type": 2,                 // 交易类型，必填，1-收入，2-支出
  "categoryId": 8,           // 分类ID，必填
  "paymentMethod": "微信支付", // 支付方式，非必填，有默认值
  "transactionTime": "2023-06-20T12:30:00", // 交易时间，有默认值，可选择
  "description": "超市购物"   // 交易描述/备注，非必填
}
```

- **响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "transactionId": 10001,
    "categoryId": 8,
    "categoryName": "购物",
    "amount": 125.50,
    "type": 2,
    "paymentMethod": "微信支付",
    "transactionTime": "2023-06-20T12:30:00",
    "description": "超市购物",
    "createTime": "2023-06-20T14:00:00"
  }
}
```

#### 3.2 分页查询交易记录

- **URL**: `/transactions`
- **方法**: GET
- **描述**: 分页查询交易记录
- **查询参数**:
  - `type`: 交易类型，可选，1-收入，2-支出
  - `categoryId`: 分类ID，可选
  - `startDate`: 开始日期，可选，格式：yyyy-MM-dd
  - `endDate`: 结束日期，可选，格式：yyyy-MM-dd
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
        "categoryId": 8,
        "categoryName": "购物",
        "amount": 125.50,
        "type": 2,
        "paymentMethod": "微信支付",
        "transactionTime": "2023-06-20T12:30:00",
        "description": "超市购物"
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
    "categoryId": 8,
    "categoryName": "购物",
    "amount": 125.50,
    "type": 2,
    "paymentMethod": "微信支付",
    "transactionTime": "2023-06-20T12:30:00",
    "description": "超市购物",
    "createTime": "2023-06-20T14:00:00",
    "updateTime": "2023-06-20T14:00:00"
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
  "type": 2,                 // 交易类型，可选，1-收入，2-支出
  "paymentMethod": "支付宝", // 支付方式，可选
  "transactionTime": "2023-06-20T13:00:00", // 交易时间，可选
  "description": "超市购物和日用品" // 交易描述，可选
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
- **描述**: 获取预设分类列表
- **查询参数**:
  - `type`: 交易类型，可选，1-收入，2-支出
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
      "type": 1
    },
    {
      "categoryId": 2,
      "name": "理财",
      "icon": "investment",
      "color": "#2196F3",
      "type": 1
    },
    {
      "categoryId": 3,
      "name": "兼职",
      "icon": "parttime",
      "color": "#9C27B0",
      "type": 1
    },
    {
      "categoryId": 4,
      "name": "红包",
      "icon": "redpacket",
      "color": "#F44336",
      "type": 1
    },
    {
      "categoryId": 5,
      "name": "其他收入",
      "icon": "other",
      "color": "#607D8B",
      "type": 1
    },
    {
      "categoryId": 6,
      "name": "餐饮",
      "icon": "food",
      "color": "#FF5722",
      "type": 2
    },
    {
      "categoryId": 7,
      "name": "交通",
      "icon": "transport",
      "color": "#03A9F4",
      "type": 2
    },
    {
      "categoryId": 8,
      "name": "购物",
      "icon": "shopping",
      "color": "#9C27B0",
      "type": 2
    },
    {
      "categoryId": 9,
      "name": "娱乐",
      "icon": "entertainment",
      "color": "#FF9800",
      "type": 2
    },
    {
      "categoryId": 10,
      "name": "居家",
      "icon": "home",
      "color": "#795548",
      "type": 2
    },
    {
      "categoryId": 11,
      "name": "学习",
      "icon": "study",
      "color": "#3F51B5",
      "type": 2
    },
    {
      "categoryId": 12,
      "name": "医疗",
      "icon": "medical",
      "color": "#E91E63",
      "type": 2
    },
    {
      "categoryId": 13,
      "name": "服饰",
      "icon": "clothing",
      "color": "#673AB7",
      "type": 2
    },
    {
      "categoryId": 14,
      "name": "人情",
      "icon": "gift",
      "color": "#FFC107",
      "type": 2
    },
    {
      "categoryId": 15,
      "name": "其他支出",
      "icon": "other",
      "color": "#607D8B",
      "type": 2
    }
  ]
}
```

### 5. 支付方式管理

#### 5.1 获取支付方式列表

- **URL**: `/payment-methods`
- **方法**: GET
- **描述**: 获取预设支付方式列表
- **响应示例**:

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "微信支付",
      "icon": "wechat_pay"
    },
    {
      "id": 2,
      "name": "支付宝",
      "icon": "alipay"
    },
    {
      "id": 3,
      "name": "银行卡",
      "icon": "bank_card"
    },
    {
      "id": 4,
      "name": "现金",
      "icon": "cash"
    }
  ]
}
```



### 6. 统计分析

#### 6.1 获取分类支出占比

- **URL**: `/statistics/category`
- **方法**: GET
- **描述**: 获取分类支出占比统计
- **查询参数**:
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
      "categoryId": 6,
      "categoryName": "餐饮",
      "amount": 1200.75,
      "percentage": 28.20,
      "count": 25
    },
    {
      "categoryId": 7,
      "categoryName": "交通",
      "amount": 800.25,
      "percentage": 18.80,
      "count": 18
    },
    {
      "categoryId": 9,
      "categoryName": "娱乐",
      "amount": 500.50,
      "percentage": 11.75,
      "count": 8
    },
    {
      "categoryId": 15,
      "categoryName": "其他支出",
      "amount": 250.25,
      "percentage": 6.00,
      "count": 5
    }
  ]
}
```

#### 6.2 获取月度收支趋势

- **URL**: `/statistics/trend`
- **方法**: GET
- **描述**: 获取月度收支趋势分析
- **查询参数**:
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
    "nets": [799.50, 1199.75, 899.25, 1199.50, 1599.75, 1299.50]
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
    "amount": "金额不能为空",
    "type": "交易类型必须为1或2"
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