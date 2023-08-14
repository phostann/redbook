package cc.phos.security;

import cc.phos.entity.UserEntity;
import cc.phos.properties.AuthProperties;
import cc.phos.security.mobile.MobileCodeAuthenticationToken;
import cc.phos.service.RedisService;
import cc.phos.utils.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthProperties authProperties;
    private final TokenUtil tokenUtil;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 放行白名单
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (Arrays.stream(authProperties.getWhiteList()).anyMatch(pattern -> antPathMatcher.match(pattern, request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }

        // 验证token
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = tokenHeader.substring(7);
        // 如果不是access token或者token已过期，放行
        if (!tokenUtil.isAccessToken(token) || tokenUtil.isTokenExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // token 合法
        String principle = tokenUtil.extractSubject(token);
        AuthType authType = tokenUtil.extractAuthType(token);
        AbstractAuthenticationToken authToken = null;
        switch (authType) {
            case USERNAME_PASSWORD -> {
                // TODO: 处理用户名密码登录
            }
            case MOBILE_CODE -> {
                UserEntity userEntity = redisService.getValue("user:" + principle);
                authToken = new MobileCodeAuthenticationToken(userEntity, null, Collections.emptyList());
            }
        }
        if (authToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
