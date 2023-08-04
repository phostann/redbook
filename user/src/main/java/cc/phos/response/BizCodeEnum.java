package cc.phos.response;

import lombok.Getter;

@Getter
public enum BizCodeEnum {
    UN_AUTHORIZATION(10000, "用户未登录或已过期"),
    LOGIN_FAILED_EXCEPTION(10001, "登录失败"),
    UNKNOWN_EXCEPTION(10002, "系统未知异常"),
    INVALID_TOKEN_EXCEPTION(10003, "非法 token"),
    VALID_FAILED_EXCEPTION(10004, "参数格式校验失败"),
    USER_NOT_FOUND_EXCEPTION(10005, "用户不存在"),
    // 资源不存在
    RESOURCE_NOT_FOUND_EXCEPTION(10006, "资源不存在");

    private final int code;
    private final String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
