package com.home.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liqingdong
 * UAA服务, 用来进行oauth授权管理
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UaaServer {

    public static void main(String[] args) {
        SpringApplication.run(UaaServer.class);
    }
}
