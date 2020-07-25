package com.home.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author liqingdong
 * oauth2授权服务器
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;
    /**
     * 客户端详情服务, 相当于ClientDetailsServiceConfigurer的配置
     */
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        //设置授权码模式的授权码如何存取
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     * 配置客户端服务, 用来认证客户端是否合法
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 自定义
        clients.inMemory()
                // 标识客户ID
                .withClient("c1")
                // (需要值得信任的客户端)客户端秘钥
                .secret(new BCryptPasswordEncoder().encode("secret"))
                // 客户端可以访问的资源
                .resourceIds("res1")
                // 客户端申请资源的方式, oauth支持的5中授权类型
                .authorizedGrantTypes("authorization_code",
                        "password",
                        "client_credentials",
                        "implicit",
                        "refresh_token")
                // 允许的授权范围, 客户端权限范围(read, order-service)
                .scopes("all")
                // 授权码模式, false会跳转到授权页面进行授权
                // true不跳转授权页面, 直接发放令牌
                .autoApprove(true)
                .redirectUris("http://www.baidu.com");
    }

    /**
     * 令牌管理服务, 任何模式都需要配置
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        // 客户端服务 ClientDetailsServiceConfigurer中的配置信息
        services.setClientDetailsService(clientDetailsService);
        // 是否支持刷新令牌
        services.setSupportRefreshToken(true);
        // 令牌存储策略
        services.setTokenStore(tokenStore);
        // 令牌默认有效期2小时
        services.setAccessTokenValiditySeconds(7200);
        // 刷新令牌默认有效期3天
        services.setRefreshTokenValiditySeconds(259200);
        return services;
    }

    /**
     * 配置令牌断点的安全约束
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // 密码模式需要
                .authenticationManager(authenticationManager)
                // 授权码模式需要
                .authorizationCodeServices(authorizationCodeServices)
                // 令牌管理服务
                .tokenServices(tokenServices())
                // 允许post提交获取令牌
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 令牌访问端点的策略
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                // 允许表单提交, 申请令牌
                .allowFormAuthenticationForClients();
    }

}
