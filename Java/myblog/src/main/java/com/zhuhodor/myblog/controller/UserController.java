package com.zhuhodor.myblog.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhuhodor.myblog.Entity.BlogModule.Blog;
import com.zhuhodor.myblog.Entity.Project;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.common.Result;
import com.zhuhodor.myblog.vo.LoginVo;
import com.zhuhodor.myblog.service.UserService;
import com.zhuhodor.myblog.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * 用户模块api
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController{
    @Autowired
    private UserService userService;
    @CrossOrigin
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginVo loginVo, HttpServletResponse response) {
        User user = userService.findUserByName(loginVo.getUsername());
        Assert.notNull(user, "用户不存在");
        //密码校验
        Md5Hash md5Hash = new Md5Hash(loginVo.getPassword(), user.getSalt());
        if (!user.getPassword().equals(md5Hash.toHex())){
            return Result.fail("密码不正确");
        }
        String jwtToken = jwtUtil.generateToken(user.getId());
        response.setHeader("Authorization", jwtToken);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        log.info("用户=={}==登录", loginVo.getUsername());
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

    @GetMapping("/{id}")
    public Result getUserById(@PathVariable("id") int id){
        log.info("查找id为{}的用户信息", id);
        return Result.success(userService.findUserById(id));
    }

    /**
     * 注册新用户
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody @Validated User user){
        //不允许用户名重复
        if (userService.findUserByName(user.getUsername()) == null){
            Date now = Calendar.getInstance().getTime();
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

    public Result getUserInfo(String userId){
        int count = blogService.count(new QueryWrapper<Blog>().eq("userId", userId));
        int pros = projectService.count(new QueryWrapper<Project>().eq("start_user", userId));
        return null;
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
