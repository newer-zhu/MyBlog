package com.zhuhodor.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhuhodor.myblog.Entity.Permission;
import com.zhuhodor.myblog.Entity.Role;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.mapper.UserMapper;
import com.zhuhodor.myblog.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends IService<User> {

    @Cacheable(key = "'id'+#p0")
    User findUserById(int id);

    @CachePut(key = "'id'+#p0.id")
    void creatUser(User user);

    User findUserByName(String name);

    List<Role> findRolesByUserName(String username);

    List<Permission> findPermissionsByRoleId(String id);
}
