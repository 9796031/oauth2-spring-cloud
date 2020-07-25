package com.home.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author liqingdong
 * 令牌存储策略
 */
@Configuration
public class TokenConfig {

    @Bean
    public TokenStore tokenStore() {
        // 内存方式存储
        return new InMemoryTokenStore();
    }
}
