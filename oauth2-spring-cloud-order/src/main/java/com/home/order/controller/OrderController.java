package com.home.order.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liqingdong
 * 订单controller 用来测试token访问资源
 */
@RestController
public class OrderController {

    /**
     * 拥有p1资源才可以访问该url
     */
    @GetMapping("/r1")
    @PreAuthorize("hasAnyAuthority('p1')")
    public String r1() {
        return "访问r1资源成功!!!";
    }
}
