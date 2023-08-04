package cc.phos.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@TableName("user")
public class UserEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String mobile;
    @JsonIgnore
    private String password;
    private String brief;
    private Integer age;
    private Integer gender;
    private OffsetDateTime birthday;
    private String banner;

    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;
}
