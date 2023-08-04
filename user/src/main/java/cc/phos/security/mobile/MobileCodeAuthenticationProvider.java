package cc.phos.security.mobile;

import cc.phos.entity.UserEntity;
import cc.phos.mapper.UserMapper;
import cc.phos.service.RedisService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class MobileCodeAuthenticationProvider implements AuthenticationProvider {

    private final RedisService redisService;
    private final UserMapper userMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 判断是否支持该验证方式
        Assert.isInstanceOf(MobileCodeAuthenticationToken.class, authentication, "不支持的验证方式");
        MobileCodeAuthenticationToken mobileCodeAuthenticationToken = (MobileCodeAuthenticationToken) authentication;
        // 拿到手机号和验证码
        String mobile = mobileCodeAuthenticationToken.getPrincipal().toString();
        String code = mobileCodeAuthenticationToken.getCredentials().toString();
        log.info("手机号: {}", mobile);
        log.info("验证码: {}", code);

        if (!StringUtils.hasLength(mobile)) {
            throw new BadCredentialsException("手机号不能为空");
        }

        // 缓存中的验证码
        String cacheCode = redisService.getValue("sms:code:" + mobile);

        // 判断验证码是否正确
        if (cacheCode == null || !cacheCode.equals(code)) {
            throw new BadCredentialsException("验证码错误");
        }

        // 从数据库中查询用户
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getMobile, mobile);
        UserEntity userEntity = userMapper.selectOne(wrapper);
        if (userEntity == null) {
            throw new UsernameNotFoundException("用户不存在");
        } else {
            // 验证成功后删除验证码
            redisService.removeValue("sms:code:" + mobile);
            return new MobileCodeAuthenticationToken(userEntity, null, Collections.emptySet());
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(MobileCodeAuthenticationToken.class);
    }
}
