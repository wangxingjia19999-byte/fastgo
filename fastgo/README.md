校内二手物品交易平台（极简版）

只做核心 CRUD，不做支付、聊天：
功能：
商品发布、编辑、删除
分类管理
查看商品列表
留言 / 求购记录（简单文本）
表很少，逻辑极简单，老师不会觉得你抄模板。
# 原生Java + MyBatis 快速运行说明

1. 数据库准备

- 请确保本地MySQL已启动，并创建如下表结构：

```sql
CREATE DATABASE your_database;
USE your_database;
CREATE TABLE user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50)
);
INSERT INTO user (name) VALUES ('Tom'), ('Jerry');
```

2. 配置数据库连接

- 修改 src/main/resources/mybatis-config.xml 中的 url、username、password 为你的数据库信息。

3. 编译与运行

- 进入 fastgo 目录，执行：

```sh
mvn compile
mvn exec:java -Dexec.mainClass="com.example.MyBatisDemo"
```

或直接用IDE运行 com.example.MyBatisDemo 的 main 方法。

4. 预期输出

- 控制台将输出数据库 user 表中的所有用户信息。

---

如需自定义实体或Mapper，请参考 com.example.User、UserMapper 及对应XML。
