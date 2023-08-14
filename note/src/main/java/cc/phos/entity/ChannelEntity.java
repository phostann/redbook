package cc.phos.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@TableName("channel")
public class ChannelEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long sort;
    private String channelName;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
