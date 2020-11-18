---
# spring security demo 解析
#### author：闽东打桩机:chicken:
**spring security 核心内容主要分为两点：用户认证，权限效验。 原理主要是由过滤器（FilterChainProxySecurityFilterChain）内置的多个有序的过滤器对请求进行过滤，过滤成功后才能访问REST接口。想要使用该框架主要操作就是对过滤链进行配置，及自定义过滤链。下文为配置该框架的流程。**
* ***认证：应用于对用户登录进行一系列的效验。***
* ***授权：应用于用户登录成功后对用户资源访问权限的控制。***
                                                            过滤链图
<div align=center><img src="https://github.com/CTDW/spring_security_demo/blob/main/imgs/filtechainr.png" width="1000" height="450" /></div>
