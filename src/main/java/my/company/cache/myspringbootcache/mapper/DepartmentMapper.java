package my.company.cache.myspringbootcache.mapper;

import my.company.cache.myspringbootcache.bean.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DepartmentMapper {


    @Select("select * from department where id = #{id}")
    Department findDeptById(String id);

    @Update("update department set departmentName = #{departmentName} where id = #{id}")
    void updateDept(Department department);


    @Delete("delete from department where id = #{id}")
    void deleteDept(String id);


}
