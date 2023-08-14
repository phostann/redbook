package cc.phos.vo.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateChannelVO {
    @NotBlank(message = "频道名称不能为空")
    private String channelName;
}
