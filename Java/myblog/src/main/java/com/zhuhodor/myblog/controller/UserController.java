package com.zhuhodor.myblog.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.dao.LoginDao;
import com.zhuhodor.myblog.service.UserService;
import com.zhuhodor.myblog.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * 用户模块api
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    JwtUtil jwtUtil;

//    登录接口
    @CrossOrigin
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDao loginDao, HttpServletResponse response) {
        User user = userService.findUserByName(loginDao.getUsername());
        Assert.notNull(user, "用户不存在");
        //密码校验
        Md5Hash md5Hash = new Md5Hash(loginDao.getPassword(), user.getSalt());
        if (!user.getPassword().equals(md5Hash.toHex())){
            return Result.fail("密码不正确");
        }
        String jwtToken = jwtUtil.generateToken(user.getId());
        response.setHeader("Authorization", jwtToken);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        return Result.success(MapUtil.builder().put("id", user.getId())
                .put("username", user.getUsername())
                .put("email", user.getEmail())
                .put("createAt", user.getCreateAt())
                .put("college", user.getCollege())
                .put("major", user.getMajor())
                .put("grade", user.getGrade())
                .put("avatar", user.getAvatar())
                .put("createAt", user.getCreateAt())
                .put("description", user.getDescription()).map());
    }

    @RequestMapping("/{id}")
    public Result getUserById(@PathVariable("id") int id){
        return Result.success(userService.findUserById(id));
    }

    @PostMapping("/register")
    public Result register(@RequestBody @Validated User user){
        //不允许用户名重复
        if (userService.findUserByName(user.getUsername()) == null){
            LocalDate now = LocalDate.now();
            user.setCreateAt(now);
            try {
                userService.creatUser(user);
                return Result.success(200,"注册成功",user);
            }catch (Exception e){
                e.printStackTrace();
                log.error("注册异常--->{}",e.getMessage());
                return Result.fail("注册异常");
            }
        }else {
            return Result.fail("用户名已存在");
        }
    }

    //用户登出
//    @RequestMapping("/logout")
//    public Result logout(){
//        Subject subject = SecurityUtils.getSubject();
//        log.info("用户[{}]登出",subject.getPrincipal());
//        subject.logout();
//        return Result.success(null);
//    }


}
