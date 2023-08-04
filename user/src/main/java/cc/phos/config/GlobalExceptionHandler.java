package cc.phos.config;

import cc.phos.exception.RedBookException;
import cc.phos.response.BizCodeEnum;
import cc.phos.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.ServerException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 校验失败异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException ex) {
        log.error("参数校验失败: {}", ex.getMessage());
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        return R.error(BizCodeEnum.VALID_FAILED_EXCEPTION, "参数校验失败: " + String.join(",", fieldErrors));
    }

    // 拦截 400 Bad Request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("参数解析失败: {}", ex.getMessage());
        return R.error(BizCodeEnum.VALID_FAILED_EXCEPTION, "参数解析失败: " + ex.getMessage());
    }

    @ExceptionHandler(RedBookException.class)
    public R handleRedBookException(RedBookException ex) {
        log.error("自定义异常: {}", ex.getMsg());
        return R.error(ex.getCode(), ex.getMsg());
    }
}
