package com.home.uaa.dao;

import com.home.uaa.entity.Permission;
import com.home.uaa.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liqingdong
 * 用来查询数据库用户
 */
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User getUserByUserName(String userName) {
        String sql = "select * from t_user where name = ?";
        List<User> result = jdbcTemplate.query(sql, new Object[]{userName}, new BeanPropertyRowMapper<>(User.class));
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }

    /**
     * 9. 通过数据库获取权限
     * @param userId
     * @return
     */
    public String[] getPermissionByUserName(long userId) {
        String sql = "select * from t_permission where id in (\n" +
                "\tselect permission_id from t_role_permission where role_id in (\n" +
                "\t\tselect role_id from t_user_role \n" +
                "\t\t\twhere user_id = ?\n" +
                "\t\t)\n" +
                "\t)";
        List<Permission> permissions = jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(Permission.class));
        if (CollectionUtils.isEmpty(permissions)) {
            return new String[]{};
        }
        String[] permission = new String[permissions.size()];
        List<String> permissionList = permissions.stream().map(Permission::getCode).collect(Collectors.toList());
        permissionList.toArray(permission);
        return permission;
    }
}
