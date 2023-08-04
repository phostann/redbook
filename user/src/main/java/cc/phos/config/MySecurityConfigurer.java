package cc.phos.config;

import cc.phos.properties.AuthProperties;
import cc.phos.security.*;
import cc.phos.security.mobile.MobileCodeAuthenticationFilter;
import cc.phos.security.mobile.MobileCodeAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class MySecurityConfigurer {

    private final MobileCodeAuthenticationProvider mobileCodeAuthenticationProvider;
    private final MyAuthenticationEntryPoint entryPoint;
    private final AuthProperties authProperties;
    private final MyAuthSuccessHandler successHandler;
    private final MyAuthFailedHandler failedHandler;
    private final MyAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        MobileCodeAuthenticationFilter mobileCodeAuthenticationFilter = mobileCodeAuthenticationFilter();
        // 关闭csrf
        http.csrf(AbstractHttpConfigurer::disable);
        // 添加通用 jwt 过滤器，在每次请求前验证 jwt, 如果 jwt 有效，将用户信息存入 SecurityContext
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(mobileCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(handling -> handling.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(entryPoint));
        http.sessionManagement(sessionManagement -> {
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });
        http.authorizeHttpRequests(authorizeRequests -> {
            // 放行接口
            authorizeRequests.requestMatchers(authProperties.getWhiteList()).permitAll();
            authorizeRequests.anyRequest().authenticated();
        });
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return new ProviderManager(Collections.singletonList(mobileCodeAuthenticationProvider));
    }

    @Bean
    public MobileCodeAuthenticationFilter mobileCodeAuthenticationFilter() {
        MobileCodeAuthenticationFilter filter = new MobileCodeAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        successHandler.setAuthType(AuthType.MOBILE_CODE);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failedHandler);
        return filter;
    }
}
