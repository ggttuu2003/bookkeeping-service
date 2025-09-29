# 智能记账小程序后端服务

## 项目概述

智能记账小程序后端服务是一个基于Spring Boot开发的记账应用后端，提供了用户认证、记账管理、数据统计分析以及AI智能预测等功能。本项目采用了现代化的技术栈和架构设计，旨在为用户提供一个安全、高效、智能的记账解决方案。

## 技术栈

- **核心框架**：Spring Boot 2.7.x
- **安全框架**：Spring Security + JWT
- **数据库**：MySQL 8.0
- **ORM框架**：MyBatis-Plus 3.5.x
- **缓存**：Redis
- **API文档**：Knife4j (基于Swagger)
- **工具库**：Hutool、Fastjson、Lombok、MapStruct等
- **AI服务**：WebFlux (用于调用外部AI服务)

## 功能特性

### 1. 用户管理

- 用户注册、登录、注销
- 基于JWT的身份认证
- 用户信息管理
- 角色权限控制

### 2. 记账核心功能

- 多账本管理
- 收支记录管理
- 分类与标签管理
- 预算管理
- 账本成员协作

### 3. 智能分析统计

- 日常收支统计
- 月度收支报表
- 分类消费占比分析
- 收支趋势分析
- 消费习惯分析
- 预算执行情况分析

### 4. 创新功能

- **AI预测**：基于历史数据预测未来收支
- **OCR识别**：自动识别票据信息创建交易记录
- **智能分类**：根据交易描述自动推荐分类
- **消费建议**：基于消费数据提供个性化消费建议

## 系统架构

```
+------------------+
|   小程序前端     |
+--------+---------+
         |
         | HTTP/HTTPS
         |
+--------v---------+
|   API网关层      |
+--------+---------+
         |
         |
+--------v---------+
|  应用服务层      |
|                  |
|  +------------+  |
|  | 用户服务   |  |
|  +------------+  |
|                  |
|  +------------+  |
|  | 记账服务   |  |
|  +------------+  |
|                  |
|  +------------+  |
|  | 统计分析   |  |
|  +------------+  |
|                  |
|  +------------+  |
|  | AI智能服务 |  |
|  +------------+  |
+--------+---------+
         |
         |
+--------v---------+
|   数据持久层     |
|  +------------+  |
|  |  MySQL     |  |
|  +------------+  |
|  +------------+  |
|  |  Redis     |  |
|  +------------+  |
+------------------+
```

## 数据库设计

系统包含以下主要数据表：

- **用户表(tb_user)**：存储用户基本信息
- **角色表(tb_role)**：存储系统角色信息
- **用户角色关联表(tb_user_role)**：用户与角色多对多关系
- **账本表(tb_book)**：存储账本信息
- **账本成员表(tb_book_member)**：账本与用户多对多关系
- **分类表(tb_category)**：交易分类信息
- **标签表(tb_tag)**：交易标签信息
- **交易记录表(tb_transaction)**：核心交易数据
- **交易标签关联表(tb_transaction_tag)**：交易与标签多对多关系
- **预算表(tb_budget)**：预算管理
- **AI预测记录表(tb_ai_prediction)**：AI预测结果存储
- **系统配置表(tb_system_config)**：系统配置信息

## API接口

### 用户接口

- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `GET /api/auth/info` - 获取当前用户信息
- `PUT /api/auth/info` - 更新用户信息

### 记账接口

- `POST /api/books` - 创建账本
- `GET /api/books` - 获取用户账本列表
- `GET /api/books/{id}` - 获取账本详情
- `POST /api/transactions` - 创建交易记录
- `GET /api/transactions` - 分页查询交易记录
- `PUT /api/transactions/{id}` - 更新交易记录
- `DELETE /api/transactions/{id}` - 删除交易记录
- `POST /api/transactions/ocr` - OCR识别创建交易

### 统计分析接口

- `GET /api/statistics/daily` - 获取每日收支统计
- `GET /api/statistics/monthly` - 获取月度收支统计
- `GET /api/statistics/category` - 获取分类收支统计
- `GET /api/statistics/trend` - 获取收支趋势分析
- `GET /api/statistics/habits` - 获取消费习惯分析
- `GET /api/statistics/budget` - 获取预算执行情况

### AI智能接口

- `GET /api/ai/predict/expense` - 预测未来支出
- `GET /api/ai/predict/income` - 预测未来收入
- `GET /api/ai/advice` - 获取消费建议
- `POST /api/ai/suggest-category` - 智能推荐分类

## 项目亮点

1. **完善的安全机制**：采用Spring Security + JWT实现安全认证，保障用户数据安全
2. **灵活的多账本设计**：支持用户创建多个账本，满足不同场景需求
3. **丰富的统计分析**：提供多维度的数据分析，帮助用户了解自己的财务状况
4. **AI智能功能**：集成AI能力，提供预测、识别和建议等智能服务
5. **良好的扩展性**：模块化设计，便于功能扩展和维护
6. **完整的API文档**：使用Knife4j提供交互式API文档

## 创新点详解

### 1. AI预测功能

系统基于用户历史交易数据，使用时间序列分析算法预测未来一段时间内的可能收支情况，并给出置信度评分。这一功能可以帮助用户提前规划财务，做好预算安排。

### 2. OCR票据识别

用户可以上传票据图片，系统通过OCR技术自动识别票据上的商家、金额、日期等信息，并自动创建交易记录。这大大简化了记账流程，提高了记账效率和准确性。

### 3. 智能分类推荐

系统能够根据交易描述、金额等信息，智能推荐最可能的交易分类。随着用户使用的增加，推荐准确率会不断提高，为用户省去手动选择分类的麻烦。

### 4. 个性化消费建议

基于用户的消费习惯和预算情况，系统能够提供个性化的消费建议，如指出过度消费的类别、建议控制的消费项目等，帮助用户养成良好的消费习惯。

## 部署说明

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 部署步骤

1. 克隆项目到本地
   ```bash
   git clone https://github.com/yourusername/bookkeeping-service.git
   ```

2. 创建数据库并导入初始化脚本
   ```bash
   mysql -u root -p < src/main/resources/schema.sql
   ```

3. 修改配置文件
   ```
   修改 src/main/resources/application.yml 中的数据库连接、Redis配置等
   ```

4. 编译打包
   ```bash
   mvn clean package -DskipTests
   ```

5. 运行应用
   ```bash
   java -jar target/bookkeeping-service-0.0.1-SNAPSHOT.jar
   ```

## 未来计划

1. 集成更多AI能力，如消费异常检测、财务健康评分等
2. 支持多语言和多货币
3. 增加数据导入导出功能
4. 优化移动端体验
5. 增加社交分享功能

## 贡献指南

欢迎贡献代码或提出建议，请遵循以下步骤：

1. Fork本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开一个Pull Request

## 许可证

本项目采用MIT许可证 - 详情请参见 [LICENSE](LICENSE) 文件