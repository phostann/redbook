package cc.phos.config;

import cc.phos.core.response.BizCodeEnum;
import cc.phos.core.response.R;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(ConstraintViolationException.class)
    public R handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("参数校验失败: {}", ex.getMessage());
        return R.error(BizCodeEnum.VALID_FAILED_EXCEPTION, "参数校验失败: " + ex.getMessage());
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentTypeMismatchException ex) {
        log.error("参数校验失败: {}", ex.getMessage());
        return R.error(BizCodeEnum.VALID_FAILED_EXCEPTION, "参数校验失败: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception ex) {
        log.error("未知异常: {}", ex.getMessage());
        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION, "未知异常: " + ex.getMessage());
    }
}
