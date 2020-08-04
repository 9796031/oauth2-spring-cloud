package com.home.order.entity;

import lombok.Data;

/**
 * @author liqingdong
 * 用户实体
 */
@Data
public class UserEntity {

    private long id;

    private String name;

    private String password;

    private String address;
}
