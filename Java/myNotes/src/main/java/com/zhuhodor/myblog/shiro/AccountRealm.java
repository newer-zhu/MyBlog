package com.zhuhodor.myblog.shiro;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.zhuhodor.myblog.Entity.Permission;
import com.zhuhodor.myblog.Entity.Role;
import com.zhuhodor.myblog.Entity.User;
import com.zhuhodor.myblog.service.UserService;
import com.zhuhodor.myblog.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        String primaryPrincipal =(String) principal.getPrimaryPrincipal();
        List<Role> roles = userService.findRolesByUserName(primaryPrincipal);
        if (!CollectionUtil.isEmpty(roles)){
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            roles.forEach(role -> {
                simpleAuthorizationInfo.addRole(role.getRole());
                List<Permission> permissions = userService.findPermissionsByRoleId(role.getId());
                if (!CollectionUtil.isEmpty(permissions)){
                    permissions.forEach(permission -> {
                        simpleAuthorizationInfo.addStringPermission(permission.getPermission());
                    });
                }

            });
            return simpleAuthorizationInfo;
        }
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        log.info("jwt--------------->{}", jwtToken);
        String userId = jwtUtil.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        User user = userService.findUserById((int) Long.parseLong(userId));
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }
        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);

        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
    }
}
