# Mongo adepts Starter

![](./doc/mongo-adepts-logo.png)

>这是一个基于MongoTemplate的封装强化starter组件

# 特性

- 熟悉的CRUD操作（用过MyBatis&MyBatisPlus的更容易上手）
- 使用简单，代码无侵入
- 支持序列化传输（Dubbo RPC 调用）


### 快速使用

#### 1. 添加maven依赖
>说明：如果只添加了`mongo-adepts-starter`依赖默认是静态模式，不会自动连接mongodb；
这个是考虑到模块化开发时`mongo-adepts`可能只做一个model依赖模块，会导致不需要连接mongodb的服务会自动连接，所以才设置其不自动连接。
```xml
<!--mongo-adepts-->
<dependency>
    <groupId>com.coeuy</groupId>
    <artifactId>mongo-adepts-starter</artifactId>
    <version>version</version>
</dependency>
<!--mongo data-->
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

#### 2. Springboot - application.yml

```yaml
# 原生 spring mongo data 配置
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/test-db

# mongo-adepts 配置
mongo-adepts:
  # debug模式
  debug: true
```

#### 3. 代码中使用

以集合`User`为例子

```java
/**
 * 用户集合
 * 初次使用JavaMongo提示：
 * 加@Document即表示该类为文档类，集合名默认使用该Class的SimpleName，即"User"
 */
@Data
@Document
public class User {

    /**
     * 这里的@Id表示该属性为集合ObjectId 文档唯一键
     */
    @Id
    private String userId;

    private String username;

    private String password;
}
```

- 方式1 ：获取Bean调用(需要传 Class<?>)

```java
import com.coeuy.osp.mongo.adepts.model.query.Adepts;
import com.coeuy.osp.mongo.adepts.model.query.LambdaQueryWrapper;
import com.coeuy.osp.mongo.adepts.model.query.QueryWrapper;
import com.coeuy.osp.mongo.adepts.model.query.QueryWrapper;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Resource
    private MongoAdepts mongoAdepts;

    /**
     * new QueryWrapper 传入 字段
     */
    public void getOne() {
        QueryWrapper queryWrapper = new QueryWrapper().eq("username", "Superman");
        User user = mongoAdepts.getOne(queryWrapper, User.class);
        System.out.println(user);
    }

    /**
     * 支持Lambada
     */
    public void getOneLambada() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Adepts.<User>lambdaQuery().eq(User::getUsername, "Superman");
        User user = mongoAdepts.getOne(lambdaQueryWrapper, User.class);
        System.out.println(user);
    }

    public void insert() {
        User user = new User();
        user.setUsername("Superman");
        User insert = mongoAdepts.insert(user);
        System.out.println(insert);
    }
}
```

####

- 方式2：继承`MongoService`

```java
import com.coeuy.osp.mongo.adepts.service.MongoService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends MongoService<User> {

    public void mongo() {
        User user = new User();
        user.setUsername("Superman");
        User insert = this.insert(user);
        System.out.println(insert);
    }
}

```

### CRUD接口文档

1. getById(id,entityClass) 根据文档ObjectId查询单个文档

2. getOne(queryWrapper) 根据构造器条件查询文档单条记录

3. list(entityClass) 查询指定集合全部文档

4. list(queryWrapper,entityClass) 根据构造器条件查询多个文档

5. listByIds(idList,entityClass) 根据文档多个ObjectId查询多个文档

6. insert(entity) 插入单个文档

7. insertBatch(entityList) 批量插入文档

8. save(entity) 保存单条文档（注意：⚠️这里会保存为null的属性）

9. update(queryWrapper) 根据条件构造器更新单个文档

10. updateMulti(queryWrapper) 根据条件构造器更新多个文档MONGO_ID

11. delete(queryWrapper) 根据条件删除文档(注意：⚠️这里会删除满足条件的所有文档而不是删除单个文档)

12. deleteAll(entityClass) 删除集合所有文档

13. count(queryWrapper) 根据条件构造器查询集合文档数量

14. exists(queryWrapper) 根据条件构造器查询文档是否存在

15. exists(entityClass) 查询集合是否存在

16. page(pageInfo,entityClass) 无条件分页查询

17. page(pageInfo,queryWrapper) 根据条件构造器分页查询

### 调用 mongoTemplate

说明：mongoAdepts是基于mongoTemplate的封装强化，您也可以通过mongoAdepts调用mongoTemplate原生方法
```java
 User user = mongoAdepts.template().findOne(...)
```


### QueryWrapper 条件构造器

1. `eq(String key, Object value)` 等于


2. `ne(String key, Object value)` 不等于


3. `in(String key, Object value)` 匹配多个


4. `ge(String key, Object value)` 大于等于


5. `gt(String key, Object value)` 小于等于


6. `le(String key, Object value)` 大于


7. `lt(String key, Object value)` 小于


8. `geAndLe(String key, Object ge, Object le)` 范围查询（能够比较的值)


9. `like(String key, CharSequence keyword)` 模糊匹配（`*keyword*`）


10. `likeLeft(String key, CharSequence keyword)`模糊匹配左边（`keyword*`）


11. `likeLeft(String key, CharSequence keyword)`模糊匹配右边（`*keyword`）


12. `orderByDesc(String key)` 根据指定Key降序


12. `orderByAsc(String key)` 根据指定的Key生序


13. `update(String key, Object value)` 更新指定文档属性


14. `search(String value)` 索引搜索，建立全文索引的情况下生效


15. `push(String key, Object value)` 追加子文档


15. `pull(String key, Object value)` 删除子文档


15. `or(QueryWrapper<T> wrapper)` Or 条件


15. `inc(String key,Integer number)` 字段加减法


### 参考例子

#### [mongo-adepts-springboot-example](https://github.com/yarnk/mongo-adepts-springboot-example)





