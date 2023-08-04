package cc.phos.security;

import cc.phos.entity.UserEntity;
import cc.phos.exception.RedBookException;
import cc.phos.mapper.UserMapper;
import cc.phos.response.BizCodeEnum;
import cc.phos.service.RedisService;
import cc.phos.utils.TokenUtil;
import cc.phos.vo.token.TokenVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Component
@Data
public class MyAuthSuccessHandler implements AuthenticationSuccessHandler {

    private AuthType authType;
    private final TokenUtil tokenUtil;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        String key = "user:";
        switch (authType) {
            case USERNAME_PASSWORD -> key += userEntity.getUsername();
            case MOBILE_CODE -> key += userEntity.getMobile();
        }
        redisService.cacheValue(key, userEntity);
        String accessToken = this.tokenUtil.generateToken(userEntity, authType);
        String refreshToken = this.tokenUtil.generateRefreshToken(userEntity, authType);
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(new TokenVO(accessToken, refreshToken)));
        writer.flush();
    }
}
