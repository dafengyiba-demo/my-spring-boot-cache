package my.company.cache.myspringbootcache.service;

import my.company.cache.myspringbootcache.bean.Employee;
import my.company.cache.myspringbootcache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;


/**
 * 注解@CacheConfig是抽取公共缓存配置用的
 *      cacheNames：指定公共的cache
 *      keyGenerator：指定公共的key生成规则
 *      cacheManager：指定公共的缓存管理器
 */
@CacheConfig(cacheNames = {"emp"})
@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 注解@Cacheable是将方法的运行结果进行缓存，以后再次查询该数据，直接重缓存中获取，不用调用方法：
     *
     * cacheManager管理多个cache，真正进行数据操作的是cache组件，每一个cache都有自己的名字
     * 几个属性：
     *  1.cacheNames/value：指定缓存组件的名字
     *  2.key：指定缓存数据使用的key，默认是使用方法参数的值作为key，方法的返回值为value
     *      SpEL表达式来指定key：
     *              名字              描述                  示例
     *          1. methodName  当前被调用的方法的名称        #root.methodName
     *          2. method      当前被调用的方法             #root.method.name
     *          3. target      当前被调用的目标对象#         #root.target
     *          4. targetClass 当前被调用的目标对象类        #root.targetClass
     *          5. args        当前被调用的方法的参数列表     #root.args[0]
     *          6. caches      当前方法使用的cache列表       #root.caches[0].name
     *          7. result      方法执行后的返回值            #result
     *
     *          方法的参数名字：可以直接使用#参数名，也可以使用#p0或#a0，例如：#id,#p0,#a0,0代表参数的索引
     *  3.keyGenerator:指定自己的key生成器
     *      key/keyGenerator只用配置一个，key和keyGenerator不能使用result对象，因为@Cacheable的调用时机是方法执行前
     *  4.cacheManager:指定缓存管理器，默认是缓存到一个ConcurrentHasHMap中的
     *      cacheResolver/cacheManager只用配置一个
     *  5.condition：指定符合条件才缓存，也可以用上面的SpEL表达式来指定
     *  6.unless：当unless条件为true时，不进行缓存，可以获取到返回结果进行判断
     *  6.sync：是否启用异步模式进行缓存
     *
     *
     *
     * 原理：
     *  1.自动配置类:CacheAutoConfiguration
     *  2.缓存的自动配置类：
     *      org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
     *      org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration 【默认开启】
     *      org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration
     *  3.到底哪个自动配置类生效?
     *      在application.yaml中配置debug=true，打开自动配置报告
     *      默认是：SimpleCacheConfiguration生效
     *  4.XXXXCacheConfiguration是个容器中注入一个XXXXcacheManager
     *      1.SimpleCacheConfiguration给容器中注入了一个ConcurrentMapCacheManager
     *      2.ConcurrentMapCacheManager的cache是保存在ConcurrentMap<String, Cache> cacheMap中的ConcurrentMapCache
     *      3.cache中的缓存是保存在ConcurrentMap<Object, Object> store中的
     *
     *
     * 运行流程：
     *  1.@Cacheable的运行流程：
     *      1.方法运行之前，先去查询（Cache）组件，按照cacheNames指定的名称去获取，CacheManage先获取相应的缓存，
     *      第一个获取时没有，则会自动创建一个Cache组件
     *      2.去Cache中查找缓存的内容，使用的key，默认是方法的参数
     *          key的生成策略：
     *              默认使用keyGenerator生成的，默认使用SimpleKeyGenerator生成key
     *              SimpleKeyGenerator的生成策略：
     *                  1.如果没有参数：key = new SimpleKeyGenerator();
     *                  2.如果只有一个参数：key = 参数值
     *                  3.如果有多个参数：key = new SimpleKey(params)
     *      3.如果没有查到缓存，就调用目标方法
     *      4.将目标方法的结果放到缓存中
     *      总结：方法执行前，检查缓存，有则返回，没有则调用方法，并缓存
     */

//    @Cacheable(cacheNames = {"emp"},
//            keyGenerator = "myKeyGenerator",
//            condition = "#id>1 and #id<1000",
//            unless = "a0==5")
    @Cacheable(cacheNames = "emp",key = "#id")
    public Employee getEmp(Integer id){
        System.out.println("查询员工。。。。");
        Employee emp = employeeMapper.findEmpById(id);
        return emp;
    }


    /**
     *
     *  注解@CachePut是既修改数据，又更新缓存
     *  运行时机：
     *      1.先调用目标方法
     *      2.将目标方法的结果缓存起来
     *   注意：缓存时对cacheNames和key的指定，@CachePut的key可以使用result对象，因为它的执行时机是方法执行后
     */
    @CachePut(cacheNames = {"emp"},key = "#result.id")
    public Employee updateEmp(Employee employee){
        employeeMapper.updateEmp(employee);
        return employee;
    }


    /**
     *
     * 注解@CacheEvict是清除缓存：在删除某条记录的时候，清除某些缓存
     *
     *   几个重要的属性：
     *      allEntries = true: 指定清除这个key的所有缓存数据
     *      beforeInvocation = true: 指定在方法调用之前执行，默认是在方法执行之后执行，如果出现异常，缓存就不会被清除掉
     */
    @CacheEvict(value = {"emp"},key = "#a0")
    public void deleteEmp(Integer id){
        System.out.println("模拟删除数据。。。");
    }


    /**
     * 注解@Caching是组合注解，可以组合成复杂的缓存配置
     */
    @Caching(
            cacheable = {
                @Cacheable(value = "emp",key = "#lastName")
            },
            put = {
                @CachePut(value = "emp",key="#a0"),
                @CachePut(value = "emp2",key = "#result.id")
            },
            evict = {
                @CacheEvict(value = "emp",key = "#lastName")
            }
    )
    public Employee getEmpByLastName(String lastName) {
        return employeeMapper.findEmpByLastName(lastName);
    }
}
