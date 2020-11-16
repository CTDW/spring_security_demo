package com.yzh.demo.project.mapper;

import com.yzh.demo.project.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailsMapper {
    /**
     * 查找用户通过用户名
     * @param name
     * @return User
     */
    User getUserByName(@Param("name")String name);

    /**
     * 查找用户根据用户名
     * @param name
     * @return List<User.Role>
     */
    List<User.Role> getUserRoleByName(@Param("name")String name);
}
