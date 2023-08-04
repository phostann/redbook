package cc.phos.vo.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileCodeLoginVO {
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    @NotBlank(message = "验证码不能为空")
    private String code;
}
