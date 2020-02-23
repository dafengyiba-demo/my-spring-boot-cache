package my.company.cache.myspringbootcache;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


/**
 * 一、搭建基本环境:
 *  1.导入数据库文件department.sql、employee.sql创建出department和employee表
 *  2.JavaBean封装
 *  3.整合mybatis操作数据库
 *      1.配置数据源
 *      2.使用注解版的mybatis
 *          1.使用@MapperScan指定要扫描的包(使用了@MapperScan注解，接口上可以不使用@Mapper注解)
 * 二、快速体验缓存：
 *  1.开启基于注解的缓存,@EnableCaching
 *  2.注解标注缓存
 *
 *  默认使用的是：ConcurrentMapCacheManager缓存管理器，ConcurrentMapCache缓存，数据是缓存在ConcurrentMap<Object, Object>中的
 *  开发中一般使用的是第三方缓存中间件：Redis、ehcache等
 *
 * 三、整合Redis作为缓存中间件
 *  1.安装redis，使用docker安装
 *  2.引入redis的starter
 *  3.配置redis，在application.yaml中配置redis
 *      redis序列化问题：
 *          1.如果保存对象，springboot默认使用jdk序列化机制，将序列化的数据保存到redis中，被保存的对象需要实现序列化接口
 *          2.将数据转成json的方式保存
 *              1.使用json库，将数据转成json
 *              2.使用redisTemplate默认的序列化规则
 *                  1.在使用时，给redisTemplate设置序列化规则：单独指定key和value的序列化规则
 *                      Jackson2JsonRedisSerializer<Employee> serializer = new Jackson2JsonRedisSerializer<>(Employee.class);
 *                      redisTemplate.setKeySerializer(serializer);
 *                      redisTemplate.setValueSerializer(serializer);
 *                  2.按照redisAutoConfiguration注册自己的redisTemplate：指定默认序列化规则即可
 *                      @Bean
 *                      public RedisTemplate<String, Employee> empRedisTemplate(RedisConnectionFactory redisConnectionFactory)
 *                              throws UnknownHostException {
 *                          RedisTemplate<String, Employee> template = new RedisTemplate<>();
 *                          template.setConnectionFactory(redisConnectionFactory);
 *                          Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(Employee.class);
 *                          template.setDefaultSerializer(serializer);
 *                          return template;
 *                      }
 *  4.原理：
 *      1.引入redis的starter，容器中创建的是RedisCacheManager
 *      2.RedisCacheManager创建RedisCache来作为缓存组件
 *      3.RedisCache操作redis来进行缓存
 *      4.默认保存数据k-v都是object，都是利用jdk的序列化来保存数据，如何保存成json？
 *          1.引入redis的starter，cacheManager变成了RedisCacheManager
 *          2.重写CacheManager
 *      5.编码的方式使用缓存，直接注入RedisCacheManager，然后用RedisCacheManager获取到某个cache进行操作
 */



@MapperScans({
        @MapperScan("my.company.cache.myspringbootcache.mapper")
})
@SpringBootApplication
@EnableCaching
public class MySpringBootCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySpringBootCacheApplication.class, args);
    }

}
