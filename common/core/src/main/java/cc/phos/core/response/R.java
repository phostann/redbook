package cc.phos.core.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    public static <T> R<T> ok(T data) {
        return new R<>(200, "success", data);
    }

    public static <T> R<T> ok() {
        return new R<>(200, "success", null);
    }

    public static <T> R<T> error(String msg) {
        return new R<>(500, msg, null);
    }

    public static <T> R<T> error(BizCodeEnum bizCodeEnum) {
        return new R<>(bizCodeEnum.getCode(), bizCodeEnum.getMsg(), null);
    }

    public static <T> R<T> error(BizCodeEnum bizCodeEnum, String msg) {
        return new R<>(bizCodeEnum.getCode(), msg, null);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg, null);
    }

    // set message
    public R<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    // set data
    public R<T> data(T data) {
        this.data = data;
        return this;
    }
}
