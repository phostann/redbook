package cc.phos.exception;

import cc.phos.response.BizCodeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedBookException extends RuntimeException {
    private int code;
    private String msg;

    public RedBookException(BizCodeEnum bizCodeEnum) {
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMsg();
    }

    public RedBookException(BizCodeEnum bizCodeEnum, String msg) {
        this.code = bizCodeEnum.getCode();
        this.msg = msg;
    }

    public RedBookException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
