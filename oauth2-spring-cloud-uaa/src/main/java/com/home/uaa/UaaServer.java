package com.home.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author liqingdong
 * UAA服务, 用来进行oauth授权管理
 */
@SpringCloudApplication
public class UaaServer {

    public static void main(String[] args) {
        SpringApplication.run(UaaServer.class);
    }
}
