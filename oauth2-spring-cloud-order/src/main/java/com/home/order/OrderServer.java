package com.home.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liqingdong
 * 订单服务, 用来校验授权是否有效
 */
@SpringBootApplication
public class OrderServer {

    public static void main(String[] args) {
        SpringApplication.run(OrderServer.class);
    }
}
