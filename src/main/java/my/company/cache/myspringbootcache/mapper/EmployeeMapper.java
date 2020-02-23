package my.company.cache.myspringbootcache.mapper;

import my.company.cache.myspringbootcache.bean.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface EmployeeMapper {

    @Select("select * from employee where id = #{id}")
    Employee findEmpById(Integer id);

    @Update("update employee set last_name=#{lastName},email=#{email},gender=#{gender},d_id=#{dId} where id=#{id}")
    void updateEmp(Employee employee);

    @Delete("delete from employee where id = #{id}")
    void deleteEmp(String id);

    @Select("select * from employee where last_name=#{lastName}")
    Employee findEmpByLastName(String lastName);
}
