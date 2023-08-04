package cc.phos.vo.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO {
    private String accessToken;
    private String refreshToken;
}
