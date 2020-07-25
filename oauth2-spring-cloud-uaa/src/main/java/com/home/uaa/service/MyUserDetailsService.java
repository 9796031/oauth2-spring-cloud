package com.home.uaa.service;

import com.home.uaa.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author liqingdong
 * 自定义UserDetailsService 加载用户
 * 注意:使用自定义UserDetailsService需要注释webSecurityConfig中的UserDetailsService
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return User.withUsername("zhangsan").password("$2a$10$66YF4Gec5gLsSB1/8dHR2Ob5mIIRlsFPuvjMvLDKkQOoWDgHso1Xi").authorities("p1").build();
        // 使用数据库方式读取用户
        com.home.uaa.entity.User user = userDao.getUserByUserName(username);
        if (user == null) {
            // 如果没有返回空, 由provider来抛异常(认证的处理类)
            return null;
        }
        // 9. 通过数据库获取权限
        String[] permission = new String[]{};
        if (user != null) {
            permission = userDao.getPermissionByUserName(user.getId());
        }
        return User.withUsername(user.getName()).password(user.getPassword()).authorities(permission).build();
    }
}
