package com.example.lab02.config;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class ShiroConfig {

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
        Map<String, String> filterMap = new LinkedHashMap<String, String>();
        // 这里直接使用 Shiro 的 URL 过滤链保护接口，避免再叠加
        // 注解代理相关 Bean 时出现 authorizer / advisor 装配冲突。
        filterMap.put("/api/shiro/public/**", "anon");
        filterMap.put("/api/shiro/login", "anon");
        filterMap.put("/api/shiro/logout", "authc");
        filterMap.put("/api/shiro/admin", "authc,roles[ADMIN]");
        filterMap.put("/api/shiro/me", "authc");
        chain.addPathDefinitions(filterMap);
        return chain;
    }

    @Bean(name = "authorizer")
    public Authorizer authorizer(List<Realm> realms) {
        // Shiro 1.13 的自动配置在创建 securityManager 和注解处理器时，
        // 都会显式寻找名为 authorizer 的 Bean。
        // 这里直接用当前项目注册的 Realm 列表构造一个独立 Authorizer，
        // 避免再依赖 securityManager，从而消除循环依赖。
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setRealms(realms);
        return authorizer;
    }
}
