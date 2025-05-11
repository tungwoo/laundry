# 洗衣店管理系统

## 项目介绍

洗衣店管理系统是一套全面的洗衣店业务管理解决方案，提供收衣、订单管理、会员管理、财务管理等核心功能，支持微信小程序和Web管理后台双端协同。系统旨在提升洗衣店的运营效率、优化客户体验，实现数据驱动的精细化运营。

## 功能特点

- **智能订单录入**：支持智能识别和手工录入模式，记录衣物详细信息
- **订单状态管理**：完整的订单状态流转，覆盖全业务流程
- **会员管理**：会员信息管理、会员卡充值、消费记录查询
- **财务管理**：多种支付方式、订单退款、财务报表
- **双端协同**：微信小程序和Web管理后台无缝协作

## 技术栈

### 后端
- Spring Boot 2.6.13
- MyBatis-Plus 3.5.3
- MySQL 数据库
- Maven 构建工具
- JDK 8

### 前端
- 管理后台：Vue + Element UI/Element Plus
- 微信小程序：微信原生技术

## 项目结构
```
根目录/
├── src/                                # Java 后端源代码
│   ├── main/
│   │   ├── java/com/bigcat/laundry/
│   │   │   ├── config/                 # 配置类
│   │   │   ├── controller/             # 控制器
│   │   │   │   ├── admin/              # 管理后台控制器
│   │   │   │   └── miniapp/            # 小程序控制器
│   │   │   ├── entity/                 # 实体类
│   │   │   ├── mapper/                 # Mybatis-Plus Mapper接口
│   │   │   ├── service/                # 服务接口
│   │   │   │   └── impl/               # 服务实现类
│   │   │   ├── vo/                     # 视图对象
│   │   │   ├── common/                 # 公共组件
│   │   │   │   ├── exception/          # 异常处理
│   │   │   │   ├── util/               # 工具类
│   │   │   │   └── result/             # 统一响应结果
│   │   │   └── LaundryApplication.java # 应用启动类
│   │   ├── resources/                  # 配置资源
│   │   │   ├── application.yml         # 应用配置文件
│   │   │   └── db/                     # 数据库脚本
├── miniapp/                            # 微信小程序
├── admin/                              # Vue管理后台
├── pom.xml                             # Maven配置
└── README.md                           # 项目说明
```

## 部署步骤

### 后端部署
1. 创建数据库并导入初始数据
```sql
-- 执行数据库初始化脚本
mysql -u username -p < src/main/resources/db/init.sql
```

2. 修改数据库配置
```yaml
# 修改 src/main/resources/application.yml 文件
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/laundry?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

3. 编译打包
```bash
mvn clean package -DskipTests
```

4. 运行应用
```bash
java -jar target/laundry-0.0.1-SNAPSHOT.jar
```

### 管理后台部署
1. 进入管理后台目录
```bash
cd admin
```

2. 安装依赖
```bash
npm install
```

3. 修改API地址配置
```
# 修改 .env.production 文件中的 API 地址
```

4. 构建生产环境代码
```bash
npm run build
```

5. 部署到服务器
```bash
# 将dist目录部署到Web服务器
```

### 微信小程序部署
1. 在微信开发者工具中导入小程序项目
2. 修改API地址配置
3. 上传代码并提交审核

## 系统账号
管理后台初始账号：
- 用户名：admin
- 密码：admin123

## 开发团队

- 大猫猫科技

## 许可证

版权所有 (c) 大猫科技
