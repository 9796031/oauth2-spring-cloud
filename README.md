[toc]
# oauth2-spring-cloud
# 1 授权码模式
授权码方式每次请求都需要重新获取code,再换取token才能访问
## 1.1 登录获取code 
授权码模式通过用户登录后授权, 通过授权码获取token
```localhost:53020/uaa/oauth/authorize?client_id=c1&response_type=code&scope=all&redirect_uri=http://www.baidu.com```
登录用户:zhangsan
密码:123
获得授权码code在url中可以看到  

**请求登录URL时需要注意, 需要有对应关系**  
- redirect_uri要和配置的AuthorizationServer中配置的保持一致  
- 授权码模式response_type固定为code
- client_id是在AuthorizationServer中配置的client
- scope是在AuthorizationServer中配置的scope

## 1.2 通过code获取token
需要使用get提交, 因为在类中配置了支持Get方式获取token  
URL:```localhost:53020/uaa/oauth/token?code=Sv1ACY&grant_type=authorization_code&client_id=c1&client_secret=secret&redirect_uri=http://www.baidu.com```

**注意**
- 请求中的code是认证后获取到的code
- grant_type固定写死
- client_id是配置的clientID
- client_secret是配置的secret
- redirect_uri和配置保持一致

# 2 简单模式
URL:```localhost:53020/uaa/oauth/authorize?client_id=c1&response_type=token&scope=all&redirect_uri=http://www.baidu.com```  

请求url中的参数和授权码模式只是```response_type```不同, 这种方式请求可以直接获取token

token在重定向地址参数中  
```https://www.baidu.com/#access_token=73802e59-e76f-4ae5-81e7-0b055d66c4ec&token_type=bearer&expires_in=6421```
# 3 密码模式
URL:```http://localhost:53020/uaa/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=zhangsan&password=123```  
**需要有对应关系**  
可以直接返回token  
```
{
    "access_token": "73802e59-e76f-4ae5-81e7-0b055d66c4ec",
    "token_type": "bearer",
    "refresh_token": "ee96e24a-7e62-455d-8030-106c163d1fd7",
    "expires_in": 6551,
    "scope": "all"
}
```
# 4 客户端模式
URL:```http://localhost:53020/uaa/oauth/token?client_id=c1&client_secret=secret&grant_type=client_credentials```
**注意对应关系**  
响应结果:  
```
{
  "access_token": "a0bc376f-d4f9-40d9-8310-9ee897ab8169",
  "token_type": "bearer",
  "expires_in": 7199,
  "scope": "all"
}
```
# 5 配置资源服务器
ResourceServerConfigurer类  
OrderController类  
使用方法授权测试  

# 6 测试资源服务使用token方式访问
通过用户名密码模式获取token  

通过```localhost:53021/order/r1```访问资源,测试通过  
**注意, 请求头信息需要携带token信息**
key:Authorization  
value:Bearer token

# 7 JWT方式token
## 7.1 获取token
URL:```http://localhost:53020/uaa/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=zhangsan&password=123```
获得结果  
```
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJ6aGFuZ3NhbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE1OTYxMzEwNTUsImF1dGhvcml0aWVzIjpbInAxIiwicDIiXSwianRpIjoiNGM0Zjk3MTUtNTE1Yy00NmEzLWJlZGQtNWU2MTMzODZlNDdiIiwiY2xpZW50X2lkIjoiYzEifQ.jed-yHFt9DIjrs_BNLfIP-CePXtRubZJ2jZZ1yoqTrU",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJ6aGFuZ3NhbiIsInNjb3BlIjpbImFsbCJdLCJhdGkiOiI0YzRmOTcxNS01MTVjLTQ2YTMtYmVkZC01ZTYxMzM4NmU0N2IiLCJleHAiOjE1OTYzODMwNTUsImF1dGhvcml0aWVzIjpbInAxIiwicDIiXSwianRpIjoiYTkwMWQ4YzQtMmE4My00MDk1LTkwMzgtOGU0MzAyZDkwMTRkIiwiY2xpZW50X2lkIjoiYzEifQ.sPO5lHDuU04N4wYQtyXn8FqOY4x1shNDD-3DgQnJtLE",
    "expires_in": 7199,
    "scope": "all",
    "jti": "4c4f9715-515c-46a3-bedd-5e613386e47b"
}
```
通过修改头信息携带jwt token进行授权认证
## 7.2 通过资源服务验证token
URL:```http://localhost:53020/uaa/oauth/check_token?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJ6aGFuZ3NhbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE1OTYxMzEwNTUsImF1dGhvcml0aWVzIjpbInAxIiwicDIiXSwianRpIjoiNGM0Zjk3MTUtNTE1Yy00NmEzLWJlZGQtNWU2MTMzODZlNDdiIiwiY2xpZW50X2lkIjoiYzEifQ.jed-yHFt9DIjrs_BNLfIP-CePXtRubZJ2jZZ1yoqTrU```  
响应结果  
```$xslt
{
    "aud": [
        "res1"
    ],
    "user_name": "zhangsan",
    "scope": [
        "all"
    ],
    "exp": 1596131055,
    "authorities": [
        "p1",
        "p2"
    ],
    "jti": "4c4f9715-515c-46a3-bedd-5e613386e47b",
    "client_id": "c1"
}
```
## 7.3 访问资源
URL:```localhost:53021/order/r1```  
资源访问成功

# 8 使用数据库存储客户端信息
表oauth_client_details存储客户端信息  
表oauth_code存储客户端生成的code

## 8.1 测试客户端模式(通过数据库存储)
URL:```localhost:53020/uaa/oauth/token?grant_type=client_credentials&client_id=c1&client_secret=secret```  
返回结果  
```$xslt
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJzY29wZSI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIiwiUk9MRV9BUEkiXSwiZXhwIjoxNTk2MTMzMjAzLCJqdGkiOiIyMzY0NmFkNy1jNDBkLTRhNDktYWJhYy1hMjE0OWU2MmI3MTEiLCJjbGllbnRfaWQiOiJjMSJ9.RHjz17Wa0S-X_f7I9ZSWk0JajlCXRTkJZ4Fwktw47cQ",
    "token_type": "bearer",
    "expires_in": 7199,
    "scope": "ROLE_ADMIN ROLE_USER ROLE_API",
    "jti": "23646ad7-c40d-4a49-abac-a2149e62b711"
}
```
## 8.2 测试授权码模式(通过数据库存储)
URL:```http://localhost:53020/uaa/oauth/authorize?client_id=c1&response_type=code&scope=ROLE_ADMIN&redirect_uri=http://www.baidu.com```  
登录用户zhangsan 密码123获取code  
```$xslt
https://www.baidu.com/?code=UEWZaa
```
生成的code会存储在oauth_code表中