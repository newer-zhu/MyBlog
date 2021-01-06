package com.zhuhodor.myblog.service;

import com.zhuhodor.myblog.Entity.Permission;
import com.zhuhodor.myblog.Entity.Role;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.mapper.UserMapper;
import com.zhuhodor.myblog.service.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public User findUserById(int id);

    public void creatUser(User user);

    public User findUserByName(String name);

    public List<Role> findRolesByUserName(String username);

    public List<Permission> findPermissionsByRoleId(String id);

}
