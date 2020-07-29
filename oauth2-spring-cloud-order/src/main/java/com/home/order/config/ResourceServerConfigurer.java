package com.home.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author liqingdong
 * 资源服务器配置
 * EnableGlobalMethodSecurity 启用基于方法的授权
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "res1";

    /**
     * 配置资源
     * @param resources 资源
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID)
                // 验证令牌的服务
                .tokenServices(tokenServices())
                // 允许基于令牌(token)的方式验证
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 所有资源必须有scope=all的权限
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                .and().csrf().disable()
                // 基于token方式验证, 不需要使用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * 资源服务令牌解析服务
     * 令牌(token)远程验证的服务
     */
    @Bean
    public ResourceServerTokenServices tokenServices() {
        // 同一个项目中可以使用
        // DefaultTokenServices services = new DefaultTokenServices();
        // 使用远程方式验证令牌
        RemoteTokenServices services = new RemoteTokenServices();
        // /oauth/check_token固定, oauth2提供
        // 可以通过http://localhost:53020/uaa/oauth/check_token?token=e2e718f8-498e-45c1-acac-8cd6cd6ac424来测试
        services.setCheckTokenEndpointUrl("http://localhost:53020/uaa/oauth/check_token");
        services.setClientId("c1");
        services.setClientSecret("secret");
        return services;
    }
}

