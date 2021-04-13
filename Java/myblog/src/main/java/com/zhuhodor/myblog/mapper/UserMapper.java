package com.zhuhodor.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhuhodor.myblog.Entity.Permission;
import com.zhuhodor.myblog.Entity.Role;
import com.zhuhodor.myblog.Entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where id = #{id}")
    User findUserById(int id);

    @Select("select * from user where username = #{username}")
    User findUserByName(String username);

    @Select("SELECT u.id uid, u.username, r.id, r.role FROM `user` u LEFT JOIN user_role ur ON u.id = ur.userId\n" +
            "LEFT JOIN role r\n" +
            "ON ur.roleId = r.id\n" +
            "WHERE u.username = #{username}")
    List<Role> findRolesByUserName(String username);

    @Select("SELECT p.id, p.permission, p.url FROM role r\n" +
            "LEFT JOIN role_permission rp\n" +
            "ON r.id = rp.roleId\n" +
            "LEFT JOIN permission p\n" +
            "ON rp.permissionId = p.id\n" +
            "WHERE r.id = #{id}\n")
    List<Permission> findPermissionsByRoleId(String id);

    @Select("SELECT u.* FROM `user` u, `project` p WHERE u.id = p.start_user AND p.id = #{projectId}")
    User findUserByProjectId(String projectId);

}
