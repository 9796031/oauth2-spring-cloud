package com.home.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liqingdong
 * token拦截器, 解析jwt token封装成铭文json传递给微服务
 */
public class AuthorizationFilter extends ZuulFilter {
    /**
     * 拦截的类型
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 拦截器权重, 0最高
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否拦截
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * shouldFilter=true, 该方法会被调用
     * 将token解析, 组装成铭文传递给其他微服务
     */
    @Override
    public Object run() {
        // 1.获取令牌内容
        // 获取request对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 获取权限内容
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2Authentication)) {
            return null;
        }
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        // 用户信息
        Object principal = oAuth2Authentication.getPrincipal();
        // 权限信息
        List<String> authorities = new ArrayList<>();
        oAuth2Authentication.getAuthorities().forEach(auth -> authorities.add(auth.getAuthority()));
        // 获取oauth请求对象中的其他信息
        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String, Object> requestParams = new HashMap<>(requestParameters);
        requestParams.put("principal", principal);
        requestParams.put("authorities", authorities);
        requestContext.addZuulRequestHeader("json-token", JSON.toJSONString(requestParams));
        return null;
    }
}
