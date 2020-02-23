package my.company.cache.myspringbootcache;

import my.company.cache.myspringbootcache.bean.Department;
import my.company.cache.myspringbootcache.bean.Employee;
import my.company.cache.myspringbootcache.mapper.DepartmentMapper;
import my.company.cache.myspringbootcache.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

@SpringBootTest
class MySpringBootCacheApplicationTests {


    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    DepartmentMapper departmentMapper;

    @Test
    void contextLoads() {

        Employee emp = employeeMapper.findEmpById(1);
        System.out.println(emp.toString());

        Department dept = departmentMapper.findDeptById("1");
        System.out.println(dept.toString());
    }


    @Autowired
    RedisTemplate<String,Employee> empRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Test
    void redis(){

        stringRedisTemplate.opsForValue().set("key1","value1");
        stringRedisTemplate.opsForValue().set("key2","value2");
        stringRedisTemplate.opsForValue().set("key3","value3");
        stringRedisTemplate.opsForValue().append("key1","-append");
    }

    @Test
    void redis2() {
        Employee emp = employeeMapper.findEmpById(1);

        Jackson2JsonRedisSerializer<Employee> serializer = new Jackson2JsonRedisSerializer<>(Employee.class);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.opsForValue().set("emp-1",emp);
//        redisTemplate.opsForValue().set("emp-1",emp);
    }


}
