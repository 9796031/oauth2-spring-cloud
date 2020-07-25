package com.home.uaa.entity;

import lombok.Data;

/**
 * @author liqingdong
 * 用来认证授权
 */
@Data
public class User {

    private long id;

    private String name;

    private String password;

    private String address;
}
