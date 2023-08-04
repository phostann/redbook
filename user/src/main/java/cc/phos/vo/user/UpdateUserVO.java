package cc.phos.vo.user;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UpdateUserVO {
    private String nickname;
    private String avatar;
    private String brief;
    private Integer age;
    private Integer gender;
    private String banner;
    private OffsetDateTime birthday;
}
