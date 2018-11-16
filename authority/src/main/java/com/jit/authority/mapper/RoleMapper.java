package com.jit.authority.mapper;

import com.jit.authority.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RoleMapper {

    @Select("SELECT r.name FROM USER u LEFT JOIN user_role ur ON u.id = ur.user_id LEFT JOIN role r ON r.id = ur.role_id WHERE username = #{username}")
    List<Role> findRolesByUsername(@Param("username") String username);

    @Select("select * from role")
    List<Role> findAllRoles();
}
