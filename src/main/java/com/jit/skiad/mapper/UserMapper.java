package com.jit.skiad.mapper;

import com.jit.skiad.domain.User;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;


@Mapper
@Component(value = "userRepository")
public interface UserMapper {

    @Select("select u.*, r.name as roles from user u left join user_role ur on u.id = ur.user_id left join role r on r.id = ur.role_id where username = #{username}")
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO user(username,password,image,register_time) VALUES(#{user.username}, #{user.password},#{user.image},#{user.register_time})")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")//mybatis使用注解方式插入数据后获取自增长的主键值
    int insert(@Param("user") User user);

    @Insert("insert into user_role(user_id,role_id) values(#{user_id},#{role_id})")
    int insertUserRole(@Param("user_id") Integer user_id, @Param("role_id") Integer role_id);

    @Select("SELECT image FROM user WHERE username = #{username}")
    String getUserImage(@Param("username") String username);

    @Update("update user set image = #{image} where username = #{username}")
    void updateUserImage(@Param("username") String username, @Param("image") String image);
}


