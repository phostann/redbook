package cc.phos.controller;

import cc.phos.entity.UserEntity;
import cc.phos.exception.RedBookException;
import cc.phos.mapper.UserMapper;
import cc.phos.response.BizCodeEnum;
import cc.phos.response.R;
import cc.phos.service.RedisService;
import cc.phos.utils.SmsUtil;
import cc.phos.utils.IDUtil;
import cc.phos.utils.TokenUtil;
import cc.phos.vo.user.UpdateUserVO;
import cc.phos.vo.user.UserRegisterVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserMapper userMapper;
    private final RedisService redisService;

    @PostMapping("/register")
    public R register(@Valid @RequestBody UserRegisterVO vo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setMobile(vo.getMobile());
        // 生成随机 id
        userEntity.setUsername(IDUtil.generateUserID());
        // 默认昵称
        userEntity.setNickname("用户" + userEntity.getMobile());
        // 默认头像
        userEntity.setAvatar("http://localhost:8081/images/default-avatar.jpeg");
        // 保存到数据库
        userMapper.insert(userEntity);
        return R.ok();
    }

    // 密码登录

    @GetMapping("/code/{mobile}")
    public R code(@PathVariable("mobile") @NotBlank String mobile) {
        // 检查缓存中是否有验证码
        String code = redisService.getValue("sms:code:" + mobile);
        if (code == null) {
            code = SmsUtil.generateSmsCode();
            // 缓存 5 分钟
            redisService.cacheValue("sms:code:" + mobile, code, 5, TimeUnit.MINUTES);
        }
        return R.ok(code);
    }

    @GetMapping("/info/{id}")
    public R getUserById(@PathVariable("id") Long id) {
        UserEntity userEntity = userMapper.selectById(id);
        return R.ok(userEntity);
    }

    @PatchMapping("/update/{id}")
    public R updateUser(@RequestBody UpdateUserVO vo, @PathVariable String id) {
        UserEntity userEntity = userMapper.selectById(id);
        if (userEntity != null) {
            BeanUtils.copyProperties(vo, userEntity, "updatedAt");
        }
        userMapper.updateById(userEntity);
        return R.ok();
    }

    @GetMapping("/test")
    public R test() {
        log.info("test redis");
        return R.ok();
    }

}
