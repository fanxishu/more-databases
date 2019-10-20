# more-databases

mybatis多数据源配置
2018-10-23 16:19:16 ht_kasi 阅读数 196更多
分类专栏： mybatis  springmvc  数据库  springboot
版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
本文链接：https://blog.csdn.net/ht_kasi/article/details/83308308
        最近改造老项目需要使用两个数据库，故而总结了一下mybatis的双数据源配置，学过mybatis的都知道mybatis其实就是封装了JDBC的框架，使用mybatis绕不开sqlSessionFactory的配置，那配置多个数据源的核心也是在于配置多个session工厂，用不同的工厂去操作不同的数据库就ok了，具体配置请参照下面配置文件说明，分为springboot和springmvc两种

springboot + mybatis
1、首先application.yml文件里面的datasource配置，这里就不写连接池的具体属性了

#主数据库
spring.datasource.primary.url=jdbc:mysql://localhost:3306/pa_db?useUnicode=true&characterEncoding=utf-8
spring.datasource.primary.username=root
spring.datasource.primary.password=root
spring.datasource.primary.driver-class-name=com.mysql.jdbc.Driver
#省略连接池配置
#从数据库
spring.datasource.secondary.url=jdbc:mysql://localhost:3306/cas_user_db?useUnicode=true&characterEncoding=utf-8
spring.datasource.secondary.username=root
spring.datasource.secondary.password=root
spring.datasource.secondary.driver-class-name=com.mysql.jdbc.Driver
2、接着就是核心的sqlSessionFactory配置

2.1、主数据源

basePackages对应的是dao类放置地址,不同的数据源dao和mapper分开放

@Configuration
@MapperScan(basePackages = "com.xxxx.xxxx.dao", sqlSessionTemplateRef = "primarySqlSessionTemplate")
public class PrimaryDataSourceConfig {
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    @Primary
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }
    // sqlSessionFactory配置
    @Bean(name = "primarySqlSessionFactory")
    @Primary /*此处必须在主数据库的数据源配置上加上@Primary*/
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("paDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        /*加载mybatis全局配置文件*/
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis-config.xml"));
        /*加载所有的mapper.xml映射文件*/
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));
        return bean.getObject();
    }
 
    // 事务配置
    @Bean(name = "primaryTransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean(name = "paSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
2.2、 从数据源

@Configuration
@MapperScan(basePackages = "com.xxxx.xxxx.mapper2", sqlSessionTemplateRef = "secondarySqlSessionTemplate")
public class SecondaryDataSource2Config {
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }
    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("secondaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        /*加载mybatis全局配置文件*/
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis-config.xml"));
        /*加载所有的mapper.xml映射文件*/
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));
        return bean.getObject();
    }
    @Bean(name = "secondaryTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean(name = "secondarySqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
基本上就这些，大家

注意，在多数据源的情况下，我们不需要在启动类添加：@MapperScan("com.xxx.mapper") 的注解。
1
这样 MyBatis 多数据源的配置就完成了，如果有更多的数据源请参考第二个数据源的配置即可。

测试

配置好多数据源之后，在项目中想使用哪个数据源就把对应数据源注入到类中使用即可。

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private User1Mapper user1Mapper;
    @Autowired
    private User2Mapper user2Mapper;

    @Test
    public void testInsert() throws Exception {
        user1Mapper.insert(new User("aa111", "a123456", UserSexEnum.MAN));
        user1Mapper.insert(new User("bb111", "b123456", UserSexEnum.WOMAN));
        user2Mapper.insert(new User("cc222", "b123456", UserSexEnum.MAN));
    }
}
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
上面的测试类中注入了两个不同的 Mapper，对应了不同的数据源。在第一个数据源中插入了两条数据，第二个数据源中插入了一条信息，运行测试方法后查看数据库1有两条数据，数据库2有一条数据，证明多数据源测试成功。
————————————————
版权声明：本文为CSDN博主「taojin12」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/taojin12/article/details/88399177
